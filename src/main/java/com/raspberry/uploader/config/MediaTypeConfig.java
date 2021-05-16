package com.raspberry.uploader.config;

import com.raspberry.uploader.service.MediaType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.EnumMap;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "mediatype")
public class MediaTypeConfig {

    private EnumMap<MediaType, Directory> directories = new EnumMap<>(MediaType.class);

    @Getter
    @Setter
    public static class Directory
    {
        private String path;
    }

}
