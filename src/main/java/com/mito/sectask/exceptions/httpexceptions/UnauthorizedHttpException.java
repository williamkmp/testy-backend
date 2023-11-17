package com.mito.sectask.exceptions.httpexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import com.mito.sectask.values.Message;

public class UnauthorizedHttpException extends HttpStatusCodeException {
    public UnauthorizedHttpException() {
        super(HttpStatus.UNAUTHORIZED, Message.ERROR_UNAUTHORIZED);
    }

    public UnauthorizedHttpException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
