package com.mito.sectask.exceptions.httpexceptions;

import com.mito.sectask.values.MESSAGES;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public final class UnauthorizedHttpException extends HttpStatusCodeException {
    public UnauthorizedHttpException() {
        super(HttpStatus.UNAUTHORIZED, MESSAGES.ERROR_UNAUTHORIZED);
    }
}
