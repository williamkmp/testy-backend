package com.mito.sectask.exceptions.messsagingexceptions;

import com.mito.sectask.values.MESSAGES;
import org.springframework.http.HttpStatus;

public class UnauthorizedPageAccessMessagingException
    extends PageMessagingException {

    public UnauthorizedPageAccessMessagingException(
        Long userId,
        Long pageId,
        String sessionId
    ) {
        super(
            userId,
            pageId,
            sessionId,
            HttpStatus.FORBIDDEN,
            MESSAGES.ERROR_FORBIDDEN
        );
    }
}
