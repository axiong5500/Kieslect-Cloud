package com.kieslect.outapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ApiNotOpenException extends RuntimeException{
    public ApiNotOpenException(String message) {
        super(message);
    }
}
