package com.mito.sectask.dto.request.page;

import java.util.List;
import com.mito.sectask.dto.dto.InviteDto;
import lombok.Value;

@Value
public class PageCreateRequest {

    private String parentId;

    private String title;
    
    private List<InviteDto> members; 
    
    private String iconKey;
    
    private String imageId;
    
    private Float imagePosition;
}
