package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.annotations.caller.Caller;
import com.mito.sectask.dto.dto.UserDto;
import com.mito.sectask.dto.request.user.UserUpdatePasswordRequest;
import com.mito.sectask.dto.request.user.UserUpdateProfileRequest;
import com.mito.sectask.dto.response.Response;
import com.mito.sectask.entities.File;
import com.mito.sectask.entities.User;
import com.mito.sectask.exceptions.httpexceptions.UnauthorizedHttpException;
import com.mito.sectask.services.encoder.PasswordEncocder;
import com.mito.sectask.services.image.ImageService;
import com.mito.sectask.services.user.UserService;
import com.mito.sectask.utils.Util;
import com.mito.sectask.values.MESSAGES;
import com.mito.sectask.values.VALIDATION;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncocder encoder;
    private final ImageService imageService;

    @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @Authenticated(true)
    public Response<UserDto> me(@Caller User caller) {
        String imageId = (caller.getImage() != null)
            ? caller.getImage().getId().toString()
            : null;
        return new Response<UserDto>(HttpStatus.OK)
            .setData(
                new UserDto()
                    .setId(caller.getId().toString())
                    .setEmail(caller.getEmail())
                    .setTagName(caller.getTagName())
                    .setFullName(caller.getFullName())
                    .setImageId(imageId)
            );
    }

    @GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Authenticated(true)
    public Response<UserDto> getUser(@PathVariable("userId") Long userId) {
        Optional<User> maybeUser = userService.findById(userId);

        if (maybeUser.isEmpty()) {
            return new Response<UserDto>(HttpStatus.BAD_REQUEST)
                .setMessage(MESSAGES.ERROR_RESOURCE_NOT_FOUND);
        }

        User user = maybeUser.get();
        String imageId = (user.getImage() != null)
            ? user.getImage().getId().toString()
            : null;

        return new Response<UserDto>(HttpStatus.OK)
            .setData(
                new UserDto()
                    .setId(user.getId().toString())
                    .setEmail(user.getEmail())
                    .setTagName(user.getTagName())
                    .setFullName(user.getFullName())
                    .setImageId(imageId)
            );
    }

    @PutMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Transactional
    @Authenticated(true)
    public Response<UserDto> updateProfile(
        @Valid @RequestBody UserUpdateProfileRequest request,
        @Caller User caller
    ) {
        boolean isTagNameAvailable = Boolean.TRUE.equals(
            userService.checkTagNameIsAvailable(
                request.getTagName(),
                caller.getId()
            )
        );

        boolean isEmailAvailable = Boolean.TRUE.equals(
            userService.checkEmailIsAvailable(
                request.getEmail(),
                caller.getId()
            )
        );

        if (!isTagNameAvailable || !isEmailAvailable) {
            Map<String, String> validationError = new HashMap<>();
            validationError.put(
                "tagName",
                !isTagNameAvailable ? VALIDATION.UNIQUE : null
            );
            validationError.put(
                "email",
                !isEmailAvailable ? VALIDATION.UNIQUE : null
            );

            return new Response<UserDto>(HttpStatus.BAD_REQUEST)
                .setMessage(MESSAGES.UPDATE_FAIL)
                .setError(validationError);
        }

        // Update user data
        caller.setEmail(request.getEmail());
        caller.setTagName(request.getTagName());
        caller.setFullName(request.getFullName());
        Optional<User> maybeUser = userService.updateUser(caller);
        if (maybeUser.isEmpty()) {
            throw new UnauthorizedHttpException();
        }
        User updatedUser = maybeUser.get();

        // Updating user image
        Long newImageId = Util.String.toLong(request.getImageId()).orElse(null);

        Optional<File> maybeImage = imageService.updateUserImage(
            caller.getId(),
            newImageId
        );
        String imageId = maybeImage.isPresent()
            ? maybeImage.get().getId().toString()
            : null;

        return new Response<UserDto>(HttpStatus.OK)
            .setMessage(MESSAGES.UPDATE_SUCCESS)
            .setData(
                new UserDto()
                    .setId(updatedUser.getId().toString())
                    .setEmail(updatedUser.getEmail())
                    .setTagName(updatedUser.getTagName())
                    .setFullName(updatedUser.getFullName())
                    .setImageId(imageId)
            );
    }

    @Authenticated(true)
    @PutMapping(path = "/password")
    public Response<UserDto> updatePassword(
        @RequestBody @Valid UserUpdatePasswordRequest request,
        @Caller User caller
    ) {
        boolean isPasswordMatch = userService.validatePassword(
            caller.getId(),
            request.getOldPassword()
        );

        if (!isPasswordMatch) {
            Map<String, String> validationError = new HashMap<>();
            validationError.put("oldPassword", VALIDATION.WRONG);

            return new Response<UserDto>(HttpStatus.BAD_REQUEST)
                .setMessage(MESSAGES.UPDATE_FAIL)
                .setError(validationError);
        }

        String encodedPassword = encoder.encode(request.getNewPassword());
        caller.setPassword(encodedPassword);
        User updatedUser = userService
            .updateUser(caller)
            .orElseThrow(UnauthorizedHttpException::new);

        String imageId = caller.getImage() != null
            ? caller.getImage().getId().toString()
            : null;

        return new Response<UserDto>(HttpStatus.OK)
            .setMessage(MESSAGES.UPDATE_SUCCESS)
            .setData(
                new UserDto()
                    .setId(updatedUser.getId().toString())
                    .setEmail(updatedUser.getEmail())
                    .setTagName(updatedUser.getTagName())
                    .setFullName(updatedUser.getFullName())
                    .setImageId(imageId)
            );
    }
}
