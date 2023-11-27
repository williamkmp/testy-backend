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
import com.mito.sectask.annotations.caller.Caller;
import com.mito.sectask.entities.ImageEntity;
import com.mito.sectask.entities.UserEntity;
import com.mito.sectask.exceptions.httpexceptions.RequestHttpException;
import com.mito.sectask.services.image.ImageService;
import com.mito.sectask.values.ERROR;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/{imageId}")
    public ResponseEntity<byte[]> get(@PathVariable("imageId") Long id) {
        Optional<ImageEntity> maybeImage = imageService.findById(id);

        if (maybeImage.isEmpty()) {
            throw new RequestHttpException(ERROR.ERROR_RESOURCE_NOT_FOUND);
        }

        ImageEntity image = maybeImage.get();
        return ResponseEntity
            .ok()
            .header("Content-Type", "image/*")
            .body(image.getFile());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<byte[]> getUserImage(
        @PathVariable("userId") Long userId
    ) {
        ImageEntity userProfilePicture = imageService
            .findUserProfilePicture(userId)
            .orElseThrow(() ->
                new RequestHttpException(ERROR.ERROR_UPLOAD_FAILED)
            );

        return ResponseEntity
            .ok()
            .header("Content-Type", "image/*")
            .body(userProfilePicture.getFile());
    }

    @PostMapping(
        path = "/user",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Authenticated(true)
    public ResponseEntity<byte[]> uploadImageUser(
        @RequestParam("image") MultipartFile file,
        @Caller UserEntity caller
    ) {
        try {
            ImageEntity savedImage = imageService
                .saveUserImage(caller.getId(), file.getBytes())
                .orElseThrow(Exception::new);

            return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(savedImage.getFile());
        } catch (Exception e) {
            throw new RequestHttpException(ERROR.ERROR_UPLOAD_FAILED);
        }
    }
}
