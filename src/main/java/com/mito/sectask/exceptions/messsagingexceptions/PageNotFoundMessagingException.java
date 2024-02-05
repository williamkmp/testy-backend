package com.mito.sectask.exceptions.messsagingexceptions;

import com.mito.sectask.values.MESSAGES;
import org.springframework.http.HttpStatus;

/**
 * indicates a page is deleted, user must be redirected to index page
 */
public class PageNotFoundMessagingException extends PageMessagingException {

    public PageNotFoundMessagingException(
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
