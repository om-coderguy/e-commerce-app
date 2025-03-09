package com.ecommerceapplication.ecommeceapp.repository;

import com.ecommerceapplication.ecommeceapp.entity.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart,Integer> {
}
