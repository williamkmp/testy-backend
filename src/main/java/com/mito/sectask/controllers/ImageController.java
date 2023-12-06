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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.annotations.caller.Caller;
import com.mito.sectask.dto.response.Response;
import com.mito.sectask.dto.response.image.ImageUploadResponse;
import com.mito.sectask.entities.Image;
import com.mito.sectask.entities.User;
import com.mito.sectask.exceptions.httpexceptions.ResourceNotFoundHttpException;
import com.mito.sectask.services.image.ImageService;
import com.mito.sectask.values.MESSAGES;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping(
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Authenticated(true)
    public Response<ImageUploadResponse> uploadImageUser(
        @RequestParam("image") MultipartFile file,
        @Caller User caller,
        HttpServletRequest request
    ) {
        String baseUrl = ServletUriComponentsBuilder
                .fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();
        try {
            Image savedImage = imageService
                .saveImage(file.getBytes())
                .orElseThrow(Exception::new);

            return new Response<ImageUploadResponse>(HttpStatus.CREATED)
                .setMessage(MESSAGES.UPLOAD_SUCCESS)
                .setData(new ImageUploadResponse().setSrc(baseUrl + "/image/" + savedImage.getId().toString()));
        } catch (Exception e) {
            return new Response<ImageUploadResponse>(HttpStatus.BAD_REQUEST)
                .setMessage(MESSAGES.UPLOAD_FAIL);
        }
    }
}
