package com.mito.sectask.dto.response.user;

import io.micrometer.common.lang.NonNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @endpoint {@link com.mito.sectask.controllers.UserController#updateProfile() updateProfile}
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserUpdateProfileResponse {

    @NonNull
    private String id;

    @NonNull
    private String email;

    @NonNull
    private String tagName;

    @NonNull
    private String fullName;

    private String imageSrc;
}
