package com.mito.sectask.dto.response;

import io.micrometer.common.lang.NonNull;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class StandardResponse<T> {

    @NonNull private Integer status;

    private T data;
    private String message;
    private Map<String, String> error;
}
