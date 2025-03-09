package com.ecommerceapplication.ecommeceapp.controller;

import com.ecommerceapplication.ecommeceapp.dto.CategoryDTO;
import com.ecommerceapplication.ecommeceapp.dto.InventoryDTO;
import com.ecommerceapplication.ecommeceapp.dto.ProductDTO;
import com.ecommerceapplication.ecommeceapp.entity.Category;
import com.ecommerceapplication.ecommeceapp.entity.Inventory;
import com.ecommerceapplication.ecommeceapp.entity.Product;
import com.ecommerceapplication.ecommeceapp.service.InventoryService;
import com.ecommerceapplication.ecommeceapp.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventories")
@CrossOrigin
public class InventoryController {
    static final Logger LOGGER = LoggerFactory.getLogger(String.valueOf(InventoryController.class));

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductService productService;

    @PostMapping()
    public ResponseEntity<?> saveinventory(@RequestBody InventoryDTO inventoryDTO ) {
        LOGGER.info("Received request to save  the inventory");
        try {
            Product product=productService.getByProductId(inventoryDTO.getProductId());
            Inventory inventory=InventoryDTO.toInventory(inventoryDTO,product);
            inventory=inventoryService.saveInventory(inventory);
            product.setInventory(inventory);
            productService.updateProduct(ProductDTO.toDTO(product), inventoryDTO.getProductId());
            inventoryDTO=InventoryDTO.toDTO(inventory);
            LOGGER.info("Request for inventory Successful");

            return new ResponseEntity<>(inventoryDTO, HttpStatus.OK);
        }
        catch (Exception ex) {
            LOGGER.error("Unable to process request to save inventory \n"+ ex.getMessage());
            return new ResponseEntity<>("Enable to save the inventory" + ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAllInventories() {
        LOGGER.info("Received request to get all the inventories");
        List<Inventory> inventories;
        List<InventoryDTO> inventoryDTO;
        try {
            inventoryDTO = inventoryService.getAllInventory().stream().map(inve -> InventoryDTO.toDTO(inve)).collect(Collectors.toList());
            LOGGER.info("Request for inventories Successfull");
        } catch (Exception ex) {
            LOGGER.error("Unable to process request to get all inventories \n"+ ex.getMessage());
            return new ResponseEntity<>("Enable to fetch the inventories " + ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(inventoryDTO, HttpStatus.OK);
    }


}
