package com.mito.sectask.dto.request.auth;

import com.mito.sectask.values.PATTERN;
import com.mito.sectask.values.VALIDATION;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

/**
 * @endpoint {@link com.mito.sectask.controllers.AuthController#login(LoginRequest) login }
 */
@Value
public class AuthLoginRequest {

    @NotBlank(message = VALIDATION.REQUIRED)
    @Size(
        min = 0,
        max = 100,
        message = VALIDATION.STRING_LENGTH + 0 + "," + 100
    )
    @Email(message = VALIDATION.STRING_EMAIL)
    private String email;

    @NotBlank(message = VALIDATION.REQUIRED)
    @Size(
        min = 0,
        max = 100,
        message = VALIDATION.STRING_LENGTH + 0 + "," + 100
    )
    @Pattern(
        regexp = PATTERN.ALPHANUM,
        message = VALIDATION.STRING_ALPHANUM
    )
    private String password;
}
