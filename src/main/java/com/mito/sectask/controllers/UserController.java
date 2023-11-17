package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.annotations.caller.Caller;
import com.mito.sectask.dto.request.user.UserUpdateProfileRequest;
import com.mito.sectask.dto.response.StandardResponse;
import com.mito.sectask.dto.response.user.UserMeResponse;
import com.mito.sectask.dto.response.user.UserUpdateProfileResponse;
import com.mito.sectask.entities.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping(path = "/me")
    @Authenticated(true)
    public StandardResponse<UserMeResponse> me(@Caller UserEntity caller) {
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

    @PutMapping(path = "/me")
    @Authenticated(true)
    public StandardResponse<UserUpdateProfileResponse> updateProfile(
        @Valid @RequestBody UserUpdateProfileRequest request,
        @Caller UserEntity caller
    ) {
        return new StandardResponse<>();
    }
}
