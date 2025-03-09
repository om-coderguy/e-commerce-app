package com.ecommerceapplication.ecommeceapp.service.impl;

import com.ecommerceapplication.ecommeceapp.dto.InventoryDTO;
import com.ecommerceapplication.ecommeceapp.entity.Inventory;
import com.ecommerceapplication.ecommeceapp.repository.InventoryRepository;
import com.ecommerceapplication.ecommeceapp.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepo;

    @Override
    public List<Inventory> getAllInventory(){
        return (List<Inventory>) inventoryRepo.findAll();
    }

    @Override
    public Inventory saveInventory(Inventory inventory){
       return inventoryRepo.save(inventory);
    }

}
