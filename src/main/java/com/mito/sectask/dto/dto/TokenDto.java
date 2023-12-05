package com.mito.sectask.dto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TokenDto {
    private String refreshToken;
    private String accessToken;
}
