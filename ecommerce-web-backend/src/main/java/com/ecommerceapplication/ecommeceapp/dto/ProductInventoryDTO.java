package com.ecommerceapplication.ecommeceapp.dto;

import com.ecommerceapplication.ecommeceapp.entity.Product;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductInventoryDTO {

    private Integer id;
    private String name;
    private String descr;
    private float cost;
    private float discount;
    private boolean active;
    private Integer categoryId;
    private Integer totalQuantity;

    public static ProductInventoryDTO toDTO(Product product) {
        return new ProductInventoryDTO(
                product.getId(),
                product.getName(),
                product.getDescr(),
                product.getCost(),
                product.getDiscount(),
                product.isActive(),
                product.getCategory().getCatId(),
                (product.getInventory() != null) ? product.getInventory().getTotalQuantity() : 0
        );
    }
}
