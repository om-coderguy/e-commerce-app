package com.ecommerceapplication.ecommeceapp.dto;

import com.ecommerceapplication.ecommeceapp.entity.Inventory;
import com.ecommerceapplication.ecommeceapp.entity.Product;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InventoryDTO {

    private Integer id;

    private Integer totalQuantity;

    private Integer productId;

    public static InventoryDTO toDTO(Inventory inventory) {
        return new InventoryDTO(inventory.getId(),inventory.getTotalQuantity(),inventory.getProduct().getId());
    }

    public static Inventory toInventory(InventoryDTO inventoryDTO,Product product) {
        return new Inventory(inventoryDTO.getId(),inventoryDTO.getTotalQuantity(),product);
    }
}
