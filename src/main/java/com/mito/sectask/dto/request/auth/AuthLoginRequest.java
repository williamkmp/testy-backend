package com.mito.sectask.dto.request.auth;

import com.mito.sectask.values.Regex;
import com.mito.sectask.values.ValidationMessage;
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

    @NotBlank(message = ValidationMessage.REQUIRED)
    @Size(
        min = 0,
        max = 100,
        message = ValidationMessage.STRING_LENGTH + 0 + "," + 100
    )
    @Email(message = ValidationMessage.STRING_EMAIL)
    private String email;

    @NotBlank(message = ValidationMessage.REQUIRED)
    @Size(
        min = 0,
        max = 100,
        message = ValidationMessage.STRING_LENGTH + 0 + "," + 100
    )
    @Pattern(
        regexp = Regex.ALPHANUM,
        message = ValidationMessage.STRING_ALPHANUM
    )
    private String password;
}
