package com.mito.sectask.dto.dto;

import com.mito.sectask.values.PREVIEW_ACTION;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PreviewMessageDto {
    private PREVIEW_ACTION action;

    private String parentId;
    private String id;
    private String name;
    private String iconKey;
}
