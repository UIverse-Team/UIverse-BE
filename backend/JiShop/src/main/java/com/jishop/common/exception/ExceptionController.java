package com.jishop.common.exception;

import com.example.log.common.exception.DomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {
    private static final String UNKNOWN_ERROR = "알 수 없는 에러";

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ExceptionResponse> customExceptionHandler(DomainException e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(e.getStatus())
                .body(e.toResponse());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> exceptionHandler(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(UNKNOWN_ERROR));
    }

}
