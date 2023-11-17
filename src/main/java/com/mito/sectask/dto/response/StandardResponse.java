package com.mito.sectask.dto.response;

import java.util.Map;
import org.springframework.http.HttpStatus;
import io.micrometer.common.lang.NonNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class StandardResponse<T> {

    @NonNull
    private Integer status;

    private T data;
    private String error;
    private Map<String, String> formError;

    public StandardResponse<T> setStatus(HttpStatus status) {
        this.status = status.value();
        return this;
    }
}
