package com.mito.sectask.dto.response.page;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PageData {
    private String id;
    private String title;
    private String imageSrc;
    private Float imagePosition; 
}
