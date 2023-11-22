package com.mito.sectask.dto.request.auth;

import com.google.gson.annotations.SerializedName;
import com.mito.sectask.values.Regex;
import com.mito.sectask.values.ValidationMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

/**
 * @endpoint {@link com.mito.sectask.controllers.AuthController#register() register }
 */
@Value
public class AuthRegisterRequest {

    @NotBlank(message = ValidationMessage.REQUIRED)
    @Size(
        min = 0,
        max = 100,
        message = ValidationMessage.STRING_LENGTH + 0 + "," + 100
    )
    @Email(message = ValidationMessage.STRING_EMAIL)
    @SerializedName("email")
    private String email;

    @NotBlank(message = ValidationMessage.REQUIRED)
    @Pattern(
        regexp = Regex.TAGNAME,
        message = ValidationMessage.STRING_ALPHANUM
    )
    @Size(
        min = 0,
        max = 30,
        message = ValidationMessage.STRING_LENGTH + 0 + "," + 30
    )
    @SerializedName("tagName")
    private String tagName;

    @NotBlank(message = ValidationMessage.REQUIRED)
    @Size(
        min = 0,
        max = 100,
        message = ValidationMessage.STRING_LENGTH + 0 + "," + 100
    )
    @SerializedName("fullName")
    private String fullName;

    @NotBlank(message = ValidationMessage.REQUIRED)
    @Size(
        min = 5,
        max = 100,
        message = ValidationMessage.STRING_LENGTH + 5 + "," + 100
    )
    @Pattern(
        regexp = Regex.ALPHANUM,
        message = ValidationMessage.STRING_ALPHANUM
    )
    @SerializedName("password")
    private String password;
}
