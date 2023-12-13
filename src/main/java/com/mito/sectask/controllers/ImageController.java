package com.mito.sectask.controllers;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.dto.response.Response;
import com.mito.sectask.dto.response.image.ImageData;
import com.mito.sectask.entities.File;
import com.mito.sectask.exceptions.httpexceptions.ResourceNotFoundHttpException;
import com.mito.sectask.services.image.ImageService;
import com.mito.sectask.values.MESSAGES;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/image")
@AllArgsConstructor
public class ImageController {

    private ImageService imageService;

    @GetMapping("/{imageId}")
    public ResponseEntity<byte[]> get(@PathVariable("imageId") Long id) {
        Optional<File> maybeImage = imageService.findById(id);

        if (maybeImage.isEmpty()) {
            throw new ResourceNotFoundHttpException();
        }

        File image = maybeImage.get();
        return ResponseEntity
            .ok()
            .header("Content-Type", image.getContentType())
            .body(image.getBytes());
    }

    @PostMapping(
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Authenticated(true)
    public Response<ImageData> uploadImage(
        @RequestParam("image") MultipartFile file
    ) {
        String[] paths = file.getOriginalFilename().split("\\.");
        String imageExtension = paths[paths.length - 1];

        try {
            File savedImage = imageService
                .saveImage(file.getBytes(), imageExtension)
                .orElseThrow(Exception::new);

            String imageSrc = imageService
                .getImageUrl(savedImage.getId())
                .orElseThrow(Exception::new);
            return new Response<ImageData>(HttpStatus.CREATED)
                .setMessage(MESSAGES.UPLOAD_SUCCESS)
                .setData(
                    new ImageData()
                        .setId(savedImage.getId().toString())
                        .setSrc(imageSrc)
                );
        } catch (Exception e) {
            return new Response<ImageData>(HttpStatus.BAD_REQUEST)
                .setMessage(MESSAGES.UPLOAD_FAIL);
        }
    }
}
