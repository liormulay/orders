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
import com.first.hello.model.ItemRequestModel;
import com.first.hello.model.ItemResponse;
import com.first.hello.model.ItemsRequestModel;
import com.first.hello.model.OrderResponse;
import com.first.hello.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.first.hello.model.OrderResponse.createOrderResponse;

/**
 * Rest Controller for the customer (available also for admin)
 */
@RestController
public class CustomerController {

    private static final long TREE_DAYS = 1000 * 60 * 60 * 24 * 3;
    public static final String MISSED_ID_MESSAGE = "The following ids are missed";
    private static final String OUT_OF_STOCK_MESSAGE = "There are not enough from those products in the stock";

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserDAO userDAO;

    @RequestMapping(value = "/check-token", method = RequestMethod.GET)
    public ResponseEntity<?> checkToken() {
        return ResponseEntity.ok(true);
    }


    /**
     * Use this to give to user list of products
     */
    @RequestMapping(value = "/request_items", method = RequestMethod.GET)
    public List<ItemResponse> getProducts() {
        List<Product> products = productDAO.findAll();
        List<ItemResponse> itemsResponse = new ArrayList<>();
        for (Product product : products) {
            itemsResponse.add(new ItemResponse(product.getProductId(), 0, product.getProductName()
                    , product.getPrice(), product.getImageUrl()));
        }
        return itemsResponse;
    }

    /**
     * Use this when user wants to make an order
     *
     * @param orderRequest the order that user want to make
     * @return ok message with the bill
     */
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public OrderResponse makeOrder(@RequestBody ItemsRequestModel orderRequest) {
        String loggedInUserName = securityService.findLoggedInUserName();
        User loggedInUser = userDAO.findByUserName(loggedInUserName);
        Order order = new Order(new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() + TREE_DAYS));
        fillOrder(orderRequest, order);
        loggedInUser.addOrder(order);
        userDAO.save(loggedInUser);
        return OrderResponse.createOrderResponse(order);
    }

    /**
     * Make the order to the user check if the are enough from all that user request <br>
     * if yes then offset this from the stock <br>
     * if not throw error and return to the user message with the products that are not enough<br>
     * if some ids are missed in database it will throw only {@link ProductNotFoundException}
     *
     * @param orderRequest the order that user want to make
     * @param order        that will save in the database
     */
    private void fillOrder(ItemsRequestModel orderRequest, Order order) {
        List<Product> outOfStockProducts = new ArrayList<>();
        List<Integer> missedIds = new ArrayList<>();
        for (ItemRequestModel itemRequestModel : orderRequest.getItemsRequest()) {
            int productId = itemRequestModel.getProductId();
            Optional<Product> optionalProduct = productDAO.findById(productId);
            if (!optionalProduct.isPresent()) {
                missedIds.add(productId);
            } else {
                Product product = optionalProduct.get();
                int itemQuantity = itemRequestModel.getQuantity();
                int stockQuantity = product.getStockQuantity();
                if (itemQuantity > stockQuantity) {
                    outOfStockProducts.add(product);
                }
                if (outOfStockProducts.isEmpty()) {
                    product.setStockQuantity(stockQuantity - itemQuantity);
                    Item item = new Item(itemQuantity);
                    product.addItem(item);
                    order.addItem(item);
                }
            }
        }
        if (!missedIds.isEmpty()) {
            throw new ProductNotFoundException(MISSED_ID_MESSAGE, missedIds);
        }
        if (!outOfStockProducts.isEmpty()) {
            throw new OutOfStockException(OUT_OF_STOCK_MESSAGE, outOfStockProducts);
        }
    }

    /**
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

}
