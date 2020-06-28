package com.first.hello.model;

public class ProductSaleModel extends ItemRequestModel {

    private static final long serialVersionUID = -5235656766585797092L;

    private String productName;

    public ProductSaleModel(int productId, String productName) {
        super(productId, 0);
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void addToQuantity(int quantity) {
        this.quantity += quantity;
    }
}
