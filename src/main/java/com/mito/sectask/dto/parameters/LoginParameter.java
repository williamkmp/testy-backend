package com.mito.sectask.dto.parameters;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@NoArgsConstructor
/** 
 * @see {@link com.mito.sectask.services.auth.AuthService#loginUser(LoginParameter) loginUser }
 */
public class LoginParameter {
    
    @NonNull
    private String email;

    @NonNull
    private String password;
}
