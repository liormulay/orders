package com.first.hello.rest;

import com.first.hello.dao.OrderDAO;
import com.first.hello.dao.ProductDAO;
import com.first.hello.dao.UserDAO;
import com.first.hello.entity.Item;
import com.first.hello.entity.Order;
import com.first.hello.entity.Product;
import com.first.hello.entity.User;
import com.first.hello.error.NotPositiveQuantityException;
import com.first.hello.error.OutOfStockException;
import com.first.hello.error.ProductNotFoundException;
import com.first.hello.model.ItemRequestModel;
import com.first.hello.model.ItemsRequestModel;
import com.first.hello.model.OrderResponse;
import com.first.hello.model.OrderResponseWithUsername;
import com.first.hello.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.first.hello.model.OrderResponse.createOrderResponse;

@RestController
public class WebController {

    private static final long TREE_DAYS = 1000 * 60 * 60 * 24 * 3;
    private static final String MISSED_ID_MESSAGE = "The following ids are missed";
    private static final String OUT_OF_STOCK_MESSAGE = "There are not enough from those products in the stock";

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
    public String makeOrder(@RequestBody ItemsRequestModel orderRequest) {
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

    /**
     * Authorize only for admin
     *
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

    /**
     * Save new products
     *
     * @param products to be saved
     * @return ok message
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public String addNewProducts(@RequestBody List<Product> products) {
        for (Product product : products) {
            product.setProductId(0);
            productDAO.save(product);
        }
        return "All products saved successfully";
    }

    /**
     * Admin use this to add existing products to the stock
     * @param itemsRequestModel contains the products that admin want to add
     * @return ok message
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/products", method = RequestMethod.PUT)
    public String addProductsToStock(@RequestBody ItemsRequestModel itemsRequestModel) {
        List<ItemRequestModel> itemsRequest = itemsRequestModel.getItemsRequest();
        validateQuantity(itemsRequest);
        List<Integer> missedIds = new ArrayList<>();
        List<Product> productsToSave = new ArrayList<>();

        for (ItemRequestModel itemRequestModel : itemsRequest) {
            int productId = itemRequestModel.getProductId();
            Optional<Product> optionalProduct = productDAO.findById(productId);
            if (!optionalProduct.isPresent()) {
                missedIds.add(productId);
            } else {
                Product productToSave = optionalProduct.get();
                productToSave.setStockQuantity(productToSave.getStockQuantity() + itemRequestModel.getQuantity());
                productsToSave.add(productToSave);
            }
        }


        if (!missedIds.isEmpty()) {
            throw new ProductNotFoundException(MISSED_ID_MESSAGE, missedIds);
        }

        for (Product productToSave : productsToSave) {
            productDAO.save(productToSave);
        }
        return "All products updated";

    }

    /**
     * check that admin give only positive values to add
     * @param modelList the items that admin sent
     */
    private void validateQuantity(List<ItemRequestModel> modelList) {
        for (ItemRequestModel model : modelList) {
            if (model.getQuantity() <= 0) {
                throw new NotPositiveQuantityException("There is not positive quantity");
            }
        }
    }


}
