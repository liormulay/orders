package com.first.hello.error;

import java.util.List;

/**
 * Throw this exception if product hasn't found in the database
 */
public class ProductNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 3557285293857356572L;
    private List<Integer> missedIds;

    public ProductNotFoundException(String message, List<Integer> missedIds) {
        super(message);
        this.missedIds  = missedIds;
    }

    public List<Integer> getMissedIds() {
        return missedIds;
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
