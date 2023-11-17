package com.mito.sectask.dto.response.auth;

import io.micrometer.common.lang.NonNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AuthRegisterResponse {
    @NonNull
    private String id;
    
    @NonNull
    private String email;
    
    @NonNull
    private String tagName;

    @NonNull
    private String fullName;
}
