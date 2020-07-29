package com.first.hello.model;

import java.io.Serializable;

public class ProductSaleModel implements Serializable {

    private static final long serialVersionUID = -5235656766585797092L;

    private int productId;

    private String productName;

    private float price;

    /**
     * The quantity of the product in the stock
     */
    private int stockQuantity;

    private String imageUrl;

    private int salesQuantity;

    public ProductSaleModel(int productId, String productName, float price, int stockQuantity, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void addToSalesQuantity(int salesQuantity) {
        this.salesQuantity += salesQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getSalesQuantity() {
        return salesQuantity;
    }

    public void setSalesQuantity(int salesQuantity) {
        this.salesQuantity = salesQuantity;
    }
}
