package com.mito.sectask.dto.request.user;

import com.mito.sectask.values.PATTERN;
import com.mito.sectask.values.VALIDATION;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

/**
 * @endpoint {@link com.mito.sectask.controllers.UserController#updatePassword()() updatePassword }
 */
@Value
public class UserUpdatePasswordRequest {

    @NotBlank(message = VALIDATION.REQUIRED)
    @Size(
        min = 0,
        max = 100,
        message = VALIDATION.STRING_LENGTH + 0 + "," + 100
    )
    @Pattern(regexp = PATTERN.ALPHANUM, message = VALIDATION.STRING_ALPHANUM)
    private String oldPassword;

    @NotBlank(message = VALIDATION.REQUIRED)
    @Size(
        min = 0,
        max = 100,
        message = VALIDATION.STRING_LENGTH + 0 + "," + 100
    )
    @Pattern(regexp = PATTERN.ALPHANUM, message = VALIDATION.STRING_ALPHANUM)
    private String newPassword;
}
