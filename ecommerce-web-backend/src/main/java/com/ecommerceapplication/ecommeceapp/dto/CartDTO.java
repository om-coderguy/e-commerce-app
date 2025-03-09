package com.ecommerceapplication.ecommeceapp.dto;


import com.ecommerceapplication.ecommeceapp.entity.Cart;
import com.ecommerceapplication.ecommeceapp.entity.Inventory;
import com.ecommerceapplication.ecommeceapp.entity.Product;
import com.ecommerceapplication.ecommeceapp.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

  private Integer cartId;

  private Integer userId;

  private Integer quantity;

  private Integer ProductId;

  public CartDTO(Integer cartId, Integer userId) {
    this.cartId = cartId;
    this.userId = userId;
  }

  public static CartDTO toDTO(Cart cart) {
    return new CartDTO(cart.getCartId(), cart.getUser().getUserId());
  }

  public static Cart toCart(Integer cartId, User user, List<Product> products) {
    return new Cart(cartId,user,products);
  }

}


