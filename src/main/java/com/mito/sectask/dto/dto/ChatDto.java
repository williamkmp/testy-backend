package com.mito.sectask.dto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ChatDto {

    private String id;
    private String content;
    private String senderId;
    private String pageId;
}
