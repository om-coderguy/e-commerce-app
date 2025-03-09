package com.ecommerceapplication.ecommeceapp.repository;

import com.ecommerceapplication.ecommeceapp.entity.Order;
import com.ecommerceapplication.ecommeceapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    @Query(value = "select p FROM Order AS o Join o.product p GROUP BY p.id ORDER BY count(p.id) DESC")
    List<Product> getTopProducts();

    @Query(value = "SELECT p FROM RecentProduct AS r Join r.product p Join r.user u WHERE u.id=:userId ORDER BY r.count DESC")
    List<Product> getRecentProducts(@Param("userId") Integer userId);

    List<Order> findByProduct_Seller_User_UserId(Integer userId);

    void deleteOrderById(Integer orderId);

}
