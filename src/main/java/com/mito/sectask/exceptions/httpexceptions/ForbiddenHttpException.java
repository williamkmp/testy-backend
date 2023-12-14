package com.mito.sectask.exceptions.httpexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import com.mito.sectask.values.MESSAGES;

public class ForbiddenHttpException extends HttpStatusCodeException{

    public ForbiddenHttpException() {
        super(HttpStatus.FORBIDDEN, MESSAGES.ERROR_FORBIDDEN);
    }
}
