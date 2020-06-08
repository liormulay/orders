package com.first.hello.model;

public class ItemResponse extends ItemRequest{

    private static final long serialVersionUID = 5741038666778991543L;

    private String productName;

    private float price;


    public ItemResponse(int productId, int quantity, String productName, float price) {
        super(productId, quantity);
        this.productName = productName;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
