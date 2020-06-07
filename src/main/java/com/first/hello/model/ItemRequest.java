package com.first.hello.model;

import java.io.Serializable;

public class ItemRequest implements Serializable {

    private static final long serialVersionUID = 5301826533889876287L;

    private int productId;

    private int quantity;

    public ItemRequest(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
