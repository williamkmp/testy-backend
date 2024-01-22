package com.mito.sectask.dto.dto;

import com.mito.sectask.values.USER_ROLE;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class InviteDto {

    private String email;
    private USER_ROLE authority;
}
