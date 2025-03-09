package com.ecommerceapplication.ecommeceapp.service;

import com.ecommerceapplication.ecommeceapp.dto.SellerDTO;
import com.ecommerceapplication.ecommeceapp.entity.Seller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SellerService {
    public List<Seller> getAllSellers();

    public Seller getSellerById(int sellerId);

    public Seller saveSeller(Seller seller);

}
