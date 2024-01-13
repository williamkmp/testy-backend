package com.mito.sectask.controllers;

import com.mito.sectask.dto.response.Response;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.JavaVersion;
import org.springframework.core.SpringVersion;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Value("${spring.application.name:APPLICATION}")
    private String applicationName;

    @Value("${spring.application.version:0.0.1}")
    private String applicationVersion;

    @GetMapping(path = "/app/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Object> sayHi() {
        Map<String, Object> responseBody = new HashMap<>();

        responseBody.put("application", applicationName);
        responseBody.put("application-version", applicationVersion);
        responseBody.put("java-version", JavaVersion.getJavaVersion());
        responseBody.put("engine-version", "Spring_Boot-" + SpringVersion.getVersion());
        responseBody.put("dateTime", new Date());

        return new Response<Object>(HttpStatus.OK).setData(responseBody);
    }
}
