package com.first.hello.error;

import com.first.hello.entity.Product;

import java.util.List;

/**
 * Throw this exception if there are not enough of the products in the stock
 * according to the order of the user
 */
public class OutOfStockException extends RuntimeException {

    private static final long serialVersionUID = -8541754809865203514L;

    private List<Product> outOfStockProducts;

    public OutOfStockException(List<Product> outOfStockProducts) {
        this.outOfStockProducts = outOfStockProducts;
    }

    public List<Product> getOutOfStockProducts() {
        return outOfStockProducts;
    }

    public OutOfStockException(String message) {
        super(message);
    }

    public OutOfStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public OutOfStockException(Throwable cause) {
        super(cause);
    }

    public OutOfStockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
