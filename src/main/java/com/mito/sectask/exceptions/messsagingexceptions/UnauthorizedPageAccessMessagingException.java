package com.mito.sectask.exceptions.messsagingexceptions;

import com.mito.sectask.values.MESSAGES;
import org.springframework.http.HttpStatus;

/**
 * inidcates a user doesn't have authorization to access a given page, user must be redirected to index page
 */
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
            HttpStatus.UNAUTHORIZED,
            MESSAGES.ERROR_FORBIDDEN
        );
    }
}
