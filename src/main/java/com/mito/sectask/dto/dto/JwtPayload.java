package com.mito.sectask.dto.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtPayload {
    private Long id;
    private String tagName;
    private String email;
}
