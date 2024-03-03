package com.francisco.simple.ecommerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidProductStringFieldException extends RuntimeException {
    public InvalidProductStringFieldException(String fieldName, String value) {
        super("Invalid value for field '" + fieldName + "': " + value);
    }
}
