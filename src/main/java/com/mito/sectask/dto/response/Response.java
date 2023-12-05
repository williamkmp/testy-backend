package com.mito.sectask.dto.response;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import com.mito.sectask.values.KEY;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Response<T> extends ResponseEntity<StandardResponse<T>> {

    private final StandardResponse<T> responseBody;

    public Response(HttpStatusCode status) {
        super(new StandardResponse<>(), status);
        this.responseBody = super.getBody();
        this.responseBody.setStatus(status.value());
    }

    public Response<T> setData(T data) {
        this.responseBody.setData(data);
        return this;
    }

    public Response<T> setMessage(String message) {
        this.responseBody.setMessage(message);
        return this;
    }

    public Response<T> setError(Map<String, String> errorMap) {
        this.responseBody.setError(errorMap);
        return this;
    }

    public Response<T> addError(String path, String errorCode) {
        if (this.responseBody.getError() == null) {
            this.responseBody.setError(new HashMap<>());
        }
        this.responseBody.getError().put(path, errorCode);
        return this;
    }

    public Response<T> setRootError(String errorCode) {
        if (this.responseBody.getError() == null) {
            this.responseBody.setError(new HashMap<>());
        }
        this.responseBody.getError()
            .put(KEY.RESPONSE_ROOT_FORM_ERROR, errorCode);
        return this;
    }
    
}
