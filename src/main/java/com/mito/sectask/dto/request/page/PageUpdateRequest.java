package com.mito.sectask.dto.request.page;

import lombok.Value;

@Value
public class PageUpdateRequest {
    
    private String iconKey;
    
    private String imageId;
    
    private Float imagePosition;
}
