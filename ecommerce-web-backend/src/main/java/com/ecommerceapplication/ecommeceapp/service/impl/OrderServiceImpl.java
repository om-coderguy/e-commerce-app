package com.ecommerceapplication.ecommeceapp.service.impl;

import com.ecommerceapplication.ecommeceapp.constant.OrderStatus;
import com.ecommerceapplication.ecommeceapp.dto.OrderDTO;
import com.ecommerceapplication.ecommeceapp.entity.Order;
import com.ecommerceapplication.ecommeceapp.entity.Product;
import com.ecommerceapplication.ecommeceapp.entity.User;
import com.ecommerceapplication.ecommeceapp.exception.CustomException;
import com.ecommerceapplication.ecommeceapp.exception.ResourceNotFoundException;
import com.ecommerceapplication.ecommeceapp.service.NotificationService;
import com.ecommerceapplication.ecommeceapp.repository.OrderRepository;
import com.ecommerceapplication.ecommeceapp.service.OrderService;
import com.ecommerceapplication.ecommeceapp.service.ProductService;
import com.ecommerceapplication.ecommeceapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import java.util.Calendar;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private NotificationService notificationService;


    @Override
    public List<Order> getAllOrders() {
        LOGGER.info("Request to fetch all orders from database\n");
        try {
            return (List<Order>) orderRepo.findAll();
        } catch (ResourceNotFoundException ex) {
            LOGGER.error("Request to fetch all orders from database\n");
            throw ex;
        } catch (Exception ex) {
            LOGGER.error("Unable to fetch all orders from database\n");
            throw new CustomException("Unhandled exception while get request for orders\n. Error-Message = " + ex.getMessage());
        }
    }

    @Override
    public Order getOrderById(Integer id) {
        LOGGER.info("Request to fetch order from database\n");
        try {
            return orderRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order with Id - " + id + " not found"));
        } catch (ResourceNotFoundException ex) {
            LOGGER.error("Unable to fetch order\n");
            throw ex;
        } catch (Exception ex) {
            LOGGER.error("Request to fetch order from database\n");
            throw new CustomException("Unhandled exception while get request for order\n. Error-Message = " + ex.getMessage());
        }
    }

    @Override
    public OrderDTO saveOrder(OrderDTO orderDTO) {
        LOGGER.info("Request to save new order to database\n");
        try {
            User user = userService.getUserById(orderDTO.getUserId());
            if (!user.isSiteUser()) {
                throw new CustomException("Only Site user can place order");
            }
            Product product = productService.getByProductId(orderDTO.getProductId());
            int quantity = product.getInventory().getTotalQuantity();
            if (quantity <= 0) {
                throw new CustomException("Product is not available");
            } else if (quantity < orderDTO.getQuantity()) {
                throw new CustomException("Order quantity is not present. Only " + quantity + " left in stock");
            }
            Order order = OrderDTO.toOrder(orderDTO, user, product);
            Calendar cal = Calendar.getInstance();
            order.setOrderId("ORD-" + UUID.randomUUID());
            order.setStatus(OrderStatus.CREATED);
            order.setCreationDate(cal.getTime());
            order.setDeliveryDate(cal.getTime());
            float costOutDisc = order.getQuantity() * product.getCost();
            order.setTotalCost(costOutDisc - (costOutDisc / 100 * product.getDiscount()));
            product.getInventory().setTotalQuantity(quantity - order.getQuantity());
            orderRepo.save(order);
            LOGGER.info("Order Placed successfully by Id - " + order.getOrderId());
            if (order.getUser().getEmail() != null && order.getUser().getMobileNo() != null)
                notificationService.send("Order placed Successfully", order.getUser().getMobileNo(), order.getUser().getEmail());
            orderDTO = OrderDTO.toDTO(order);
            return orderDTO;
        } catch (ResourceNotFoundException ex) {
            LOGGER.error("Unable to process Order in database - {}\n" + ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            LOGGER.error("Exception got - {}", ex.getMessage());
            throw new CustomException("Unhandled exception while placing order\n. Error-Message = " + ex.getMessage());
        }
    }

    @Override
    public String changeOrderStatus(OrderDTO orderDTO, Integer deliveryId) {
        LOGGER.info("Request to alter order status in database\n");
        try {
            if (!userService.getUserById(deliveryId).isDelivery()) {
                throw new CustomException("User is not of type 'Delivery'");
            }

            Order order = this.getOrderById(orderDTO.getId());
            order.setStatus(OrderStatus.PICK_UP);
            return "Order picked up by delivery";
        } catch (ResourceNotFoundException ex) {
            LOGGER.error("Enable to process the request\n");
            throw ex;
        } catch (Exception ex) {
            LOGGER.error("Enable to process the request\n");
            throw new CustomException("Unhandled exception while changing status of order \n. Error-Message = " + ex.getMessage());
        }
    }

    @Override
    public List<OrderDTO> getOrdersBySellerUserId(Integer userId) {
        List<Order> orders = orderRepo.findByProduct_Seller_User_UserId(userId);
        return orders.stream()
                .map(OrderDTO::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrderById(Integer orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        orderRepo.delete(order);
    }

}
