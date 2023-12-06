package com.mito.sectask.dto.response.image;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ImageUploadResponse {

    private String src;
}
