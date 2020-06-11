package com.first.hello.model;

import java.io.Serializable;
import java.util.List;

/**
 * User send this when he want to make an order or manager update products
 */
public class ItemsRequestModel implements Serializable {

    private static final long serialVersionUID = -7817376925341892843L;

    /**
     * The items that user wants
     */
    private List<ItemRequestModel> itemsRequest;

    public ItemsRequestModel() {
    }

    public void setItemsRequest(List<ItemRequestModel> itemsRequest) {
        this.itemsRequest = itemsRequest;
    }

    public List<ItemRequestModel> getItemsRequest() {
        return itemsRequest;
    }
}
