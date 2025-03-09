package com.ecommerceapplication.ecommeceapp.service;

import com.ecommerceapplication.ecommeceapp.dto.CartDTO;
import com.ecommerceapplication.ecommeceapp.entity.Cart;
import com.ecommerceapplication.ecommeceapp.entity.Category;
import com.ecommerceapplication.ecommeceapp.entity.Product;

public interface CartService {

    public Cart addProductInCart(CartDTO cartDTO);

    public Cart getCartByUserId(Integer userId);

    Product deleteItemFromCart(Integer userId, Integer productId);
}
