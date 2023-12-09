package com.mito.sectask.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mito.sectask.dto.response.Response;


@RestController
@RequestMapping("/page")
public class PageController {
    @GetMapping("path")
    public Response<Object> getUserPages() {
        

        return new Response<Object>(HttpStatus.OK);
    }
    
}
