package com.ecommerceapplication.ecommeceapp.dto;

import com.ecommerceapplication.ecommeceapp.constant.OrderStatus;
import com.ecommerceapplication.ecommeceapp.entity.Order;
import com.ecommerceapplication.ecommeceapp.entity.Product;
import com.ecommerceapplication.ecommeceapp.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {

    private Integer id;
    private String orderId;
    private OrderStatus status;
    private Date creationDate;
    private Date deliveryDate;
    private Integer quantity;
    private float totalCost;
    private Integer userId;
    private Integer productId;

    private ProductDetailsDTO productDetails; // New field for additional product information

    public OrderDTO(Integer id, String orderId, OrderStatus status, Date creationDate, Date deliveryDate,
                    Integer quantity, float totalCost, Integer userId, Integer productId, ProductDetailsDTO productDetails) {
        this.id = id;
        this.orderId = orderId;
        this.status = status;
        this.creationDate = creationDate;
        this.deliveryDate = deliveryDate;
        this.quantity = quantity;
        this.totalCost = totalCost;
        this.userId = userId;
        this.productId = productId;
        this.productDetails = productDetails;
    }

    public OrderDTO(Integer id, Integer quantity, Integer userId, Integer productId) {
        this.id = id;
        this.quantity = quantity;
        this.userId = userId;
        this.productId = productId;
    }

    public static OrderDTO toDTO(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getOrderId(),
                order.getStatus(),
                order.getCreationDate(),
                order.getDeliveryDate(),
                order.getQuantity(),
                order.getTotalCost(),
                order.getUser().getUserId(),
                order.getProduct().getId(),
                ProductDetailsDTO.fromProduct(order.getProduct()) // Map product details
        );
    }

    public static Order toOrder(OrderDTO orderDTO, User user, Product product) {
        return new Order(orderDTO.getId(), orderDTO.getQuantity(), product, user);
    }
}
