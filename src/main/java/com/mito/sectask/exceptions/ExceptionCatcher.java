package com.mito.sectask.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import com.mito.sectask.dto.response.StandardResponse;
import com.mito.sectask.exceptions.httpexceptions.RequestHttpException;
import com.mito.sectask.exceptions.httpexceptions.UnauthorizedHttpException;
import com.mito.sectask.values.Message;

@ControllerAdvice
public class ExceptionCatcher {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse<Object>> internalServerException(
        Exception exception
    ) {
        log.error("Unhandled exception occured", exception);

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .body(
                new StandardResponse<>()
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .setError(Message.ERROR_INTERNAL_SERVER)
            );
    }

    @ExceptionHandler(UnauthorizedHttpException.class)
    public ResponseEntity<StandardResponse<Object>> unauthorizedException(
        UnauthorizedHttpException exception
    ) {
        String errorMessage = exception.getMessage() == null
            ? exception.getMessage()
            : Message.ERROR_UNAUTHORIZED;

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED.value())
            .body(
                new StandardResponse<>()
                    .setStatus(HttpStatus.UNAUTHORIZED)
                    .setError(errorMessage)
            );
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<StandardResponse<Object>> httpException(
        HttpStatusCodeException exception
    ) {
        Integer statusCode = exception.getStatusCode().value();
        String errorMessage = exception.getMessage();

        if (null == errorMessage) {
            errorMessage = exception.getStatusText();
        }

        return ResponseEntity
            .status(statusCode)
            .body(
                new StandardResponse<>()
                    .setStatus(HttpStatus.valueOf(statusCode))
                    .setError(errorMessage)
            );
    }

    @ExceptionHandler(RequestHttpException.class)
    public ResponseEntity<StandardResponse<?>> requestHttpException(
        RequestHttpException exception
    ) {
        StandardResponse<?> responseBody = exception.getResponseBody();
        Integer statusCode = responseBody.getStatus();
        return ResponseEntity.status(statusCode).body(responseBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponse<Object>> validationException(
        MethodArgumentNotValidException exception
    ) {
        Integer statusCode = HttpStatus.BAD_REQUEST.value();
        List<FieldError> errors = exception.getBindingResult().getFieldErrors();
        Map<String, String> failedValidations = new HashMap<>();
        for (FieldError error : errors) {
            String message = error.getDefaultMessage();
            String key = error.getField();
            if (failedValidations.containsKey(key)) continue;
            failedValidations.put(key, message);
        }

        return ResponseEntity
            .status(statusCode)
            .body(
                new StandardResponse<>()
                    .setStatus(HttpStatus.valueOf(statusCode))
                    .setFormError(failedValidations)
            );
    }
}
