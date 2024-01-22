package com.mito.sectask.exceptions.httpexceptions;

import com.mito.sectask.values.MESSAGES;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class InternalServerErrorHttpException extends HttpStatusCodeException {

    public InternalServerErrorHttpException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGES.ERROR_INTERNAL_SERVER);
    }
}
