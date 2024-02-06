package com.mito.sectask.dto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class PageDeleteDto {
    private String pageId;
    private String redirectPageId;
}
