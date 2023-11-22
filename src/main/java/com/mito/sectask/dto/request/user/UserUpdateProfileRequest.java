package com.mito.sectask.dto.request.user;

import com.google.gson.annotations.SerializedName;
import com.mito.sectask.values.Regex;
import com.mito.sectask.values.ValidationMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

/**
 * @endpoint {@link com.mito.sectask.controllers.UserController#updateProfile() updateProfile }
 */
@Value
public class UserUpdateProfileRequest {

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
}
