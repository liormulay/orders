package com.first.hello.model;

public class ProductSaleModel extends ItemRequestModel {

    private static final long serialVersionUID = -5235656766585797092L;

    private String productName;

    private String imageUrl;

    public ProductSaleModel(int productId, String productName, String imageUrl) {
        super(productId, 0);
        this.productName = productName;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
