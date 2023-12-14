package com.mito.sectask.exceptions.httpexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import com.mito.sectask.values.MESSAGES;

public class InternalServerErrorHttpException extends HttpStatusCodeException{
    public InternalServerErrorHttpException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGES.ERROR_INTERNAL_SERVER);
    }
}
