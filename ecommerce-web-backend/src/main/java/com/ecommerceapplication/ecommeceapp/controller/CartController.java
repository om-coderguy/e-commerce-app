package com.ecommerceapplication.ecommeceapp.controller;


import com.ecommerceapplication.ecommeceapp.dto.CartDTO;
import com.ecommerceapplication.ecommeceapp.dto.ProductDTO;
import com.ecommerceapplication.ecommeceapp.entity.Cart;
import com.ecommerceapplication.ecommeceapp.entity.Product;
import com.ecommerceapplication.ecommeceapp.service.CartService;
import com.ecommerceapplication.ecommeceapp.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/carts")
@CrossOrigin
public class CartController {
    static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @PostMapping()
    public ResponseEntity<?> addProductInCart(@RequestBody CartDTO cartDTO) {
        LOGGER.info("Received request to add product into the cart. ProductId - "+cartDTO.getProductId());
        try {
            Cart cart=cartService.addProductInCart(cartDTO);
            ProductDTO productDTO=ProductDTO.toDTO(productService.getByProductId(cartDTO.getProductId()));
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        }
        catch (Exception ex) {
            LOGGER.error(" \n"+ ex.getMessage());
            return new ResponseEntity<>("" + ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductsInCart(@PathVariable("id") int userId) {
        LOGGER.info("Received request to get all products from cart");
        try {
            Cart cart=cartService.getCartByUserId(userId);
            List<ProductDTO> productList=cart.getProducts().stream().map(product->ProductDTO.toDTO(product)).collect(Collectors.toList());
            return new ResponseEntity<>(productList, HttpStatus.OK);
        }
        catch (Exception ex) {
            LOGGER.error(" \n"+ ex.getMessage());
            return new ResponseEntity<>("" + ex, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteProductFromCart(@RequestBody CartDTO cartDTO) {
        LOGGER.info("Received request to delete product from cart ProductId "+cartDTO.getProductId());
        try {
            Product product=cartService.deleteItemFromCart(cartDTO.getUserId(), cartDTO.getProductId());
            ProductDTO productDTO=ProductDTO.toDTO(product);
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        }
        catch (Exception ex) {
            LOGGER.error(" \n"+ ex.getMessage());
            return new ResponseEntity<>("" + ex, HttpStatus.BAD_REQUEST);
        }
    }

}
