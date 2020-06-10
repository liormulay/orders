package com.first.hello.model;

import java.io.Serializable;
import java.util.List;

/**
 * User send this when he want to make an order
 */
public class OrderRequest implements Serializable {

    private static final long serialVersionUID = -7817376925341892843L;

    /**
     * The items that user wants
     */
    private List<ItemRequest> itemsRequest;

    public OrderRequest() {
    }

    public void setItemsRequest(List<ItemRequest> itemsRequest) {
        this.itemsRequest = itemsRequest;
    }

    public List<ItemRequest> getItemsRequest() {
        return itemsRequest;
    }
}
