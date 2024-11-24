package com.example.microstone.util;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@Data
@Log4j2
public class MediaUtils {
    private static Map<String, MediaType> mediaMap;

    static{
        mediaMap = new HashMap<String, MediaType>();
        //lowercase letter
        mediaMap.put("jpg", MediaType.IMAGE_JPEG);
        mediaMap.put("jpeg", MediaType.IMAGE_JPEG);
        mediaMap.put("png", MediaType.IMAGE_PNG);
        mediaMap.put("gif", MediaType.IMAGE_GIF);
        mediaMap.put("webp", MediaType.IMAGE_PNG);

        //uppercase letter
        mediaMap.put("JPG", MediaType.IMAGE_JPEG);
        mediaMap.put("JPEG", MediaType.IMAGE_JPEG);
        mediaMap.put("GIF", MediaType.IMAGE_GIF);
        mediaMap.put("PNG", MediaType.IMAGE_PNG);
        mediaMap.put("WEBP", MediaType.IMAGE_PNG);
    }

    public static MediaType getMediaType(String extension) {
        if(extension == null){
            return null;
        }
        return mediaMap.get(extension.toLowerCase());
    }
}
