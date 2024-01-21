package com.mito.sectask.dto.request.block;

import com.mito.sectask.values.BLOCK_ACTION;
import com.mito.sectask.values.BLOCK_TYPE;
import lombok.Value;

@Value
public class BlockMessageDto {
    private BLOCK_ACTION action;

    private Long pageId;
    private String blockId;
    private BLOCK_TYPE type;
    private String content;
    private String iconKey;
    private Float width;
    private String fileId;

    private String previousId;
    private String nextId;
}
