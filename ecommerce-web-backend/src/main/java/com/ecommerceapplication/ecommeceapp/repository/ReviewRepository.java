package com.ecommerceapplication.ecommeceapp.repository;

import com.ecommerceapplication.ecommeceapp.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
