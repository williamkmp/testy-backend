package com.mito.sectask.exceptions.messsagingexceptions;

import org.springframework.http.HttpStatus;
import com.mito.sectask.values.MESSAGES;

public class UnauthorizedPageAccessMessagingException extends PageMessagingException{

    public UnauthorizedPageAccessMessagingException(Long userId, Long pageId, String sessionId) {
        super(
            userId,
            pageId,
            sessionId,
            HttpStatus.FORBIDDEN, 
            MESSAGES.ERROR_FORBIDDEN
        );
    }
    
}
