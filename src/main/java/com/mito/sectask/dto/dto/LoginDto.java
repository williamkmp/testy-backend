package com.mito.sectask.dto.dto;

import io.micrometer.common.lang.NonNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class LoginDto {

    @NonNull
    private String id;

    @NonNull
    private String email;

    @NonNull
    private String tagName;

    @NonNull
    private String fullName;

    private String imageId;

    @NonNull
    private TokenDto token;


}
