package com.jishop.common.exception;

import com.example.log.common.exception.ErrorType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DomainException extends RuntimeException {

    private final String message;
    private final HttpStatus status;

    public DomainException(ErrorType errorType) {
        this.message = errorType.getMessage();
        this.status = errorType.getHttpStatus();
    }

    public ExceptionResponse toResponse() {
        return new ExceptionResponse(message);
    }

}
