package com.francisco.simple.ecommerce.exceptions.handler;

import com.francisco.simple.ecommerce.dto.ErrorDetail;
import com.francisco.simple.ecommerce.exceptions.InvalidProductException;
import com.francisco.simple.ecommerce.exceptions.InvalidProductIntFieldException;
import com.francisco.simple.ecommerce.exceptions.InvalidProductStringFieldException;
import com.francisco.simple.ecommerce.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest httpServletRequest) {
        return buildErrorResponse(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidProductStringFieldException.class)
    public ResponseEntity<?> handleInvalidProductFieldException(InvalidProductStringFieldException exception, HttpServletRequest httpServletRequest) {
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidProductIntFieldException.class)
    public ResponseEntity<?> InvalidProductIntFieldException(InvalidProductIntFieldException exception, HttpServletRequest httpServletRequest) {
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<?> InvalidProductException(InvalidProductException exception, HttpServletRequest httpServletRequest) {
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorDetail> buildErrorResponse(Exception exception, HttpStatus status) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(LocalDateTime.now(ZoneId.of("Europe/Madrid")));
        errorDetail.setStatus(status.value());
        errorDetail.setTitle(exception.getMessage());
        errorDetail.setDetail(exception.getClass().getName());
        return new ResponseEntity<>(errorDetail, status);
    }
}
