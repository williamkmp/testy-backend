package com.mito.sectask.dto.dto;

import com.mito.sectask.values.BLOCK_TYPE;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class BlockMessageDto {

    private String id;
    private BLOCK_TYPE type;
    private String content;
    private String iconKey;
    private Float width;
    private String fileId;

    private String previousId;
    private String nextId;
}
