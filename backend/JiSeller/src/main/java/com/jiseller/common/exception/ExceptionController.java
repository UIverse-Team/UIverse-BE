package com.jiseller.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ExceptionResponse> customExceptionHandler(DomainException e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(e.getStatus())
                .body(e.toResponse());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<com.jishop.common.exception.ExceptionResponse> exceptionHandler(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new com.jishop.common.exception.ExceptionResponse(e.getMessage()));
    }
}
