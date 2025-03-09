package com.ecommerceapplication.ecommeceapp.service.impl;

import com.ecommerceapplication.ecommeceapp.constant.UserType;
import com.ecommerceapplication.ecommeceapp.dto.SellerDTO;
import com.ecommerceapplication.ecommeceapp.entity.Seller;
import com.ecommerceapplication.ecommeceapp.entity.User;
import com.ecommerceapplication.ecommeceapp.exception.CustomException;
import com.ecommerceapplication.ecommeceapp.exception.ResourceNotFoundException;
import com.ecommerceapplication.ecommeceapp.repository.SellerRepository;
import com.ecommerceapplication.ecommeceapp.repository.UserRepository;
import com.ecommerceapplication.ecommeceapp.service.SellerService;
import com.ecommerceapplication.ecommeceapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class SellerServiceImpl implements SellerService {
    static final Logger LOGGER= LoggerFactory.getLogger(SellerServiceImpl.class);

    @Autowired
    private SellerRepository sellerRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public Seller getSellerById(int sellerId ){
        return sellerRepo.findById(sellerId).orElseThrow(()->new ResourceNotFoundException("Seller not found at Id - "+sellerId));
    }
    public List<Seller> getAllSellers(){
        LOGGER.info("Trying to fetch the sellers from database\n");
        try {
            return (List<Seller>) sellerRepo.findAll();
        }
        catch (ResourceNotFoundException ex){
            LOGGER.error("Fetching not completed \n Error - "+ex.getMessage());
            throw ex;
        }
    }

    public Seller saveSeller(Seller seller){
        LOGGER.info("Trying to save the data to database\n");
        try{
            User user=userService.getUserById(seller.getUser().getUserId());
            user.setUserType(UserType.SELLER);
            sellerRepo.save(seller);
            LOGGER.info("Saved seller successfully\n");
//            userService.updateUser(user,userSellerDTO.getUserId());
        }
        catch (ResourceNotFoundException ex){
            LOGGER.error("Unable to save data in database - {}\n",ex.getMessage());
            throw ex;
        }
        catch (Exception ex){
            LOGGER.error("Exception got - {}",ex.getMessage());
            throw new CustomException("Unhandled exception while registering seller\n. Error-Message = "+ex.getMessage());
        }
        return seller;
    }

}
