package com.first.hello.rest;

import com.first.hello.dao.OrderDAO;
import com.first.hello.dao.ProductDAO;
import com.first.hello.entity.Item;
import com.first.hello.entity.Order;
import com.first.hello.entity.Product;
import com.first.hello.error.NotPositiveQuantityException;
import com.first.hello.error.ProductNotFoundException;
import com.first.hello.model.ItemRequestModel;
import com.first.hello.model.ItemsRequestModel;
import com.first.hello.model.OrderResponseWithUsername;
import com.first.hello.model.ProductSaleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.function.Supplier;

import static com.first.hello.model.OrderResponse.createOrderResponse;
import static com.first.hello.rest.CustomerController.MISSED_ID_MESSAGE;

/**
 * Rest Controller that only the admin has access
 */
@RestController
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private OrderDAO orderDAO;

    /**
     * @return all products in the stock
     */
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    /**
     * Authorize only for admin
     *
     * @return all orders that have made for any user
     */
    @RequestMapping(value = "/all-orders", method = RequestMethod.GET)
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
    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public ResponseEntity<String> addNewProducts(@RequestBody List<Product> products) {
        for (Product product : products) {
            addNewProduct(product);
        }
        return ResponseEntity.ok("All products saved successfully");
    }

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public ResponseEntity<String> addNewProduct(@RequestBody Product product) {
        product.setProductId(0);
        productDAO.save(product);
        return ResponseEntity.ok("Product save successfully");
    }

    /**
     * Admin use this to add existing products to the stock
     *
     * @param itemsRequestModel contains the products that admin want to add
     * @return ok message
     */
    @RequestMapping(value = "/products", method = RequestMethod.PUT)
    public ResponseEntity<String> addProductsToStock(@RequestBody ItemsRequestModel itemsRequestModel) {
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
        return ResponseEntity.ok("All products updated");

    }

    @RequestMapping(value = "/add-quantity-to-stock", method = RequestMethod.PUT)
    public ResponseEntity<?> addQuantityToStock(@RequestBody ItemRequestModel itemRequestModel) throws Throwable {
        int productId = itemRequestModel.getProductId();
        Product product = productDAO.findById(productId)
                .orElseThrow((Supplier<Throwable>) () ->
                        new ProductNotFoundException(MISSED_ID_MESSAGE, Collections.singletonList(productId)));
        product.setStockQuantity(product.getStockQuantity() + itemRequestModel.getQuantity());
        productDAO.save(product);
        return ResponseEntity.ok("Product was updated");
    }

    /**
     * check that admin give only positive values to add
     *
     * @param modelList the items that admin sent
     */
    private void validateQuantity(List<ItemRequestModel> modelList) {
        for (ItemRequestModel model : modelList) {
            if (model.getQuantity() <= 0) {
                throw new NotPositiveQuantityException("There is not positive quantity");
            }
        }
    }

    /**
     * @return list of products with the amount of their total sales descendant
     */
    @RequestMapping(method = RequestMethod.GET, value = "/products-with-sales")
    public List<ProductSaleModel> getProductsBySales() {
        List<Product> products = productDAO.findAll();
        List<ProductSaleModel> response = new ArrayList<>();
        for (Product product : products) {
            List<Item> items = product.getItems();
            ProductSaleModel productSaleModel = new ProductSaleModel(product.getProductId(), product.getProductName(),
                    product.getPrice(), product.getStockQuantity(), product.getImageUrl());
            if (items != null && items.size() > 0) {
                for (Item item : items) {
                    productSaleModel.addToSalesQuantity(item.getQuantity());
                }
            }
            response.add(productSaleModel);
        }
        return response;
    }

}
