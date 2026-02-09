package com.yourcompany.exceptions;

import org.hibernate.HibernateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorResponse> clientNotFoundHandler(ClientNotFoundException ex) {
        return buildResponseBody(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> bookNotFoundHandler(BookNotFoundException ex) {
        return buildResponseBody(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> orderNotFoundHandler(OrderNotFoundException ex) {
        return buildResponseBody(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<ErrorResponse> requestNotFoundHandler(RequestNotFoundException ex) {
        return buildResponseBody(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(HibernateException.class)
    public ResponseEntity<ErrorResponse> hibernateExceptionHandler(HibernateException ex) {
        return buildResponseBody(HttpStatus.BAD_REQUEST, "Ошибка при отправке запроса в БД.");
    }

    private ResponseEntity<ErrorResponse> buildResponseBody(HttpStatus status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), message);
        return ResponseEntity.status(status).body(errorResponse);
    }
}

record ErrorResponse(int status, String message) {}