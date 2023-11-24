package com.mito.sectask.controllers;

import java.io.IOException;
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
import com.mito.sectask.dto.response.StandardResponse;
import com.mito.sectask.dto.response.image.ImageUploadResponse;
import com.mito.sectask.entities.ImageEntity;
import com.mito.sectask.entities.UserEntity;
import com.mito.sectask.exceptions.httpexceptions.RequestHttpException;
import com.mito.sectask.exceptions.httpexceptions.UnauthorizedHttpException;
import com.mito.sectask.services.image.ImageService;
import com.mito.sectask.services.user.UserService;
import com.mito.sectask.values.Message;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final UserService userService;

    @GetMapping("/{imageId}")
    @Authenticated(true)
    public ResponseEntity<byte[]> get(@PathVariable("imageId") Long id) {
        Optional<ImageEntity> maybeImage = imageService.findById(id);

        if (maybeImage.isEmpty()) {
            throw new RequestHttpException(Message.ERROR_RESOURCE_NOT_FOUND);
        }

        ImageEntity image = maybeImage.get();
        return ResponseEntity
            .ok()
            .header("Content-Type", "image/*")
            .body(image.getFile());
    }

    @GetMapping("/user/{userId}")
    @Transactional
    public ResponseEntity<byte[]> getUserImage(
        @PathVariable("userId") Long userId
    ) {
        Optional<UserEntity> maybeUser = userService.findById(userId);
        if (maybeUser.isEmpty()) {
            throw new UnauthorizedHttpException();
        }

        ImageEntity userImage = maybeUser.get().getImage();

        if (userImage == null) {
            throw new RequestHttpException(Message.ERROR_RESOURCE_NOT_FOUND);
        }

        return ResponseEntity
            .ok()
            .header("Content-Type", "image/*")
            .body(userImage.getFile());
    }

    @PostMapping(
        path = "/user",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Authenticated(true)
    public StandardResponse<ImageUploadResponse> uploadImageUser(
        @RequestParam("image") MultipartFile file,
        @Caller UserEntity caller
    ) {
        //TODO: implement logic for user with existing image
        Optional<ImageEntity> maybeImage;
        try {
            maybeImage = imageService.saveImage(file.getBytes());
        } catch (IOException e) {
            maybeImage = Optional.empty();
        }
        if (maybeImage.isEmpty()) {
            throw new RequestHttpException(Message.ERROR_UPLOAD_FAILED);
        }
        ImageEntity savedImage = maybeImage.get();
        caller.setImage(savedImage);
        UserEntity updatedCaller = userService
            .updateUser(caller)
            .orElseThrow(() ->
                new RequestHttpException(Message.ERROR_UPLOAD_FAILED)
            );
        return new StandardResponse<ImageUploadResponse>()
            .setStatus(HttpStatus.CREATED)
            .setData(new ImageUploadResponse().setId(updatedCaller.getId()));
    }
}
