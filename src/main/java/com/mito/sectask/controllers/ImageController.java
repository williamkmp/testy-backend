package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.annotations.caller.Caller;
import com.mito.sectask.dto.response.StandardResponse;
import com.mito.sectask.dto.response.image.ImageUploadResponse;
import com.mito.sectask.entities.Image;
import com.mito.sectask.entities.User;
import com.mito.sectask.exceptions.httpexceptions.RequestHttpException;
import com.mito.sectask.services.image.ImageService;
import com.mito.sectask.values.MESSAGES;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/{imageId}")
    public ResponseEntity<byte[]> get(@PathVariable("imageId") Long id) {
        Optional<Image> maybeImage = imageService.findById(id);

        if (maybeImage.isEmpty()) {
            throw new RequestHttpException(MESSAGES.ERROR_RESOURCE_NOT_FOUND);
        }

        Image image = maybeImage.get();
        return ResponseEntity
            .ok()
            .header("Content-Type", "image/*")
            .body(image.getFile());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<byte[]> getUserImage(
        @PathVariable("userId") Long userId
    ) {
        Image userProfilePicture = imageService
            .findUserProfilePicture(userId)
            .orElseThrow(() ->
                new RequestHttpException(MESSAGES.UPLOAD_FAIL)
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
    public StandardResponse<ImageUploadResponse> uploadImageUser(
        @RequestParam("image") MultipartFile file,
        @Caller User caller
    ) {
        try {
            Image savedImage = imageService
                .saveUserImage(caller.getId(), file.getBytes())
                .orElseThrow(Exception::new);

            return new StandardResponse<ImageUploadResponse>()
                .setStatus(HttpStatus.CREATED)
                .setData(new ImageUploadResponse().setId(savedImage.getId()));
        } catch (Exception e) {
            throw new RequestHttpException(MESSAGES.UPLOAD_FAIL);
        }
    }

    @DeleteMapping(path = "/user")
    @Authenticated(true)
    public ResponseEntity<Object> deleteImageUser(@Caller User caller) {
        imageService.deleteUserProfilePicture(caller.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/project/profile/{projectId}")
    public ResponseEntity<byte[]> getProjectProfile(
        @PathVariable("projectId") Long projectId
    ) {
        Image picture = imageService
            .getProjectPicture(projectId)
            .orElseThrow(() ->
                new RequestHttpException(MESSAGES.UPLOAD_FAIL)
            );

        return ResponseEntity
            .ok()
            .header("Content-Type", "image/*")
            .body(picture.getFile());
    }

    @PostMapping(
        path = "/project/{projectId}",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Authenticated(true)
    public StandardResponse<ImageUploadResponse> uploadProjectProfile(
        @PathVariable("projectId") Long projectId,
        @RequestParam("image") MultipartFile file
    ) {
        try {
            Image savedImage = imageService
                .saveUserImage(projectId, file.getBytes())
                .orElseThrow(Exception::new);

            return new StandardResponse<ImageUploadResponse>()
                .setStatus(HttpStatus.CREATED)
                .setData(new ImageUploadResponse().setId(savedImage.getId()));
        } catch (Exception e) {
            throw new RequestHttpException(MESSAGES.UPLOAD_FAIL);
        }
    }

    @DeleteMapping(path = "/project/{projectId}")
    @Authenticated(true)
    public ResponseEntity<Object> deleteProjectPicture(
        @PathVariable("projectId") Long projectId
    ) {
        imageService.deleteProjectPicture(projectId);
        return ResponseEntity.ok().build();
    }
}
