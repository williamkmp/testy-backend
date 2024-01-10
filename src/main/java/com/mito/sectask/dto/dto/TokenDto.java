package com.mito.sectask.dto.dto;

import io.micrometer.common.lang.NonNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TokenDto {

    @NonNull
    private String accessToken;
    
    @NonNull
    private String refreshToken;
}
