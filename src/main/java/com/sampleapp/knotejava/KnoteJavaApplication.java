package com.sampleapp.knotejava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(NoteProperties.class)
public class KnoteJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnoteJavaApplication.class, args);
    }


}
