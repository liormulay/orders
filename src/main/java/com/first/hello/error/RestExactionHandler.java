package com.first.hello.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExactionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(ProductNotFoundException exception) {
        ErrorResponse productErrorResponse =
                new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(productErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<OutOfStockResponse> handleException(OutOfStockException outOfStockException){
        OutOfStockResponse outOfStockResponse =
                new OutOfStockResponse(outOfStockException.getOutOfStockProducts());
        return new ResponseEntity<>(outOfStockResponse,HttpStatus.BAD_REQUEST);
    }
}
