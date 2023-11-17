package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.dto.response.StandardResponse;
import com.mito.sectask.dto.response.image.ImageUploadResponse;
import com.mito.sectask.entities.ImageEntity;
import com.mito.sectask.services.image.ImageService;
import com.mito.sectask.values.Message;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(path = "/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping(
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Authenticated(true)
    @ResponseBody
    public StandardResponse<ImageUploadResponse> upload(
        @RequestParam("file") MultipartFile file
    ) throws IOException {
        Optional<ImageEntity> maybeImage = imageService.saveImage(
            file.getBytes()
        );

        if (maybeImage.isEmpty()) {
            return new StandardResponse<ImageUploadResponse>()
                .setStatus(HttpStatus.BAD_REQUEST)
                .setError(Message.ERROR_UPLOAD_FAILED);
        }

        ImageEntity savedImage = maybeImage.get();
        return new StandardResponse<ImageUploadResponse>()
            .setStatus(HttpStatus.CREATED)
            .setData(new ImageUploadResponse().setId(savedImage.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Long id) {
        Optional<ImageEntity> maybeImage = imageService.findById(id);

        if (maybeImage.isEmpty()) {
            return ResponseEntity
                .badRequest()
                .body(
                    new StandardResponse<>()
                        .setStatus(HttpStatus.NOT_FOUND)
                        .setError(Message.ERROR_RESOURCE_NOT_FOUND)
                );
        }
        
        ImageEntity image = maybeImage.get();
        return ResponseEntity
            .ok()
            .header("Content-Type", "image/*")
            .body(image.getFile());
    }
}
