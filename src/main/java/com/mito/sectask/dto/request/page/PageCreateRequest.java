package com.mito.sectask.dto.request.page;

import java.util.List;
import com.mito.sectask.values.VALIDATION;
import com.mito.sectask.dto.dto.InviteDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class PageCreateRequest {

    private String parentId;

    @NotBlank(message = VALIDATION.REQUIRED)
    @Size(
        min = 1,
        max = 100,
        message = VALIDATION.STRING_LENGTH + 1 + "," + 100
    )
    private String title;
    
    private List<InviteDto> members; 
    
    private String iconKey;
    
    private String imageId;
    
    private Float imagePosition;
}
