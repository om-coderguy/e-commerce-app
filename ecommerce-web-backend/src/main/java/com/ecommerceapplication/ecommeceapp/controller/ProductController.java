package com.ecommerceapplication.ecommeceapp.controller;

import com.ecommerceapplication.ecommeceapp.dto.*;
import com.ecommerceapplication.ecommeceapp.entity.*;
import com.ecommerceapplication.ecommeceapp.repository.RecentProductRepository;
import com.ecommerceapplication.ecommeceapp.repository.UserRepository;
import com.ecommerceapplication.ecommeceapp.service.CategoryService;
import com.ecommerceapplication.ecommeceapp.service.ProductService;
import com.ecommerceapplication.ecommeceapp.service.SellerService;
import com.ecommerceapplication.ecommeceapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {

    static final Logger LOGGER = LoggerFactory.getLogger(String.valueOf(ProductController.class));
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RecentProductRepository recentProductRepo;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("")
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO productDTO) {
        LOGGER.info("Received request to save product");

        try {
            Map<String, Object> response = productService.addProduct(productDTO);
            LOGGER.info("Product saved successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex) {
            LOGGER.error("Error while saving product: {}", ex.getMessage());
            return new ResponseEntity<>("Error in saving product: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable("id") int productId) {
        LOGGER.info("Received request to update product with ID: {}", productId);

        try {
            // Check if product exists before updating
            Product existingProduct = productService.getByProductId(productId);
            if (existingProduct == null) {
                LOGGER.warn("Product with ID {} not found", productId);
                return new ResponseEntity<>("Product not found with ID: " + productId, HttpStatus.NOT_FOUND);
            }

            // Update the product
            ProductDTO updatedProduct = ProductDTO.toDTO(productService.updateProduct(productDTO, productId));
            LOGGER.info("Product updated successfully for ID: {}", productId);

            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);

        } catch (IllegalArgumentException ex) {
            LOGGER.error("Invalid request data for product ID {}: {}", productId, ex.getMessage());
            return ResponseEntity.badRequest().body("Invalid request data: " + ex.getMessage());

        } catch (Exception ex) {
            LOGGER.error("Error updating product with ID {}: {}", productId, ex.getMessage());
            return new ResponseEntity<>("Internal Server Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAllProducts() {
        LOGGER.info("Received request to get all the products");
        List<Product> products;
        List<ProductDTO> productDTO;
        try {
            products = productService.getAllProducts();
            productDTO = products.stream().map(product -> ProductDTO.toDTO(product)).filter(product -> product.isActive()).collect(Collectors.toList());
            LOGGER.info("Get Request for products Successful");
        } catch (Exception ex) {
            LOGGER.error("Get Request for products Unsuccessful\n" + ex.getMessage());
            return new ResponseEntity<>("Enable to fetch the Products" + ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @PostMapping("/byid")
    public ResponseEntity<?> getProductById(@RequestBody RecentProductDTO recentProductDTO) {
        try {
            ProductDTO productDTO = productService.processRecentProduct(recentProductDTO);
            LOGGER.info("Request for product Successful " + recentProductDTO.getProductId());
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Request for product Unsuccessful " + recentProductDTO.getProductId() + "\n Exception = " + ex.getMessage());
            return new ResponseEntity<>("Unable to fetch the product for Id - " + recentProductDTO.getProductId() + " Exception: " + ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/top")
    public ResponseEntity<?> getTopProducts() {
        LOGGER.info("Received request to get top products");
        List<Product> products;
        List<ProductDTO> productDTO;
        try {
            List<ProductDTO> productDTOs = productService.getTopProducts();
            LOGGER.info(" Request for top products Successfully");
            return new ResponseEntity<>(productDTOs, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Get Request for top products Unsuccessful\n" + ex.getMessage());
            return new ResponseEntity<>("Enable to fetch the top Products" + ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/recent/{id}")
    public ResponseEntity<?> getRecentProducts(@PathVariable("id") Integer userId) {
        LOGGER.info("Received request to get recent products");
        List<Product> products;
        List<ProductDTO> productDTO;
        try {
            List<ProductDTO> productDTOs = productService.getRecentProducts(userId);
            LOGGER.info(" Request for recent products Successfully");
            return new ResponseEntity<>(productDTOs, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Get Request for recent products Unsuccessful\n" + ex.getMessage());
            return new ResponseEntity<>("Enable to fetch the recent Products" + ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/similar/{id}")
    public ResponseEntity<?> getSimilarProducts(@PathVariable("id") int productId) {
        LOGGER.info("Received request to get similar products");
//        List<Product> products;
//        List<ProductDTO> productDTO;
        try {
            List<Product> products = productService.getSimilarProducts(productId);
            List<ProductDTO> productDTOs = products.stream().map(product -> ProductDTO.toDTO(product)).collect(Collectors.toList());
//            products = productService.getTopProducts().stream(). map(id -> productService.getByProductId(id)).collect(Collectors.toList());
//            productDTO=products.stream().map(product -> ProductDTO.toDTO(product)).collect(Collectors.toList());
            LOGGER.info(" Request for similar products Successful");
            return new ResponseEntity<>(productDTOs, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Get Request for similar products Unsuccessful\n" + ex.getMessage());
            return new ResponseEntity<>("Enable to fetch the similar Products" + ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable("id") int categoryID) {
        LOGGER.info("Received request to get similar products");
        try {
            List<Product> products = productService.getSimilarProducts(categoryID);
            List<ProductDTO> productDTOs = products.stream().map(product -> ProductDTO.toDTO(product)).collect(Collectors.toList());
            LOGGER.info(" Request for similar products Successful");
            return new ResponseEntity<>(productDTOs, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Get Request for similar products Unsuccessful\n" + ex.getMessage());
            return new ResponseEntity<>("Enable to fetch the similar Products" + ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/seller/{userId}")
    public ResponseEntity<?> getProductsBySellerId(@PathVariable("userId") Integer userId) {
        LOGGER.info("Fetching products for sellers userID: {}", userId);
        try {
            List<ProductDTO> products = productService.getProductsBySellerUserId(userId);

            if (products.isEmpty()) {
                return new ResponseEntity<>("No products found for sellers userId: " + userId, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Error fetching products by sellerId: {}", userId, ex);
            return new ResponseEntity<>("Error fetching products", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") int productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (DataIntegrityViolationException ex) {
            LOGGER.error("Cannot delete product due to foreign key constraints: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cannot delete product as it is linked to other records.");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while deleting product: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error while deleting product.");
        }
    }


    @GetMapping("/seller/inventory/{userId}")
    public ResponseEntity<?> getProductsBySellerUserId(@PathVariable("userId") Integer userId) {
        LOGGER.info("Fetching products with inventory for seller userId: {}", userId);
        try {
            List<ProductInventoryDTO> products = productService.getProductsInventoryBySellerUserId(userId);
            if (products.isEmpty()) {
                return new ResponseEntity<>("No products found for seller with userId: " + userId, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Error while fetching products: {}", ex.getMessage());
            return new ResponseEntity<>("Internal Server Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{productId}/specifications")
    public ResponseEntity<?> addSpecification(
            @PathVariable Integer productId,
            @RequestBody SpecificationDTO request) {
        try {
            Product updatedProduct = productService.addSpecification(productId, request);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/like")
    public ResponseEntity<String> saveLike(@RequestBody LikeDTO likeDTO) {
        String response = productService.saveLike(likeDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/review")
    public ResponseEntity<String> saveReview(@RequestBody ReviewDTO reviewDTO) {
        String response = productService.saveReview(reviewDTO);
        return ResponseEntity.ok(response);
    }

}
