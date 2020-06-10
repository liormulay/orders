package com.first.hello.model;

import java.util.Date;
import java.util.List;

/**
 * Send this model to the admin when he wants to see the orders and their customers
 */
public class OrderResponseWithUsername extends OrderResponse{

    private static final long serialVersionUID = 4676260605353164697L;

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public OrderResponseWithUsername(float total, List<ItemResponse> items, Date orderDate, Date shipDate) {
        super(total, items, orderDate, shipDate);
    }
}
