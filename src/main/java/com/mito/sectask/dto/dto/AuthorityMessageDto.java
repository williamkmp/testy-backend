package com.mito.sectask.dto.dto;

import com.mito.sectask.values.ACTION;
import com.mito.sectask.values.USER_ROLE;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AuthorityMessageDto {

    private ACTION action;
    private String id;
    private USER_ROLE role;
}
