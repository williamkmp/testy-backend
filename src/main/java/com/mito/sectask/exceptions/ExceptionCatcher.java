package com.mito.sectask.exceptions;

import com.mito.sectask.dto.dto.PageMessagingExceptionDto;
import com.mito.sectask.dto.response.Response;
import com.mito.sectask.exceptions.messsagingexceptions.PageMessagingException;
import com.mito.sectask.values.DESTINATION;
import com.mito.sectask.values.KEY;
import com.mito.sectask.values.MESSAGES;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionCatcher {

    private final SimpMessagingTemplate socket;

    @ExceptionHandler(Exception.class)
    public Response<Object> internalServerException(Exception exception) {
        log.error("Unhandled exception occured", exception);
        return new Response<Object>(HttpStatus.INTERNAL_SERVER_ERROR)
            .setMessage(MESSAGES.ERROR_INTERNAL_SERVER);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Response<Object> maxUploadSizeException(
        MaxUploadSizeExceededException exception
    ) {
        return new Response<Object>(HttpStatus.PAYLOAD_TOO_LARGE)
            .setMessage(MESSAGES.ERROR_PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public Response<Object> httpStatusCodeException(
        HttpStatusCodeException exception
    ) {
        return new Response<Object>(exception.getStatusCode())
            .setMessage(exception.getStatusText());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Object> validationException(
        MethodArgumentNotValidException exception
    ) {
        List<FieldError> errors = exception.getBindingResult().getFieldErrors();
        Map<String, String> failedValidations = new HashMap<>();
        for (FieldError error : errors) {
            String message = error.getDefaultMessage();
            String key = error.getField();
            if (failedValidations.containsKey(key)) continue;
            failedValidations.put(key, message);
        }

        return new Response<>(HttpStatus.BAD_REQUEST)
            .setError(failedValidations);
    }

    @MessageExceptionHandler(PageMessagingException.class)
    public void pageMessagingException(PageMessagingException exception) {
        socket.convertAndSend(
            DESTINATION.pageUserError(
                exception.getPageId(),
                exception.getUserId()
            ),
            new PageMessagingExceptionDto()
                .setStatus(exception.getCode().value())
                .setPageId(exception.getPageId().toString())
                .setUserId(exception.getUserId().toString())
                .setMessage(exception.getMessage()),
            Map.ofEntries(
                Map.entry(KEY.SENDER_USER_ID, exception.getUserId().toString()),
                Map.entry(
                    KEY.SENDER_SESSION_ID,
                    exception.getPageId().toString()
                )
            )
        );
    }
}
