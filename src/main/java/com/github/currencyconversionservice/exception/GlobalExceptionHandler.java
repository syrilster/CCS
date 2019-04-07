package com.github.currencyconversionservice.exception;

import com.github.currencyconversionservice.handler.GlobalExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<GlobalExceptionResponse> handleHttpClientErrorException(HttpClientErrorException ex, WebRequest request) {
        log.error("HttpClientError in handler: " + ex, ex);
        GlobalExceptionResponse globalExceptionResponse = new GlobalExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error");
        return new ResponseEntity<>(globalExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
