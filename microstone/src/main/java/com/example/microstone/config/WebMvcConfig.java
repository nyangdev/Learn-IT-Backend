package com.example.microstone.config;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Log4j2
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    //이거 안하면 사진 올릴떄마다 서버 재부팅해야함
    //https://herongmirong.tistory.com/135
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resource/static/files")
                .addResourceLocations("file:src/main/resources/static/files/");
    }
}
