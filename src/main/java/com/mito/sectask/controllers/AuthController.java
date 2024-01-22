package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.dto.dto.JwtPayload;
import com.mito.sectask.dto.dto.LoginDto;
import com.mito.sectask.dto.dto.RegisterDto;
import com.mito.sectask.dto.dto.TokenDto;
import com.mito.sectask.dto.parameters.LoginParameter;
import com.mito.sectask.dto.parameters.RegisterUserParameter;
import com.mito.sectask.dto.request.auth.AuthLoginRequest;
import com.mito.sectask.dto.request.auth.AuthRefreshTokenRequest;
import com.mito.sectask.dto.request.auth.AuthRegisterRequest;
import com.mito.sectask.dto.response.Response;
import com.mito.sectask.entities.User;
import com.mito.sectask.exceptions.httpexceptions.UnauthorizedHttpException;
import com.mito.sectask.services.auth.AuthService;
import com.mito.sectask.services.user.UserService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Transactional
    public Response<LoginDto> login(
        @RequestBody @Valid AuthLoginRequest request
    ) {
        Optional<User> maybeUser = authService.loginUser(
            new LoginParameter()
                .setEmail(request.getEmail())
                .setPassword(request.getPassword())
        );

        if (maybeUser.isEmpty()) {
            return new Response<LoginDto>(HttpStatus.BAD_REQUEST)
                .setRootError(VALIDATION.INVALID_CREDENTIAL);
        }

        User registeredUser = maybeUser.get();
        Optional<TokenDto> maybeToken = authService.generateTokens(
            registeredUser.getId()
        );

        if (maybeToken.isEmpty()) {
            return new Response<LoginDto>(HttpStatus.INTERNAL_SERVER_ERROR)
                .setMessage(MESSAGES.ERROR_INTERNAL_SERVER);
        }

        String imageId = (registeredUser.getImage() != null)
            ? registeredUser.getImage().getId().toString()
            : null;

        TokenDto token = maybeToken.get();
        return new Response<LoginDto>(HttpStatus.OK)
            .setData(
                new LoginDto()
                    .setId(registeredUser.getId().toString())
                    .setEmail(registeredUser.getEmail())
                    .setTagName(registeredUser.getTagName())
                    .setFullName(registeredUser.getFullName())
                    .setImageId(imageId)
                    .setToken(token)
            );
    }

    @PostMapping(
        path = "/register",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<RegisterDto> register(
        @RequestBody @Valid AuthRegisterRequest request
    ) {
        boolean isEmailAvailable = Boolean.TRUE.equals(
            userService.checkEmailIsAvailable(request.getEmail())
        );
        boolean isTagNameAvailable = Boolean.TRUE.equals(
            userService.checkTagNameIsAvailable(request.getTagName())
        );

        // email or tagname is note available
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

            return new Response<RegisterDto>(HttpStatus.BAD_REQUEST)
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
            return new Response<RegisterDto>(HttpStatus.INTERNAL_SERVER_ERROR)
                .setMessage(MESSAGES.ERROR_INTERNAL_SERVER);
        }

        User createdUser = maybeUser.get();

        return new Response<RegisterDto>(HttpStatus.CREATED)
            .setData(
                new RegisterDto()
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

        return new Response<TokenDto>(HttpStatus.OK).setData(maybeTokens.get());
    }
}
