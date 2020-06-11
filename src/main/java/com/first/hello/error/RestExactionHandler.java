package com.first.hello.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExactionHandler {

    /**
     * Handle {@link ProductNotFoundException}
     * @param exception throw then product/s had not found in the database
     * @return report of ids that had not found
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResponseWithItems> handleException(ProductNotFoundException exception) {
        ErrorResponseWithItems productErrorResponse =
                new ErrorResponseWithItems(HttpStatus.NOT_FOUND.value(),
                        exception.getMessage(), System.currentTimeMillis(), exception.getMissedIds());
        return new ResponseEntity<>(productErrorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle {@link OutOfStockException}
     * @param outOfStockException throw when there are not enough from some products
     * when user wants to make an order
     * @return report of products that are not enough
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResponseWithItems> handleException(OutOfStockException outOfStockException) {
        ErrorResponseWithItems outOfStockResponse =
                new ErrorResponseWithItems(HttpStatus.BAD_REQUEST.value(), outOfStockException.getMessage(),
                        System.currentTimeMillis(), outOfStockException.getOutOfStockProducts());
        return new ResponseEntity<>(outOfStockResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle {@link NotPositiveQuantityException}
     * @param notPositiveQuantityException throw when manger try to add negative quantity
     * @return error message
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(NotPositiveQuantityException notPositiveQuantityException) {
        ErrorResponse errorResponse =
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), notPositiveQuantityException.getMessage()
                        , System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
