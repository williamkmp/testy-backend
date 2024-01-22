package com.mito.sectask.dto.dto;

import com.mito.sectask.values.USER_ROLE;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PageDto {

    private String id;
    private String title;
    private USER_ROLE authority;
    private String iconKey;
    private String imageId;
    private Float imagePosition;
}
