package com.rest.area.api.multipart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MultipartBean implements Serializable {

    private @JsonIgnore Object id;
    private @JsonIgnore Object source;

    private String contentType;
    private String filename;
    private Long size;
    private Long lastModified;
    private Map<String,Object> metadata;

}