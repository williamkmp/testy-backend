package com.mito.sectask.dto.dto;

import com.mito.sectask.values.USER_ROLE;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class MemberDto {

    private String id;
    private String email;
    private String tagName;
    private String fullName;
    private String imageId;
    private USER_ROLE authority;
}
