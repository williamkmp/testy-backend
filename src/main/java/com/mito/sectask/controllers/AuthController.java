package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.dto.dto.JwtPayload;
import com.mito.sectask.dto.dto.TokenDto;
import com.mito.sectask.dto.parameters.LoginParameter;
import com.mito.sectask.dto.parameters.RegisterUserParameter;
import com.mito.sectask.dto.request.auth.AuthLoginRequest;
import com.mito.sectask.dto.request.auth.AuthRefreshTokenRequest;
import com.mito.sectask.dto.request.auth.AuthRegisterRequest;
import com.mito.sectask.dto.response.StandardResponse;
import com.mito.sectask.dto.response.auth.AuthLoginResponse;
import com.mito.sectask.dto.response.auth.AuthRefreshToken;
import com.mito.sectask.dto.response.auth.AuthRegisterResponse;
import com.mito.sectask.entities.UserEntity;
import com.mito.sectask.exceptions.httpexceptions.RequestHttpException;
import com.mito.sectask.exceptions.httpexceptions.UnauthorizedHttpException;
import com.mito.sectask.services.auth.AuthService;
import com.mito.sectask.services.user.UserService;
import com.mito.sectask.values.Message;
import com.mito.sectask.values.ValidationMessage;
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
    public StandardResponse<AuthLoginResponse> login(
        @RequestBody @Valid AuthLoginRequest request
    ) {
        Optional<AuthLoginResponse> maybeResponse = authService.loginUser(
            new LoginParameter()
                .setEmail(request.getEmail())
                .setPassword(request.getPassword())
        );

        if (maybeResponse.isEmpty()) {
            throw new RequestHttpException(
                ValidationMessage.INVALID_CREDENTIAL
            );
        }
        return new StandardResponse<AuthLoginResponse>()
            .setStatus(HttpStatus.OK)
            .setData(maybeResponse.get());
    }

    @PostMapping(
        path = "/register",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public StandardResponse<AuthRegisterResponse> register(
        @RequestBody @Valid AuthRegisterRequest request
    ) {
        Boolean isEmailAvailable = userService.checkEmailIsTaken(
            request.getEmail()
        );
        Boolean isUserNammeAvailable = userService.checkUsernameIsTaken(
            request.getEmail()
        );

        //email or tagname is note available
        if (
            Boolean.FALSE.equals(isEmailAvailable) ||
            Boolean.FALSE.equals(isUserNammeAvailable)
        ) {
            Map<String, String> validationError = new HashMap<>();
            validationError.put(
                "email",
                Boolean.FALSE.equals(isEmailAvailable)
                    ? ValidationMessage.UNIQUE
                    : null
            );
            validationError.put(
                "tagName",
                Boolean.FALSE.equals(isEmailAvailable)
                    ? ValidationMessage.UNIQUE
                    : null
            );

            throw new RequestHttpException(
                new StandardResponse<AuthRegisterResponse>()
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setFormError(validationError)
            );
        }

        Optional<UserEntity> maybeUser =
            this.userService.registerUser(
                    new RegisterUserParameter()
                        .setEmail(request.getEmail())
                        .setTagName(request.getTagName())
                        .setFullName(request.getFullName())
                        .setPassword(request.getPassword())
                );

        if (maybeUser.isEmpty()) {
            throw new RequestHttpException(Message.ERROR_INTERNAL_SERVER);
        }

        UserEntity createdUser = maybeUser.get();

        return new StandardResponse<AuthRegisterResponse>()
            .setStatus(HttpStatus.BAD_REQUEST)
            .setData(
                new AuthRegisterResponse()
                    .setId(createdUser.getId().toString())
                    .setEmail(createdUser.getEmail())
                    .setTagName(createdUser.getTagName())
                    .setFullName(createdUser.getFullName())
            );
    }

    @PostMapping(
        path = "/token/refresh",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public StandardResponse<AuthRefreshToken> refresh(
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

        TokenDto tokens = maybeTokens.get();

        return new StandardResponse<AuthRefreshToken>()
            .setStatus(HttpStatus.OK)
            .setData(
                new AuthRefreshToken()
                    .setAccessToken(tokens.getAccessToken())
                    .setRefreshToken(tokens.getAccessToken())
            );
    }
}
