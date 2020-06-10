package com.first.hello.rest;

import com.first.hello.dao.OrderDAO;
import com.first.hello.dao.ProductDAO;
import com.first.hello.dao.UserDAO;
import com.first.hello.entity.Item;
import com.first.hello.entity.Order;
import com.first.hello.entity.Product;
import com.first.hello.entity.User;
import com.first.hello.error.OutOfStockException;
import com.first.hello.error.ProductNotFoundException;
import com.first.hello.model.*;
import com.first.hello.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.first.hello.model.OrderResponse.createOrderResponse;

@RestController
public class WebController {

    private static final long TREE_DAYS = 1000 * 60 * 60 * 24 * 3;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserDAO userDAO;

    /**
     * @return all products in the stock
     */
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    /**
     * Use this when user wants to make an order
     *
     * @param orderRequest the order that user want to make
     * @return ok message with the bill
     */
    @RequestMapping(value = "order", method = RequestMethod.POST)
    public String makeOrder(@RequestBody OrderRequest orderRequest) {
        String loggedInUserName = securityService.findLoggedInUserName();
        User loggedInUser = userDAO.findByUserName(loggedInUserName);
        Order order = new Order(new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() + TREE_DAYS));
        fillOrder(orderRequest, order);
        loggedInUser.addOrder(order);
        userDAO.save(loggedInUser);
        return "You order successfully your bill is " + order.getTotal();
    }

    /**
     * Make the order to the user check if the are enough from all that user request <br>
     * if yes then offset this from the stock <br>
     * if not throw error and return to the user message with the products that are not enough
     * @param orderRequest the order that user want to make
     * @param order that will save in the database
     */
    private void fillOrder(OrderRequest orderRequest, Order order) {
        List<Product> outOfStockProducts = new ArrayList<>();
        boolean isOutOfStock = false;
        for (ItemRequest itemRequest : orderRequest.getItemsRequest()) {
            int productId = itemRequest.getProductId();
            Optional<Product> optionalProduct = productDAO.findById(productId);
            if (!optionalProduct.isPresent()) {
                throw new ProductNotFoundException("product with " + productId + " id hasn't found");
            }
            Product product = optionalProduct.get();
            int itemQuantity = itemRequest.getQuantity();
            int stockQuantity = product.getStockQuantity();
            if (itemQuantity > stockQuantity) {
                outOfStockProducts.add(product);
                isOutOfStock = true;
            }
            if (!isOutOfStock) {
                product.setStockQuantity(stockQuantity - itemQuantity);
                Item item = new Item(itemQuantity);
                product.addItem(item);
                order.addItem(item);
            }
        }
        if (isOutOfStock) {
            throw new OutOfStockException(outOfStockProducts);
        }
    }

    /**
     *
     * @return all orders that user made
     */
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public List<OrderResponse> getUserOrders() {
        String loggedInUserName = securityService.findLoggedInUserName();
        User loggedInUser = userDAO.findByUserName(loggedInUserName);
        List<Order> orders = orderDAO.findByUser(loggedInUser);
        List<OrderResponse> ordersResponse = new ArrayList<>();
        for (Order order : orders) {
            ordersResponse.add(createOrderResponse(order));
        }
        return ordersResponse;
    }

    /**
     * Authorize only for admin
     * @return all orders that have made for any user
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/all_orders", method = RequestMethod.GET)
    public List<OrderResponseWithUsername> getAllOrders() {
        List<Order> orders = orderDAO.findAll();
        List<OrderResponseWithUsername> ordersResponse = new ArrayList<>();
        for (Order order : orders) {
            OrderResponseWithUsername orderResponse = (OrderResponseWithUsername) createOrderResponse(order);
            orderResponse.setUsername(order.getUser().getUserName());
            ordersResponse.add(orderResponse);
        }
        return ordersResponse;
    }

}
