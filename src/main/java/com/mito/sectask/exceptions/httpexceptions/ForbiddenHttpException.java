package com.mito.sectask.exceptions.httpexceptions;

import com.mito.sectask.values.MESSAGES;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class ForbiddenHttpException extends HttpStatusCodeException {

    public ForbiddenHttpException() {
        super(HttpStatus.FORBIDDEN, MESSAGES.ERROR_FORBIDDEN);
    }
}
