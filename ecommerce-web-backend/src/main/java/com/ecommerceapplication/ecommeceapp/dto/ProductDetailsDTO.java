package com.ecommerceapplication.ecommeceapp.dto;

import com.ecommerceapplication.ecommeceapp.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailsDTO {

    private Integer id;
    private String name;
    private String description;
    private float cost;
    private float discount;

    public static ProductDetailsDTO fromProduct(Product product) {
        return new ProductDetailsDTO(
                product.getId(),
                product.getName(),
                product.getDescr(),
                product.getCost(),
                product.getDiscount()
        );
    }
}
