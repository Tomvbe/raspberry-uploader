package com.raspberry.uploader.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MediaTypeConfig.class)
public class AppConfiguration {
}
