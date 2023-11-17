package com.mito.sectask.dto.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TokenDto {
    private String refreshToken;
    private String accessToken;    
}
