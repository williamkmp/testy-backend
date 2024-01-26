package com.mito.sectask.exceptions.messsagingexceptions;

import org.springframework.http.HttpStatusCode;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PageMessagingException extends Exception{
    private final Long userId;
    private final String sessionId;
    private final Long pageId;
    private final String message;
    private final HttpStatusCode code;

    public PageMessagingException(Long userId, Long pageId, String sessionId, HttpStatusCode code, String message) {
        this.userId = userId;
        this.pageId = pageId;
        this.sessionId = sessionId;
        this.code = code;
        this.message = message;
    }
}
