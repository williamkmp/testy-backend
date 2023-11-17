package com.mito.sectask.exceptions.httpexceptions;

import com.mito.sectask.dto.response.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

public class RequestHttpException extends HttpStatusCodeException {

    private final StandardResponse<?> responseBody;

    public RequestHttpException(final StandardResponse<?> response) {
        super(HttpStatusCode.valueOf(response.getStatus()));
        this.responseBody = response;
    }

    public RequestHttpException(
        final HttpStatus responseStatusCode,
        final String errorMessage
    ) {
        super(responseStatusCode);
        this.responseBody =
            new StandardResponse<Object>()
                .setStatus(responseStatusCode)
                .setError(errorMessage);
    }

    public RequestHttpException(final String errorMessage) {
        super(HttpStatus.BAD_REQUEST);
        this.responseBody =
            new StandardResponse<Object>()
                .setStatus(HttpStatus.BAD_REQUEST)
                .setError(errorMessage);
    }

    public StandardResponse<?> getResponseBody() {
        return this.responseBody;
    }
}
