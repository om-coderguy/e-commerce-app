package com.ecommerceapplication.ecommeceapp.service.impl;

import com.ecommerceapplication.ecommeceapp.dto.InventoryDTO;
import com.ecommerceapplication.ecommeceapp.dto.ProductDTO;
import com.ecommerceapplication.ecommeceapp.entity.Inventory;
import com.ecommerceapplication.ecommeceapp.entity.Product;
import com.ecommerceapplication.ecommeceapp.repository.InventoryRepository;
import com.ecommerceapplication.ecommeceapp.service.InventoryService;
import com.ecommerceapplication.ecommeceapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepo;

    @Autowired
    private ProductService productService;

    @Override
    public List<Inventory> getAllInventory(){
        return (List<Inventory>) inventoryRepo.findAll();
    }

    @Override
    public Inventory saveInventory(Inventory inventory){
       return inventoryRepo.save(inventory);
    }

    @Override
    public InventoryDTO saveInventory(InventoryDTO inventoryDTO) throws Exception {
        Product product = productService.getByProductId(inventoryDTO.getProductId());
        if (product == null) {
            throw new RuntimeException("Product not found with ID: " + inventoryDTO.getProductId());
        }
        Inventory inventory = InventoryDTO.toInventory(inventoryDTO, product);
        inventory = inventoryRepo.save(inventory);

        product.setInventory(inventory);
        productService.updateProduct(ProductDTO.toDTO(product), inventoryDTO.getProductId());

        return InventoryDTO.toDTO(inventory);
    }

}
