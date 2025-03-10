package com.ecommerceapplication.ecommeceapp.service;

import com.ecommerceapplication.ecommeceapp.dto.SellerDTO;
import com.ecommerceapplication.ecommeceapp.entity.Seller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface SellerService {
    public List<Seller> getAllSellers();

    public Seller getSellerById(int sellerId);

    public Seller saveSeller(Seller seller);

    Map<String, Object> registerSeller(SellerDTO sellerDTO);
}
