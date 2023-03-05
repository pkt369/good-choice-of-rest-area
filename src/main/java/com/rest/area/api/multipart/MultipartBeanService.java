package com.rest.area.api.multipart;

import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface MultipartBeanService {

    public Optional<MultipartBean> upload(String path, MultipartFile file, Map<String,Object> metadata);

    public Optional<MultipartBean> download(String path);

    public Optional<Page<MultipartBean>> find(String path, Pageable pageable);

}
