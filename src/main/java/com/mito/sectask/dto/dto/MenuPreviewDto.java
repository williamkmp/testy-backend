package com.mito.sectask.dto.dto;

import com.mito.sectask.values.MENU_PREVIEW;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class MenuPreviewDto {
    private MENU_PREVIEW type;
    private String id;
    private String iconKey;
    private String title;
}
