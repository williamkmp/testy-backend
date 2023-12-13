package com.mito.sectask.dto.response.page;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PagePreview {
    private String id;
    private String title;
}
