package com.ecommerceapplication.ecommeceapp.service;


import com.ecommerceapplication.ecommeceapp.entity.Inventory;

import java.util.List;

public interface InventoryService {

    public List<Inventory> getAllInventory();

    public Inventory saveInventory(Inventory inventory);

}
