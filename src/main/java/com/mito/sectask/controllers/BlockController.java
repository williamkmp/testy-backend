package com.mito.sectask.controllers;

import org.springframework.web.bind.annotation.RestController;
import com.mito.sectask.dto.dto.MenuPreviewDto;
import com.mito.sectask.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
public class BlockController {
    
    @GetMapping("/")
    public Response<MenuPreviewDto> getMethodName(@RequestParam String param) {
        // TODO: impelement me
        throw new UnsupportedOperationException("endpoint not implemented yed");
    }
    
}
