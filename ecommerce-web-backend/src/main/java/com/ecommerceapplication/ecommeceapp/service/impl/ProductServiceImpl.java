package com.ecommerceapplication.ecommeceapp.service.impl;

import com.ecommerceapplication.ecommeceapp.dto.ProductDTO;
import com.ecommerceapplication.ecommeceapp.dto.ProductInventoryDTO;
import com.ecommerceapplication.ecommeceapp.entity.*;
import com.ecommerceapplication.ecommeceapp.exception.CustomException;
import com.ecommerceapplication.ecommeceapp.exception.ResourceNotFoundException;
import com.ecommerceapplication.ecommeceapp.repository.CategoryRepository;
import com.ecommerceapplication.ecommeceapp.repository.OrderRepository;
import com.ecommerceapplication.ecommeceapp.repository.ProductRepository;
import com.ecommerceapplication.ecommeceapp.repository.SellerRepository;
import com.ecommerceapplication.ecommeceapp.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    static final Logger LOGGER= LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private CategoryRepository categoryRepository;


//

    @Override
    public Product saveProduct(ProductDTO productDTO) {
        Product product = new Product();
        try {
            Seller seller = sellerService.getSellerById(productDTO.getSellerId());
            User user = userService.getUserById(seller.getUser().getUserId());
            if(!user.isSeller()) {
                throw new CustomException("Only seller can Save and Update product");
            }
            Category category = categoryService.getCategoryById(productDTO.getCategoryId());
            product.setActive(productDTO.isActive());
            product.setSeller(seller);
            product.setCost(productDTO.getCost());
            product.setDiscount(productDTO.getDiscount());
            product.setDescr(productDTO.getDescr());
            product.setName(productDTO.getName());
            product.setCategory(category);
            product = productRepo.save(product);
        }
        catch (ResourceNotFoundException ex){
            throw ex;
        }
        catch (Exception ex){
            throw  new CustomException("Unhandled Error while saving product \n Exception = ",ex);
        }

        return product;
    }

    @Override
    public Product updateProduct(ProductDTO productDTO, int productId) {
        try {
            Product product = getByProductId(productId);
            product.setName(productDTO.getName());
            product.setCost(productDTO.getCost());
            product.setDescr(productDTO.getDescr());
            product.setDiscount(productDTO.getDiscount());

            if (productDTO.getCategoryId() != null) {
                Category category = categoryRepository.findById(productDTO.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + productDTO.getCategoryId()));
                product.setCategory(category);
            }
            return productRepo.save(product);
        }
        catch (ResourceNotFoundException ex) {
            throw ex;
        }
        catch (Exception ex){
            throw new CustomException("Unhandled exception while updating the product \n Exception = ",ex);
        }
    }

    @Override
    public void deleteProduct(int productId) throws Exception {
        try{
            productRepo.deleteById(productId);
        }
        catch (Exception ex) {
            throw new CustomException("Unable to delete the product \n Exception =",ex);
        }
    }

    @Override
    public Product getByProductId(int productId) {

        LOGGER.info("Received request to get the product");
        return productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found by Id=" + productId));
    }



    @Override
    public List<Product> getSimilarProducts(int productId) {
        Product product=this.getByProductId(productId);
        List<Product> products= productRepo.findByCategory_catId(product.getCategory().getCatId());
        return products;

    }


    @Override
    public List<ProductDTO> getTopProducts(){
        List< Product> products=  orderRepo.getTopProducts();
        List<ProductDTO> productDTO=products.stream().map(product -> ProductDTO.toDTO(product)).collect(Collectors.toList()).subList(0,5);
        return productDTO;
    }

    @Override
    public List<ProductDTO> getRecentProducts(Integer userId){
        List< Product> products=  orderRepo.getRecentProducts(userId);
//        List< Product> products=  orderRepo.getTopProducts();
        List<ProductDTO> productDTO=products.stream().map(product -> ProductDTO.toDTO(product)).collect(Collectors.toList()).subList(0,5);
        return productDTO;
    }

    @Override
    public List<Product> getAllProducts(){
        return (List<Product>) productRepo.findAll();
    }

    @Override
    public List<ProductDTO> getProductsBySellerUserId(Integer userId) {
        List<ProductDTO> products= productRepo.findBySeller_User_UserId(userId)
                .stream()
                .map(ProductDTO::toDTO)
                .collect(Collectors.toList());
        return  products;
    }

    @Override
    public List<ProductInventoryDTO> getProductsInventoryBySellerUserId(Integer userId) {
        List<Product> products = productRepo.findBySeller_User_UserId(userId);
        return products.stream()
                .map(ProductInventoryDTO::toDTO)
                .collect(Collectors.toList());
    }

}
