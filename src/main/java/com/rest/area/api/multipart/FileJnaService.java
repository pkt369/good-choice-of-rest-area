package com.rest.area.api.multipart;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@ConfigurationProperties(prefix = "com.rest.area.api.multipart")
public class FileJnaService implements MultipartBeanService, InitializingBean, DisposableBean {

    private Path root = Paths.get("target/storage");

    public void setLocation(String location) {
        this.root = Paths.get(location);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }
        log.info("Location: "+root);
    }

    @Override
    public void destroy() throws Exception {

    }

    public Optional<MultipartBean> upload(String path, MultipartFile file, Map<String,Object> metadata){
        log.info("path : {}", path);
        log.info("file : {}", file);
        log.info("metadata : {}", metadata);
        try {
            Path parent = root.resolve(path.startsWith(File.separator) ? path.substring(1) : path);
            if(!Files.exists(parent)) {
                Files.createDirectories(parent);
            }

            String pathname = ObjectUtils.isEmpty(metadata.get("filename"))
                    ? UUID.randomUUID().toString()
                    : metadata.get("filename").toString();

            String filename = ObjectUtils.isEmpty(metadata.get("filename"))
                    ? file.getOriginalFilename()
                    : metadata.get("filename").toString();

            Path target = parent.resolve(pathname);
//            if (Files.exists(target)) {
//                throw new FileAlreadyExistsException("oops");
//            }

            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            FileJnaUtils.setAttribute(target, "filename", filename);
            metadata.put("filename", filename);
            FileJnaUtils.setAttribute(target, "contentType", file.getContentType());
            metadata.put("contentType", file.getContentType());

            FileJnaUtils.setAttribute(target, metadata);

            ////////////////////////////////////////////////
            MultipartBean m = convert(target);

            log.info("File has been successfully upload to: "+target);
            return Optional.of(m);

        } catch(Exception e) {
            log.info("File upload error: "+path);
            return Optional.empty();
        }
    }
    @Override
    public Optional<MultipartBean> download(String path) {
        try {
            Path target = root.resolve(path);
            if(! Files.exists(target)) {
                throw new FileNotFoundException();
            }

            ////////////////////////////////////////////////
            MultipartBean m = convert(target);
            log.info("File has been successfully download from: "+target);
            return Optional.of(m);

        }catch(Exception e) {
            log.info("File download error: "+path);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Page<MultipartBean>> find(String path, Pageable pageable) {


        try {
            Path target = root.resolve(path);
            if(! Files.exists(target) || !Files.isDirectory(target) ) {
                throw new FileNotFoundException();
            }

            int stx = pageable.getPageNumber() * pageable.getPageSize();
            int end = stx + pageable.getPageSize();
            AtomicInteger count = new AtomicInteger(0);

            List<MultipartBean> list = Files.list(target)
                    .sorted((a,b)->{
//					pageable.getSort().getOrderFor("filename");
//					for(Sort.Order order : pageable.getSort()) {
//						if("filename".equals(order.getProperty())){
//						}
//						System.err.println(order);
//					}
                        return a.compareTo(b);

                    })

                    .filter((p)->{
                        if(Files.isDirectory(p)) return false;
                        int idx = count.getAndAdd(1);
                        return idx >= stx && idx < end;

                    }).map((p)->{
                        MultipartBean m = convert(p);
                        return m;

                    }).collect(Collectors.toList());


            PageImpl<MultipartBean> page =  new PageImpl<>(list, pageable, count.get());
            log.info("File has been successfully find from: "+target);
            return Optional.of(page);

        }catch(Exception e) {
            log.info("File find error: "+path);
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    private MultipartBean convert(Path target) {

        MultipartBean m = new MultipartBean();

        m.setId(root.toUri().relativize(target.toUri()));
        m.setSource(target);
        try {
            m.setSize(Files.size(target));
        }catch(Exception e) {

        }
        try {
            m.setLastModified(Files.getLastModifiedTime(target).toMillis());
        }catch(Exception e) {

        }

        try {
            m.setFilename(FileJnaUtils.getAttribute(target, "filename"));
        }catch(Exception e) {
            m.setFilename(target.getFileName().toString());
        }

        try {
            m.setContentType(FileJnaUtils.getAttribute(target, "contentType"));
        }catch(Exception e) {
            try {
                m.setContentType(Files.probeContentType(target));
            }catch(Exception e1) {
            }
        }

        try {
            m.setMetadata(FileJnaUtils.getAttribute(target, Map.class));
        }catch(Exception e) {
            m.setMetadata(new HashMap<>());
        }
        return m;
    }
}
