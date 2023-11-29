package com.mito.sectask.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.dto.response.StandardResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/project")
public class ProjectController {
      
    @PostMapping
    @Authenticated(true)
    public StandardResponse createProject(){
        //TODO: implement this
        return null;
    }
}
