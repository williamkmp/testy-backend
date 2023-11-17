package com.mito.sectask.controllers;

import com.mito.sectask.dto.response.StandardResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
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

    @GetMapping(
        path = "/app/hi",
        consumes = MediaType.ALL_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public StandardResponse<Map<String, Object>> sayHi() {
        Map<String, Object> responseBody = new HashMap<>();

        responseBody.put("application", applicationName);
        responseBody.put("version", applicationVersion);
        responseBody.put("dateTime", new Date());

        return new StandardResponse<Map<String, Object>>()
            .setStatus(HttpStatus.OK)
            .setData(responseBody);
    }
}
