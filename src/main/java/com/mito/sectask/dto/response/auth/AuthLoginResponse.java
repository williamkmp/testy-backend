package com.mito.sectask.dto.response.auth;

import com.mito.sectask.dto.dto.TokenDto;
import io.micrometer.common.lang.NonNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AuthLoginResponse {

    @NonNull
    private String id;

    @NonNull
    private String email;

    @NonNull
    private String tagName;

    @NonNull
    private String fullName;

    private String imageSrc;

    @NonNull
    private TokenDto token;


}
