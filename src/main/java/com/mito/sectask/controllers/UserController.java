package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.annotations.caller.Caller;
import com.mito.sectask.dto.request.user.UserUpdatePasswordRequest;
import com.mito.sectask.dto.request.user.UserUpdateProfileRequest;
import com.mito.sectask.dto.response.StandardResponse;
import com.mito.sectask.dto.response.user.UserMeResponse;
import com.mito.sectask.dto.response.user.UserUpdateProfileResponse;
import com.mito.sectask.entities.User;
import com.mito.sectask.exceptions.httpexceptions.RequestHttpException;
import com.mito.sectask.exceptions.httpexceptions.UnauthorizedHttpException;
import com.mito.sectask.services.encoder.PasswordEncocder;
import com.mito.sectask.services.user.UserService;
import com.mito.sectask.values.VALIDATION;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Authenticated(true)
    @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public StandardResponse<UserMeResponse> me(@Caller User caller) {
        return new StandardResponse<UserMeResponse>()
            .setStatus(HttpStatus.OK)
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
    public StandardResponse<UserUpdateProfileResponse> updateProfile(
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

            throw new RequestHttpException(
                new StandardResponse<>()
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setFormError(validationError)
            );
        }

        caller.setEmail(request.getEmail());
        caller.setTagName(request.getTagName());
        caller.setFullName(request.getFullName());

        Optional<User> maybeUser = userService.updateUser(caller);

        if (maybeUser.isEmpty()) {
            throw new UnauthorizedHttpException();
        }
        User updatedUser = maybeUser.get();

        return new StandardResponse<UserUpdateProfileResponse>()
            .setStatus(HttpStatus.OK)
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
    public StandardResponse<UserUpdateProfileResponse> updatePassword(
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

            throw new RequestHttpException(
                new StandardResponse<>()
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setFormError(validationError)
            );
        }

        String encodedPassword = encoder.encode(request.getNewPassword());
        caller.setPassword(encodedPassword);
        User updatedUser = userService
            .updateUser(caller)
            .orElseThrow(UnauthorizedHttpException::new);

        return new StandardResponse<UserUpdateProfileResponse>()
            .setStatus(HttpStatus.OK)
            .setData(
                new UserUpdateProfileResponse()
                    .setId(updatedUser.getId().toString())
                    .setEmail(updatedUser.getEmail())
                    .setTagName(updatedUser.getTagName())
                    .setFullName(updatedUser.getFullName())
            );
    }
}
