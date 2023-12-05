package com.mito.sectask.exceptions.httpexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import com.mito.sectask.values.MESSAGES;

public final class UnauthorizedHttpException extends HttpStatusCodeException {
    public UnauthorizedHttpException() {
        super(HttpStatus.UNAUTHORIZED, MESSAGES.ERROR_UNAUTHORIZED);
    }
}
