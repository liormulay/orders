package com.first.hello.error;

/**
 * Throw this exception if product hasn't found in the database
 */
public class ProductNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 3557285293857356572L;

    public ProductNotFoundException() {
    }

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductNotFoundException(Throwable cause) {
        super(cause);
    }

    public ProductNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
