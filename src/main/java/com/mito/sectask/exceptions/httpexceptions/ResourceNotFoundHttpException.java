package com.mito.sectask.exceptions.httpexceptions;

import com.mito.sectask.values.MESSAGES;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public final class ResourceNotFoundHttpException
    extends HttpStatusCodeException {

    public ResourceNotFoundHttpException() {
        super(HttpStatus.BAD_REQUEST, MESSAGES.ERROR_RESOURCE_NOT_FOUND);
    }
}
