package com.first.hello.error;

/**
 * Use this when admin try to add not positive quantity to the stock
 */
public class NotPositiveQuantityException extends RuntimeException{

    private static final long serialVersionUID = 4153691610548390589L;

    public NotPositiveQuantityException() {
    }

    public NotPositiveQuantityException(String message) {
        super(message);
    }

    public NotPositiveQuantityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotPositiveQuantityException(Throwable cause) {
        super(cause);
    }

    public NotPositiveQuantityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
