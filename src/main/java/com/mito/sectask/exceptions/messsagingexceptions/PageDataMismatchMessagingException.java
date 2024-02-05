package com.mito.sectask.exceptions.messsagingexceptions;

import com.mito.sectask.values.MESSAGES;
import org.springframework.http.HttpStatus;

/**
 * inidcates a mismatched page data, user must refresh the given page
 */
public class PageDataMismatchMessagingException extends PageMessagingException {

    public PageDataMismatchMessagingException(
        Long userId,
        Long pageId,
        String sessionId
    ) {
        super(
            userId,
            pageId,
            sessionId,
            HttpStatus.BAD_REQUEST,
            MESSAGES.UPDATE_FAIL
        );
    }
}
