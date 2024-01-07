package com.mito.sectask.dto.request.block;

import com.mito.sectask.values.BLOCK_TYPE;
import lombok.Value;

@Value
public class CreateBlockRequest {
    private Long pageId;
    private String id;
    private String previousId;
    private BLOCK_TYPE blockType;
    private String content;
}
