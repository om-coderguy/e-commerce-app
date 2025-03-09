package com.ecommerceapplication.ecommeceapp.service;

import com.ecommerceapplication.ecommeceapp.dto.OrderDTO;
import com.ecommerceapplication.ecommeceapp.entity.Order;
import com.ecommerceapplication.ecommeceapp.entity.Product;

import java.util.List;

public interface OrderService {

    public List<Order> getAllOrders();

    public Order getOrderById(Integer id);

    public OrderDTO saveOrder(OrderDTO orderDTO);

    public String changeOrderStatus(OrderDTO orderDTO,Integer deliveryId);

    List<OrderDTO> getOrdersBySellerUserId(Integer userId);

    void deleteOrderById(Integer orderId);
}
