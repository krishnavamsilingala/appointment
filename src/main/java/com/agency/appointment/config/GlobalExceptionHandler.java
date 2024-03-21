package com.agency.appointment.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // Extracting the exception message
        String errorMessage = e.getMessage();

        // Returning the error message in the ResponseEntity
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}

