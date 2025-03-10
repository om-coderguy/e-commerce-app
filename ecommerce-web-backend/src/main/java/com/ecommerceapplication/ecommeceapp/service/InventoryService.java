package com.ecommerceapplication.ecommeceapp.service;


import com.ecommerceapplication.ecommeceapp.dto.InventoryDTO;
import com.ecommerceapplication.ecommeceapp.entity.Inventory;

import java.util.List;

public interface InventoryService {

    public List<Inventory> getAllInventory();

    public Inventory saveInventory(Inventory inventory);

    InventoryDTO saveInventory(InventoryDTO inventoryDTO) throws Exception;

}
