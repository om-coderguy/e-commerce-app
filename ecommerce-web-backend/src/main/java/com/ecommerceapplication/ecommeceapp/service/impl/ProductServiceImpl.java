package com.ecommerceapplication.ecommeceapp.service.impl;

import com.ecommerceapplication.ecommeceapp.dto.*;
import com.ecommerceapplication.ecommeceapp.entity.*;
import com.ecommerceapplication.ecommeceapp.exception.CustomException;
import com.ecommerceapplication.ecommeceapp.exception.ResourceNotFoundException;
import com.ecommerceapplication.ecommeceapp.repository.*;
import com.ecommerceapplication.ecommeceapp.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private RecentProductRepository recentProductRepo;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Map<String, Object> addProduct(ProductDTO productDTO) {
        Seller seller = sellerRepository.findByUser_UserId(productDTO.getUserId());
        if (seller == null) {
            throw new RuntimeException("Seller not found with user ID: " + productDTO.getUserId());
        }
        Category category = categoryService.getCategoryById(productDTO.getCategoryId());
        if (category == null) {
            throw new RuntimeException("Category not found with ID: " + productDTO.getCategoryId());
        }

        Product product = ProductDTO.toEntity(productDTO, seller, category);

        product = productRepo.save(product);

        return prepareProductResponse(product);
    }





    //    This is response for saving product
    private Map<String, Object> prepareProductResponse(Product product) {
        return Map.of(
                "productId", product.getId(),
                "name", product.getName(),
                "description", product.getDescr(),
                "cost", product.getCost(),
                "discount", product.getDiscount(),
                "active", product.isActive(),
                "sellerId", product.getSeller().getSellerId(),
                "categoryId", product.getCategory().getCatId()
        );
    }

    @Override
    public Product updateProduct(ProductDTO productDTO, int productId) {
        try {
            Product product = getByProductId(productId);
            product.setName(productDTO.getName());
            product.setCost(productDTO.getCost());
            product.setDescr(productDTO.getDescr());
            product.setDiscount(productDTO.getDiscount());
            product.setPhoto(productDTO.getPhoto());

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
        // Convert to DTO list
        List<ProductDTO> productDTOs = products.stream()
                .map(ProductDTO::toDTO)
                .collect(Collectors.toList());

        // Ensure we only take up to 6 items
        return productDTOs.subList(0, Math.min(6, productDTOs.size()));
    }

    @Override
    public List<ProductDTO> getRecentProducts(Integer userId) {
        List<Product> products = orderRepo.getRecentProducts(userId);

        // Convert to DTO list
        List<ProductDTO> productDTOs = products.stream()
                .map(ProductDTO::toDTO)
                .collect(Collectors.toList());

        // Ensure we only take up to 5 items
        return productDTOs.subList(0, Math.min(6, productDTOs.size()));
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

    @Override
    public Product addSpecification(Integer productId, SpecificationDTO request) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        if (product.getSpecifications() == null) {
            product.setSpecifications(new HashMap<>());
        }

        product.getSpecifications().put(request.getSpecName(), request.getSpecValue());
        return productRepo.save(product);
    }

    @Override
    public ProductDTO processRecentProduct(RecentProductDTO recentProductDTO) {
        Product product = productRepo.findById(recentProductDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        User user = userService.getUserById(recentProductDTO.getUserId());
        List<RecentProduct> recentProducts = user.getRecentProducts();

        if (recentProducts.isEmpty()) {
            recentProducts = new ArrayList<>();
            addNewRecentProduct(recentProducts, product, user);
        }

        boolean isNewProduct = true;
        for (RecentProduct recentProduct : recentProducts) {
            if (recentProduct.getProduct().getId().equals(recentProductDTO.getProductId())) {
                recentProduct.setCount(recentProduct.getCount() + 1);
                recentProductRepo.save(recentProduct);
                isNewProduct = false;
                break;
            }
        }

        if (isNewProduct) {
            addNewRecentProduct(recentProducts, product, user);
        }

        return ProductDTO.toDTO(product);
    }

    private void addNewRecentProduct(List<RecentProduct> recentProducts, Product product, User user) {
        RecentProduct newProduct = new RecentProduct();
        newProduct.setProduct(product);
        newProduct.setUser(user);
        newProduct.setCount(1L);
        recentProducts.add(newProduct);
        recentProductRepo.save(newProduct);
    }

    @Override
    public String saveLike(LikeDTO likeDTO) {
        Product product = productRepo.findById(likeDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userService.getUserById(likeDTO.getUserId());

        if (likeRepository.existsByProduct_IdAndUser_UserId(likeDTO.getProductId(), likeDTO.getUserId())) {
            return "User has already liked/unliked this product";
        }

        Like like = new Like();
        like.setProduct(product);
        like.setUser(user);
        like.setLiked(likeDTO.isLiked());

        likeRepository.save(like);
        return "Like saved successfully";
    }

    @Override
    public String saveReview(ReviewDTO reviewDTO) {
        Product product = productRepo.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        User user = userService.getUserById(reviewDTO.getUserId());

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        reviewRepository.save(review);
        return "Review saved successfully";
    }

    @Override
    public List<ReviewDTO> getReviewsByProductId(Integer productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);

        return reviews.stream().map(review -> {
            ReviewDTO reviewDTO = new ReviewDTO();
            reviewDTO.setId(review.getId());              // Review ID
            reviewDTO.setUserName(review.getUser().getUserName()); // UserName
            reviewDTO.setReviewerName(review.getUser().getName());
            reviewDTO.setRating(review.getRating());      // Rating
            reviewDTO.setComment(review.getComment());    // Comment
            return reviewDTO;
        }).collect(Collectors.toList());
    }

}
