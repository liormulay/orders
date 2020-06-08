package com.first.hello.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrderResponse implements Serializable {

    private static final long serialVersionUID = -6803477002090476106L;

    private float total;

    private List<ItemResponse> items;

    private Date orderDate;

    private Date shipDate;

    public OrderResponse(float total, List<ItemResponse> items, Date orderDate, Date shipDate) {
        this.total = total;
        this.items = items;
        this.orderDate = orderDate;
        this.shipDate = shipDate;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public List<ItemResponse> getItems() {
        return items;
    }

    public void setItems(List<ItemResponse> items) {
        this.items = items;
    }
}
