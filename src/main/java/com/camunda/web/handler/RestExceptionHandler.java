package com.camunda.web.handler;

import com.camunda.exception.BadRequestException;
import com.camunda.web.response.ErrorResponse;
import com.camunda.web.util.RestResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Order
@ControllerAdvice(basePackages = "com.camunda.web.controller")
@Slf4j
public class RestExceptionHandler {

    private RestResponseBuilder restResponseBuilder;

    public RestExceptionHandler(final RestResponseBuilder restResponseBuilder) {
        this.restResponseBuilder = restResponseBuilder;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleUnexpectedException(final RuntimeException e) {
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        final ErrorResponse error = new ErrorResponse(String.valueOf(status.value()), e.getMessage());
        return new ResponseEntity<>(restResponseBuilder.fail(error), status);
    }


    @ExceptionHandler({
            BadRequestException.class
    })
    public ResponseEntity handleBadRequestException(final Exception e) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;

        final ErrorResponse error = new ErrorResponse(String.valueOf(status.value()), e.getMessage());
        return new ResponseEntity<>(restResponseBuilder.fail(error), status);
    }
}


