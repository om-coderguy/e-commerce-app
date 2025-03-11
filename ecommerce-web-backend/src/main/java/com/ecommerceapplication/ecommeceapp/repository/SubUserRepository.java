package com.ecommerceapplication.ecommeceapp.repository;

import com.ecommerceapplication.ecommeceapp.entity.Seller;
import com.ecommerceapplication.ecommeceapp.entity.SubUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubUserRepository extends JpaRepository<SubUser, Integer> {
    List<SubUser> findBySeller(Seller seller);
}