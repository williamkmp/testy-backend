package com.mito.sectask.dto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class FilePreviewDto {

    private String id;
    private String name;
    private String url;
    private Long size;
    private String extension;
}
