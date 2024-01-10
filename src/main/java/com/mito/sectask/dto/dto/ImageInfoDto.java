package com.mito.sectask.dto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ImageInfoDto {

    private String id;
    private String src;
}
