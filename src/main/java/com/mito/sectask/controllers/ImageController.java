package com.mito.sectask.controllers;

import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mito.sectask.entities.Image;
import com.mito.sectask.exceptions.httpexceptions.ResourceNotFoundHttpException;
import com.mito.sectask.services.image.ImageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/{imageId}")
    public ResponseEntity<byte[]> get(@PathVariable("imageId") Long id) {
        Optional<Image> maybeImage = imageService.findById(id);

        if (maybeImage.isEmpty()) {
            throw new ResourceNotFoundHttpException();
        }

        Image image = maybeImage.get();
        return ResponseEntity
            .ok()
            .header("Content-Type", "image/*")
            .body(image.getFile());
    }
}
