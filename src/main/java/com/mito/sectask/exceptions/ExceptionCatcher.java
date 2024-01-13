package com.mito.sectask.exceptions;

import com.mito.sectask.dto.response.Response;
import com.mito.sectask.values.MESSAGES;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;

@ControllerAdvice
public class ExceptionCatcher {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public Response<Object> internalServerException(Exception exception) {
        log.error("Unhandled exception occured", exception);
        return new Response<Object>(HttpStatus.INTERNAL_SERVER_ERROR).setMessage(MESSAGES.ERROR_INTERNAL_SERVER);
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public Response<Object> httpStatusCodeException(HttpStatusCodeException exception) {
        return new Response<Object>(exception.getStatusCode()).setMessage(exception.getStatusText());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Object> validationException(MethodArgumentNotValidException exception) {
        List<FieldError> errors = exception.getBindingResult().getFieldErrors();
        Map<String, String> failedValidations = new HashMap<>();
        for (FieldError error : errors) {
            String message = error.getDefaultMessage();
            String key = error.getField();
            if (failedValidations.containsKey(key)) continue;
            failedValidations.put(key, message);
        }

        return new Response<>(HttpStatus.BAD_REQUEST).setError(failedValidations);
    }
}
