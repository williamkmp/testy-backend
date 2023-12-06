package com.mito.sectask.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.dto.dto.JwtPayload;
import com.mito.sectask.dto.dto.TokenDto;
import com.mito.sectask.dto.parameters.LoginParameter;
import com.mito.sectask.dto.parameters.RegisterUserParameter;
import com.mito.sectask.dto.request.auth.AuthLoginRequest;
import com.mito.sectask.dto.request.auth.AuthRefreshTokenRequest;
import com.mito.sectask.dto.request.auth.AuthRegisterRequest;
import com.mito.sectask.dto.response.Response;
import com.mito.sectask.dto.response.auth.AuthLoginResponse;
import com.mito.sectask.dto.response.auth.AuthRegisterResponse;
import com.mito.sectask.entities.User;
import com.mito.sectask.exceptions.httpexceptions.UnauthorizedHttpException;
import com.mito.sectask.services.auth.AuthService;
import com.mito.sectask.services.user.UserService;
import com.mito.sectask.values.MESSAGES;
import com.mito.sectask.values.VALIDATION;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Authenticated(false)
@RequestMapping(path = "/auth")
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    @PostMapping(
        path = "/login",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<AuthLoginResponse> login(
        @RequestBody @Valid AuthLoginRequest request
    ) {
        Optional<AuthLoginResponse> maybeResponse = authService.loginUser(
            new LoginParameter()
                .setEmail(request.getEmail())
                .setPassword(request.getPassword())
        );

        if (maybeResponse.isEmpty()) {
            return new Response<AuthLoginResponse>(HttpStatus.BAD_REQUEST)
                .setRootError(VALIDATION.INVALID_CREDENTIAL);
        }
        return new Response<AuthLoginResponse>(HttpStatus.OK)
            .setData(maybeResponse.get());
    }

    @PostMapping(
        path = "/register",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<AuthRegisterResponse> register(
        @RequestBody @Valid AuthRegisterRequest request
    ) {
        boolean isEmailAvailable = Boolean.TRUE.equals(
            userService.checkEmailIsAvailable(request.getEmail())
        );
        boolean isTagNameAvailable = Boolean.TRUE.equals(
            userService.checkTagNameIsAvailable(request.getTagName())
        );

        //email or tagname is note available
        if (!isEmailAvailable || !isTagNameAvailable) {
            Map<String, String> validationError = new HashMap<>();
            validationError.put(
                "email",
                !isEmailAvailable ? VALIDATION.UNIQUE : null
            );
            validationError.put(
                "tagName",
                !isTagNameAvailable ? VALIDATION.UNIQUE : null
            );

            return new Response<AuthRegisterResponse>(HttpStatus.BAD_REQUEST)
                .setError(validationError);
        }

        Optional<User> maybeUser =
            this.userService.registerUser(
                    new RegisterUserParameter()
                        .setEmail(request.getEmail())
                        .setTagName(request.getTagName())
                        .setFullName(request.getFullName())
                        .setPassword(request.getPassword())
                );

        if (maybeUser.isEmpty()) {
            return new Response<AuthRegisterResponse>(
                HttpStatus.INTERNAL_SERVER_ERROR
            )
                .setMessage(MESSAGES.ERROR_INTERNAL_SERVER);
        }

        User createdUser = maybeUser.get();

        return new Response<AuthRegisterResponse>(HttpStatus.CREATED)
            .setData(
                new AuthRegisterResponse()
                    .setId(createdUser.getId().toString())
                    .setEmail(createdUser.getEmail())
                    .setTagName(createdUser.getTagName())
                    .setFullName(createdUser.getFullName())
            );
    }

    @PostMapping(
        path = "/token",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<TokenDto> refresh(
        @RequestBody @Valid AuthRefreshTokenRequest request
    ) {
        String refreshToken = request.getRefreshToken();
        Optional<JwtPayload> maybeCaller = authService.verifyRefreshToken(
            refreshToken
        );

        if (maybeCaller.isEmpty()) {
            throw new UnauthorizedHttpException();
        }

        JwtPayload caller = maybeCaller.get();

        Optional<TokenDto> maybeTokens = authService.generateTokens(
            caller.getId()
        );

        if (maybeTokens.isEmpty()) {
            throw new UnauthorizedHttpException();
        }

        return new Response<TokenDto>(HttpStatus.OK)
            .setData(
                maybeTokens.get()
            );
    }
}
