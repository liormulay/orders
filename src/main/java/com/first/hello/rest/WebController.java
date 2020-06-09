package com.first.hello.rest;

import com.first.hello.dao.OrderDAO;
import com.first.hello.dao.ProductDAO;
import com.first.hello.dao.UserDAO;
import com.first.hello.entity.Item;
import com.first.hello.entity.Order;
import com.first.hello.entity.Product;
import com.first.hello.entity.User;
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

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

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

    private void fillOrder(OrderRequest orderRequest, Order order) {
        for (ItemRequest itemRequest : orderRequest.getItemsRequest()) {
            int productId = itemRequest.getProductId();
            Optional<Product> optionalProduct = productDAO.findById(productId);
            if (!optionalProduct.isPresent()) {
                throw new ProductNotFoundException("product with " + productId + " id hasn't found");
            }
            Product product = optionalProduct.get();
            int quantity = itemRequest.getQuantity();
            Item item = new Item(quantity);
            product.addItem(item);
            order.addItem(item);
        }
    }

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


    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/all_orders",method = RequestMethod.GET)
    public List<OrderResponseWithUsername> getAllOrders(){
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
