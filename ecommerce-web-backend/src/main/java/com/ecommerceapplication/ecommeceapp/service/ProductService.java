package com.ecommerceapplication.ecommeceapp.service;
import com.ecommerceapplication.ecommeceapp.dto.*;
import com.ecommerceapplication.ecommeceapp.entity.Order;
import com.ecommerceapplication.ecommeceapp.entity.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {

     Map<String, Object> addProduct(ProductDTO productDTO);
     List<Product> getAllProducts();

     Product getByProductId(int productId);

     List<ProductDTO> getProductsBySellerUserId(Integer sellerId);

     List<ProductDTO> getTopProducts();

     List<ProductDTO> getRecentProducts(Integer userId);

//     Product saveProduct(ProductDTO productDTO) throws Exception;

     Product updateProduct(ProductDTO productDTO, int productId) throws Exception;

     void deleteProduct(int productId) throws Exception;

     List<Product> getSimilarProducts(int productId);

     List<ProductInventoryDTO> getProductsInventoryBySellerUserId(Integer userId);

     Product addSpecification(Integer productId, SpecificationDTO request);

     ProductDTO processRecentProduct(RecentProductDTO recentProductDTO);

     String saveLike(LikeDTO likeDTO);

     String saveReview(ReviewDTO reviewDTO);
}
