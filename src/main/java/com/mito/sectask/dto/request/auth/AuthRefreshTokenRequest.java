package com.mito.sectask.dto.request.auth;

import com.mito.sectask.values.VALIDATION;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

/**
 * @endpoint {@link com.mito.sectask.controllers.AuthController#refresh()
 *     AuthController.refreshToken}
 */
@Value
public class AuthRefreshTokenRequest {

    @NotBlank(message = VALIDATION.REQUIRED)
    private String refreshToken;
}
