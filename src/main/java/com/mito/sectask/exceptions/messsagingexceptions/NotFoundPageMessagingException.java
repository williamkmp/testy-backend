package com.mito.sectask.exceptions.messsagingexceptions;

import com.mito.sectask.values.MESSAGES;
import org.springframework.http.HttpStatus;

public class NotFoundPageMessagingException extends PageMessagingException {

    public NotFoundPageMessagingException(
        Long userId,
        Long pageId,
        String sessionId
    ) {
        super(
            userId,
            pageId,
            sessionId,
            HttpStatus.NOT_FOUND,
            MESSAGES.ERROR_RESOURCE_NOT_FOUND
        );
    }
}
