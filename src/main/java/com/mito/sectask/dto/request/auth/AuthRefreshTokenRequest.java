package com.mito.sectask.dto.request.auth;

import com.mito.sectask.values.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

/**
 * @endpoint {@link com.mito.sectask.controllers.AuthController#refresh() AuthController.refreshToken}
 */
@Value
public class AuthRefreshTokenRequest {

    @NotBlank(message = ValidationMessage.REQUIRED)
    private String refreshToken;
}
