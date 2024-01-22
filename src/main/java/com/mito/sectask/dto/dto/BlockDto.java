package com.mito.sectask.dto.dto;

import com.mito.sectask.values.BLOCK_TYPE;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class BlockDto {

    private String id;
    private BLOCK_TYPE type;
    private String content;
    private String iconKey;
    private Float width;
    private String fileId;
}
