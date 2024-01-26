package com.mito.sectask.dto.parameters;

import com.mito.sectask.values.BLOCK_TYPE;

import io.micrometer.common.lang.NonNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
/**
 * @see {@link com.mito.sectask.services.block.BlockService#createBlock }
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class CreateBlockParameter {
    @NonNull
    private BLOCK_TYPE blockType;
    
    private String content;

    private String iconKey;

    private Float width;

    private Long pageId;

    private Long fileId;

    private String prevId;

    private String nextId;


    
}
