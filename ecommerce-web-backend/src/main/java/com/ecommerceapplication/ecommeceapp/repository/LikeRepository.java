package com.ecommerceapplication.ecommeceapp.repository;

import com.ecommerceapplication.ecommeceapp.entity.Like;
import com.ecommerceapplication.ecommeceapp.entity.Product;
import com.ecommerceapplication.ecommeceapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByProduct_IdAndUser_UserId(Integer productId, Integer userId);

    boolean existsByProductAndUser(Product product, User user);

    List<Like> findByUser_UserIdAndLikedTrue(Integer userId);
}

