package com.mito.sectask.dto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class PageHeaderMessageDto {

    private String title;
    private String iconKey;
    private String imageId;
    private Float imagePosition;
}
