package com.ecommerceapplication.ecommeceapp.dto;

import com.ecommerceapplication.ecommeceapp.entity.Category;
import com.ecommerceapplication.ecommeceapp.entity.Product;
import com.ecommerceapplication.ecommeceapp.entity.Seller;
import com.ecommerceapplication.ecommeceapp.entity.User;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDTO {
    private Integer id;
    private String name;
    private String descr;
    private float cost;
    private float discount;
    private boolean active;
    private Integer sellerId;
    private Integer categoryId;
    private Map<String, String> specifications;

    // Seller User Info
    private String userName;
    private String password;
    private String email;
    private Long mobileNo;
    private Integer userId;
    private String userType;
    private String photo;
    private boolean liked;

    /**
     * Convert Product entity to ProductDTO including seller user details
     */
    public static ProductDTO toDTO(Product product) {
        Seller seller = product.getSeller();
        User user = seller.getUser();

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescr(),
                product.getCost(),
                product.getDiscount(),
                product.isActive(),
                seller.getSellerId(),
                product.getCategory().getCatId(),
                product.getSpecifications(),
                user.getUserName(),
                user.getPassword(),
                user.getEmail(),
                user.getMobileNo(),
                user.getUserId(),
                user.getUserType().toString(),
                product.getPhoto(),
                false
        );
    }

    public static ProductDTO toDTO(Product product,boolean liked) {
        Seller seller = product.getSeller();
        User user = seller.getUser();

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescr(),
                product.getCost(),
                product.getDiscount(),
                product.isActive(),
                seller.getSellerId(),
                product.getCategory().getCatId(),
                product.getSpecifications(),
                user.getUserName(),
                user.getPassword(),
                user.getEmail(),
                user.getMobileNo(),
                user.getUserId(),
                user.getUserType().toString(),
                product.getPhoto(),
                liked
        );
    }

    /**
     * Convert ProductDTO to Product entity
     */
    public static Product toEntity(ProductDTO productDTO, Seller seller, Category category) {
        return new Product(
                productDTO.getId(),
                productDTO.getName(),
                productDTO.getDescr(),
                productDTO.getCost(),
                productDTO.getDiscount(),
                productDTO.isActive(),
                productDTO.getPhoto(),
                seller,
                category,
                null,
                productDTO.getSpecifications()
        );
    }
}
