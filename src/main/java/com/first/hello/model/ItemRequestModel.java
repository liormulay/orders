package com.first.hello.model;

import java.io.Serializable;

/**
 * User send list of this model wrapped in {@link ItemsRequestModel}
 */
public class ItemRequestModel implements Serializable {

    private static final long serialVersionUID = 5301826533889876287L;

    int productId;

    int quantity;

    public ItemRequestModel(int productId, int quantity) {
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
