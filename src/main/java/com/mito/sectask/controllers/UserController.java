package com.mito.sectask.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.annotations.caller.Caller;
import com.mito.sectask.dto.request.user.UserUpdatePasswordRequest;
import com.mito.sectask.dto.request.user.UserUpdateProfileRequest;
import com.mito.sectask.dto.response.Response;
import com.mito.sectask.dto.response.user.UserMeResponse;
import com.mito.sectask.dto.response.user.UserUpdateProfileResponse;
import com.mito.sectask.entities.User;
import com.mito.sectask.exceptions.httpexceptions.UnauthorizedHttpException;
import com.mito.sectask.services.encoder.PasswordEncocder;
import com.mito.sectask.services.user.UserService;
import com.mito.sectask.values.MESSAGES;
import com.mito.sectask.values.VALIDATION;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncocder encoder;

    @Authenticated(true)
    @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<UserMeResponse> me(@Caller User caller) {
        return new Response<UserMeResponse>(HttpStatus.OK)
            .setData(
                new UserMeResponse()
                    .setId(caller.getId())
                    .setEmail(caller.getEmail())
                    .setTagName(caller.getTagName())
                    .setFullName(caller.getFullName())
            );
    }

    @Authenticated(true)
    @PutMapping(
        path = "",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<UserUpdateProfileResponse> updateProfile(
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

            return new Response<UserUpdateProfileResponse>(
                HttpStatus.BAD_REQUEST
            )
                .setMessage(MESSAGES.UPDATE_FAIL)
                .setError(validationError);
        }

        caller.setEmail(request.getEmail());
        caller.setTagName(request.getTagName());
        caller.setFullName(request.getFullName());

        Optional<User> maybeUser = userService.updateUser(caller);

        if (maybeUser.isEmpty()) {
            throw new UnauthorizedHttpException();
        }
        User updatedUser = maybeUser.get();

        return new Response<UserUpdateProfileResponse>(HttpStatus.OK)
            .setMessage(MESSAGES.UPLOAD_SUCCESS)
            .setData(
                new UserUpdateProfileResponse()
                    .setId(updatedUser.getId().toString())
                    .setEmail(updatedUser.getEmail())
                    .setTagName(updatedUser.getTagName())
                    .setFullName(updatedUser.getFullName())
            );
    }

    @Authenticated(true)
    @PutMapping(path = "/password")
    public Response<UserUpdateProfileResponse> updatePassword(
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

            return new Response<UserUpdateProfileResponse>(
                HttpStatus.BAD_REQUEST
            )
                .setMessage(MESSAGES.UPDATE_FAIL)
                .setError(validationError);
        }

        String encodedPassword = encoder.encode(request.getNewPassword());
        caller.setPassword(encodedPassword);
        User updatedUser = userService
            .updateUser(caller)
            .orElseThrow(UnauthorizedHttpException::new);

        return new Response<UserUpdateProfileResponse>(HttpStatus.OK)
            .setMessage(MESSAGES.UPDATE_SUCCESS)
            .setData(
                new UserUpdateProfileResponse()
                    .setId(updatedUser.getId().toString())
                    .setEmail(updatedUser.getEmail())
                    .setTagName(updatedUser.getTagName())
                    .setFullName(updatedUser.getFullName())
            );
    }
}
