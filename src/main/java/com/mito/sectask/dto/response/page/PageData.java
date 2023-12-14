package com.mito.sectask.dto.response.page;

import com.mito.sectask.values.USER_ROLE;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PageData {
    private String id;
    private String title;
    private USER_ROLE authority;
    private String iconKey; 
    private String imageSrc;
    private Float imagePosition;
}
