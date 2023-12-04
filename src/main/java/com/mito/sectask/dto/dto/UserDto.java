package com.mito.sectask.dto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserDto {

    private String id;
    private String fullName;
    private String tagName;
    private String email;
    private String imageSrc;
}
