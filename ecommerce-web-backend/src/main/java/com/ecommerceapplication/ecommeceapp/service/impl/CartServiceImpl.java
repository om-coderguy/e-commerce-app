package com.ecommerceapplication.ecommeceapp.service.impl;

import com.ecommerceapplication.ecommeceapp.dto.CartDTO;
import com.ecommerceapplication.ecommeceapp.entity.Cart;
import com.ecommerceapplication.ecommeceapp.entity.Product;
import com.ecommerceapplication.ecommeceapp.entity.User;
import com.ecommerceapplication.ecommeceapp.exception.CustomException;
import com.ecommerceapplication.ecommeceapp.exception.ResourceNotFoundException;
import com.ecommerceapplication.ecommeceapp.repository.CartRepository;
import com.ecommerceapplication.ecommeceapp.service.CartService;
import com.ecommerceapplication.ecommeceapp.service.OrderService;
import com.ecommerceapplication.ecommeceapp.service.ProductService;
import com.ecommerceapplication.ecommeceapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartServiceImpl implements CartService {

    static final Logger LOGGER= LoggerFactory.getLogger(CartService.class);


    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;




    @Override
    public Cart addProductInCart(CartDTO cartDTO) {
        try {
            LOGGER.info("Request to add product in cart\n");
            User user = userService.getUserById(cartDTO.getUserId());
            if (!user.isSiteUser()) {
                throw new CustomException("User is not a Site_User");
            }
            Product product = productService.getByProductId(cartDTO.getProductId());
            Cart cart;
            List<Product> products;
            if (user.getCart() == null) {
                cart = new Cart();
                products = new ArrayList<Product>();
                products.add(product);
            } else {
                cart = getCartByUserId(cartDTO.getUserId());
                products = cart.getProducts();
                products.add(product);
            }
            cart.setProducts(products);
            cart.setUser(user);
            cart = cartRepo.save(cart);
            return cart;
        }
        catch(ResourceNotFoundException ex){
            LOGGER.error("Request to add product to cart unsuccessful\n");
            throw ex;
        }
        catch (Exception ex){
            LOGGER.error("Request to add product to cart unsuccessful\n");
            throw new CustomException("Unable to add product into the cart",ex);
        }
    }

    @Override
    public Cart getCartByUserId(Integer userId){
        User user=userService.getUserById(userId);
        if(user.getCart()==null){
            throw new CustomException("no products in the cart");
        }
        return cartRepo.findById(user.getCart().getCartId()).orElseThrow(()-> new ResourceNotFoundException("\nCart not found for userId - "+userId));
    }

    @Override
    public Product deleteItemFromCart(Integer userId,Integer productId) {
        try {
            Product product = productService.getByProductId(productId);
            Cart cart = this.getCartByUserId(userId);
            cart.getProducts().remove(product);
            cartRepo.save(cart);
            return product;
        }
        catch(ResourceNotFoundException ex){
            throw ex;
        }
        catch(Exception ex){
            throw new CustomException("Unable to delete product from Cart Error - ",ex);
        }

    }

}
