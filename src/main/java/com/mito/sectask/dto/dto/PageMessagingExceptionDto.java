package com.mito.sectask.dto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class PageMessagingExceptionDto {

    private Integer status;
    private String userId;
    private String pageId;
    private String message;
    private String redirectUrl;
}
