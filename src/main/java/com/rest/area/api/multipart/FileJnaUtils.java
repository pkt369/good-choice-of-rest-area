package com.rest.area.api.multipart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jna.Platform;
import com.sun.jna.platform.mac.XAttrUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileJnaUtils {

    private FileJnaUtils() {}

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> void setAttribute(Path path, T value) throws IOException {
        String attrName = "user:extended";
        log.info("save: "+attrName);

        if(Platform.isMac()){
            String xpath = Normalizer.normalize(path.toFile().getAbsolutePath(), Normalizer.Form.NFD);
            XAttrUtil.setXAttr(xpath, attrName, objectMapper.writeValueAsString(value));
        } else {
            Files.setAttribute(path, attrName, objectMapper.writeValueAsBytes(value));
        }
    }

    public static <T> T getAttribute(Path path, Class<T> type) throws IOException{
        String attrName = "user:extended";//+ClassUtils.getShortName(value.getClass());
//		String attrName = "user:"+ClassUtils.getShortName(type);
//		System.err.println("load: "+attrName);

        if(Platform.isMac()){
            String xpath = Normalizer.normalize(path.toFile().getAbsolutePath(), Normalizer.Form.NFD);
            String content = XAttrUtil.getXAttr(xpath, attrName);
            return objectMapper.readValue(content, type);
        }else{
            byte[] content = (byte[])Files.getAttribute(path, attrName);
            return objectMapper.readValue(content, type);
        }
    }


    public static void setAttribute(Path path, String name, String value) throws IOException{
        String attrName = "user:"+name;

        if(Platform.isMac()){
            String xpath = Normalizer.normalize(path.toFile().getAbsolutePath(), Normalizer.Form.NFD);
            XAttrUtil.setXAttr(xpath, attrName, value);
        }else{
            Files.setAttribute(path, attrName, value.getBytes());
        }
    }

    public static String getAttribute(Path path, String name) throws IOException {
        String attrName = "user:"+name;

        if(Platform.isMac()){
            String xpath = Normalizer.normalize(path.toFile().getAbsolutePath(), Normalizer.Form.NFD);
            String content = XAttrUtil.getXAttr(xpath, attrName);
            return content;
        }else{
            byte[] content = (byte[])Files.getAttribute(path, attrName);
            return new String(content);
        }
    }

}
