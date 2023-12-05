package com.mito.sectask.dto.request.user;

import com.google.gson.annotations.SerializedName;
import com.mito.sectask.values.PATTERN;
import com.mito.sectask.values.VALIDATION;
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

    @NotBlank(message = VALIDATION.REQUIRED)
    @Size(
        min = 0,
        max = 100,
        message = VALIDATION.STRING_LENGTH + 0 + "," + 100
    )
    @Email(message = VALIDATION.STRING_EMAIL)
    @SerializedName("email")
    private String email;

    @NotBlank(message = VALIDATION.REQUIRED)
    @Pattern(
        regexp = PATTERN.TAGNAME,
        message = VALIDATION.STRING_ALPHANUM
    )
    @Size(
        min = 0,
        max = 30,
        message = VALIDATION.STRING_LENGTH + 0 + "," + 30
    )
    @SerializedName("tagName")
    private String tagName;

    @NotBlank(message = VALIDATION.REQUIRED)
    @Size(
        min = 0,
        max = 100,
        message = VALIDATION.STRING_LENGTH + 0 + "," + 100
    )
    @SerializedName("fullName")
    private String fullName;

    @SerializedName("imageSrc")
    private String imageSrc;
}
