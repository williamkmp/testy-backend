package com.mito.sectask.dto.request.page;

import com.mito.sectask.values.VALIDATION;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class PageCreateRequest {

    @NotBlank(message = VALIDATION.REQUIRED)
    @Size(
        min = 1,
        max = 100,
        message = VALIDATION.STRING_LENGTH + 1 + "," + 100
    )
    private String title;
    
    private String imageId;
    
    private Float imagePosition;
    
    private String parentId;

    private String iconKey;

    private String role;
}
