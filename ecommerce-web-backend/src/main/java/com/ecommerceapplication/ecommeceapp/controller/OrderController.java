package com.ecommerceapplication.ecommeceapp.controller;

import com.ecommerceapplication.ecommeceapp.dto.OrderDTO;
import com.ecommerceapplication.ecommeceapp.dto.ProductDTO;
import com.ecommerceapplication.ecommeceapp.entity.Order;
import com.ecommerceapplication.ecommeceapp.entity.Product;
import com.ecommerceapplication.ecommeceapp.exception.ResourceNotFoundException;
import com.ecommerceapplication.ecommeceapp.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    static final Logger LOGGER= LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping()
    public ResponseEntity<?> saveOrder(@RequestBody @Valid OrderDTO orderDTO ) {
        LOGGER.info("Received request to save order\n");
        try {
            orderDTO= orderService.saveOrder(orderDTO);
            LOGGER.info("Save request for order Successful");
            return new ResponseEntity<>(orderDTO, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("request for Order Unsuccessful \nException = "+ ex);
            return new ResponseEntity<>("Error in Saving Order info " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/order_status/{id}")
    public ResponseEntity<?> updateOrder(@RequestBody OrderDTO orderDTO,@PathVariable("id") Integer deliveryId) {
        LOGGER.info("Received request to save order\n");
        try {
            String message=orderService.changeOrderStatus(orderDTO,deliveryId);
            LOGGER.info("Save request for order Successful \n"+message);
            return new ResponseEntity<>(orderDTO, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("request for Order Unsuccessful \nException = "+ ex);
            return new ResponseEntity<>("Error in Saving Order info " + ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping()
    public ResponseEntity<?> getOrders() {
        LOGGER.info("Received request to get all orders\n");
        try {
            List<OrderDTO> order = orderService.getAllOrders().stream().map(ord->OrderDTO.toDTO(ord)).collect(Collectors.toList());
            LOGGER.info("Get request for orders Successful");
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("request for Order Unsuccessful \nException = "+ ex.getMessage());
            return new ResponseEntity<>("Error in Saving Order info " + ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/seller/{userId}")
    public ResponseEntity<?> getOrdersBySellerUserId(@PathVariable("userId") Integer userId) {
        LOGGER.info("Fetching orders for seller with userId: {}", userId);
        try {
            List<OrderDTO> orders = orderService.getOrdersBySellerUserId(userId);
            if (orders.isEmpty()) {
                return new ResponseEntity<>("No orders found for seller with userId: " + userId, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Error while fetching orders: {}", ex.getMessage());
            return new ResponseEntity<>("Internal Server Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Integer orderId) {
        try {
            orderService.deleteOrderById(orderId);
            return ResponseEntity.ok("Order deleted successfully!");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred while deleting the order.");
        }
    }
}
