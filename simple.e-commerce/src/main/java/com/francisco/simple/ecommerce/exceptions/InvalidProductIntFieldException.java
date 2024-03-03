package com.francisco.simple.ecommerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidProductIntFieldException extends RuntimeException {
    public InvalidProductIntFieldException(String fieldName, Integer value) {
        super("Invalid value for field '" + fieldName + "': " + value);
    }
}