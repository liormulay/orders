package com.first.hello.model;

import com.first.hello.entity.Item;
import com.first.hello.entity.Order;
import com.first.hello.entity.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Send this model to the user when he wants to see his orders <br>
 * @see OrderResponseWithUsername to add this class the username
 */
public class OrderResponse implements Serializable {

    private static final long serialVersionUID = -6803477002090476106L;

    private List<ItemResponse> items;

    private Date orderDate;

    private Date shipDate;

    private float total;


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

    /**
     * Function that recive {@link Order} object and return the corresponding {@link OrderResponse} object
     * @param order the entity object as it saved in database
     * @return the model that will send to the user
     */
    public static OrderResponse createOrderResponse(Order order) {
        List<Item> items = order.getItems();
        List<ItemResponse> itemsResponse = new ArrayList<>();
        for (Item item : items) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();
            int productId = product.getProductId();
            String productName = product.getProductName();
            float price = product.getPrice();
            ItemResponse itemResponse = new ItemResponse(productId, quantity, productName, price);
            itemsResponse.add(itemResponse);
        }
        float total = order.getTotal();
        Date orderDate = order.getOrderDate();
        Date shipDate = order.getShipDate();
        return new OrderResponseWithUsername(total, itemsResponse, orderDate, shipDate);
    }
}
