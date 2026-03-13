package com.yourcompany.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorResponse> clientNotFoundExceptionHandler(ClientNotFoundException ex) {
        return buildResponseBody(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> bookNotFoundExceptionHandler(BookNotFoundException ex) {
        return buildResponseBody(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> orderNotFoundExceptionHandler(OrderNotFoundException ex) {
        return buildResponseBody(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<ErrorResponse> requestNotFoundExceptionHandler(RequestNotFoundException ex) {
        return buildResponseBody(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFoundExceptionHandler(EntityNotFoundException ex) {
        return buildResponseBody(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ErrorResponse> clientExceptionHandler(ClientException ex) {
        return buildResponseBody(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(BookException.class)
    public ResponseEntity<ErrorResponse> bookExceptionHandler(BookException ex) {
        return buildResponseBody(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorResponse> orderExceptionHandler(OrderException ex) {
        return buildResponseBody(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        return buildResponseBody(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> ioExceptionHandler(IOException ex) {
        return buildResponseBody(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildResponseBody(HttpStatus status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), message);
        return ResponseEntity.status(status).body(errorResponse);
    }
}

record ErrorResponse(int status, String message) {}