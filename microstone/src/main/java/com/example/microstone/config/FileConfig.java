package com.example.microstone.config;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Log4j2
@Configuration
public class FileConfig {

    public static String rootPath = "C:\\Users\\hanul\\IdeaProjects\\microstone\\microstone\\src\\main\\resources";

     public static String getPath(){
         return rootPath;
     }


}
