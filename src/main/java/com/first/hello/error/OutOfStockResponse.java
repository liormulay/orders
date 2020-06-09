package com.first.hello.error;

import com.first.hello.entity.Product;

import java.io.Serializable;
import java.util.List;

public class OutOfStockResponse implements Serializable {

    private static final long serialVersionUID = -2584174142959184780L;

    private final String message = "The following products are out of the stock";

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
