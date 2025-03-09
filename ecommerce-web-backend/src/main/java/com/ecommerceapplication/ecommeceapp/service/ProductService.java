package com.ecommerceapplication.ecommeceapp.service;
import com.ecommerceapplication.ecommeceapp.dto.ProductDTO;
import com.ecommerceapplication.ecommeceapp.dto.ProductInventoryDTO;
import com.ecommerceapplication.ecommeceapp.dto.RecentProductDTO;
import com.ecommerceapplication.ecommeceapp.entity.Order;
import com.ecommerceapplication.ecommeceapp.entity.Product;

import java.util.List;

public interface ProductService {
     List<Product> getAllProducts();

     Product getByProductId(int productId);

     List<ProductDTO> getProductsBySellerUserId(Integer sellerId);

     List<ProductDTO> getTopProducts();

     List<ProductDTO> getRecentProducts(Integer userId);

     Product saveProduct(ProductDTO productDTO) throws Exception;

     Product updateProduct(ProductDTO productDTO, int productId) throws Exception;

     void deleteProduct(int productId) throws Exception;

     List<Product> getSimilarProducts(int productId);

     List<ProductInventoryDTO> getProductsInventoryBySellerUserId(Integer userId);

//     ProductDTO updateProduct(Integer productId, ProductDTO productDTO);
}
