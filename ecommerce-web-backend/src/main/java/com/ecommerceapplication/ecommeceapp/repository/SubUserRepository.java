package com.ecommerceapplication.ecommeceapp.repository;

import com.ecommerceapplication.ecommeceapp.entity.SubUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubUserRepository extends JpaRepository<SubUser, Integer> {
}