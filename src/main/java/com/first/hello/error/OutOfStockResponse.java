package com.first.hello.error;

import com.first.hello.entity.Product;

import java.io.Serializable;
import java.util.List;

/**
 * Send this response if there are not enough of the products in the stock
 * according to the order of the user
 */
public class OutOfStockResponse implements Serializable {

    private static final long serialVersionUID = -2584174142959184780L;

    private final String message = "There are not enough of those products according to the order";

    /**
     * The products that not enough in the stock
     */
    private final List<Product> products;

    public OutOfStockResponse(List<Product> products) {
        this.products = products;
    }

    public String getMessage() {
        return message;
    }

    public List<Product> getProducts() {
        return products;
    }

}
