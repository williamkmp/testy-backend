package com.mito.sectask.dto.parameters;

import io.micrometer.common.lang.NonNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/** 
 * @see {@link com.mito.sectask.services.auth.AuthService#registerUser }
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class RegisterUserParameter {

    @NonNull
    private String email;

    @NonNull
    private String password;

    @NonNull
    private String tagName;

    @NonNull
    private String fullName;
}
