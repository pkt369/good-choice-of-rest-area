package com.rest.area.api.multipart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.BasicLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/multipart")
public class MultipartBeanController {

    private final MultipartBeanService multipartBeanService;

    @RequestMapping(method= RequestMethod.POST, consumes="multipart/form-data")
    public @ResponseBody ResponseEntity<?> upload(
            @RequestParam(value="file") MultipartFile file,
            @RequestParam(value="filename", required=false) String filename,
            @RequestParam HashMap<String,Object> metadata) {

        return upload("", file, filename, metadata);
    }

    @RequestMapping(value="/{group}", method=RequestMethod.POST, consumes="multipart/form-data")
    public @ResponseBody ResponseEntity<?> upload(@PathVariable String group,
                                                  @RequestParam(value="file") MultipartFile file,
                                                  @RequestParam(value="filename", required=false) String filename,
                                                  @RequestParam HashMap<String,Object> metadata) {

        metadata.put("filename", filename);
        Optional<MultipartBean> content = multipartBeanService.upload(group, file, metadata);

        return content
                .map((source)
                        -> ResponseEntity.ok(toModel(source)))
                .orElseGet(()
                        -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @RequestMapping(value="/{group}", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<?> items(@PathVariable String group, Pageable pageable){

        Optional<Page<MultipartBean>> contents = multipartBeanService.find(group, pageable);

        if (contents.isPresent()) {
            return ResponseEntity.ok(toCollectionModel(contents.get()));

        } else {
            Optional<MultipartBean> content = multipartBeanService.download(group);

            return content
                    .map((source)
                            -> ResponseEntity.ok(toModel(source)))
                    .orElseGet(()
                            -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        }
    }

    @RequestMapping(value="/{group}/{id}", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<?> item(@PathVariable String group, @PathVariable String id){

        Optional<MultipartBean> content = multipartBeanService.download(group+"/"+id);

        return content
                .map((source)
                        -> ResponseEntity.ok(toModel(source)))
                .orElseGet(()
                        -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @RequestMapping(value="/{group}/{id}", method=RequestMethod.GET, params = {"flag=preview"})
    public View preview(@PathVariable String group, @PathVariable String id) throws ResourceNotFoundException{
        Optional<MultipartBean> content = multipartBeanService.download(group+"/"+id);
        return new MultipartBeanView(content, false);
    }

    @RequestMapping(value="/{group}/{id}", method=RequestMethod.GET, params = {"flag=download"})
    public View download(@PathVariable String group, @PathVariable String id) throws ResourceNotFoundException {
        Optional<MultipartBean> content = multipartBeanService.download(group+"/"+id);
        return new MultipartBeanView(content, true);
    }


    protected EntityModel<MultipartBean> toModel(MultipartBean source){
        Link self = BasicLinkBuilder.linkToCurrentMapping().slash("multipart").slash(source.getId()).withSelfRel();
        return EntityModel.of(source, self);
    }

    protected PagedModel<EntityModel<MultipartBean>> toCollectionModel(Page<MultipartBean> source){
        List<EntityModel<MultipartBean>> content = new ArrayList<>();
        source.forEach(c -> content.add(toModel(c)));
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(source.getSize(), source.getNumber(), source.getTotalElements());
        Link self = BasicLinkBuilder.linkToCurrentMapping().slash("multipart").withSelfRel();
        return PagedModel.of(content, metadata, self);
    }
}
