package com.first.hello.model;

import java.io.Serializable;
import java.util.List;

public class OrderRequest implements Serializable {

    private static final long serialVersionUID = -7817376925341892843L;

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
