package com.mito.sectask;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SectaskApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SectaskApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
