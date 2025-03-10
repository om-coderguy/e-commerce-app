package com.ecommerceapplication.ecommeceapp.controller;

import com.ecommerceapplication.ecommeceapp.constant.UserType;
import com.ecommerceapplication.ecommeceapp.dto.SellerDTO;
import com.ecommerceapplication.ecommeceapp.entity.Seller;
import com.ecommerceapplication.ecommeceapp.entity.User;
import com.ecommerceapplication.ecommeceapp.service.SellerService;
import com.ecommerceapplication.ecommeceapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/sellers")
@CrossOrigin
public class SellerController {
    static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private SellerService sellerService;
    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<?> getAllSellers() {
        LOGGER.info("Received request to get all the products\n");
        List<Seller> sellers;
        List<SellerDTO> sellerDTO = null;
        try {
            sellers = sellerService.getAllSellers();
            sellerDTO = sellers.stream().map(seller -> SellerDTO.toDTO(seller)).collect(Collectors.toList());
            LOGGER.info("Get request for seller Successful\n");
        } catch (Exception ex) {
            LOGGER.error("Get request for seller Unsuccessful \n Exception =" + ex.getMessage());
            return new ResponseEntity<>("Enable to fetch the Sellers\n" + ex, HttpStatus.OK);
        }
        return new ResponseEntity<>(sellerDTO, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveSeller(@RequestBody SellerDTO sellerDTO) {
        LOGGER.info("Received request to register seller\n");

        try {
            Map<String, Object> response = sellerService.registerSeller(sellerDTO);
            LOGGER.info("Seller registration successful");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex) {
            LOGGER.error("Error in seller registration: {}", ex.getMessage());
            return new ResponseEntity<>("Error in Saving Seller info: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

//    @PostMapping("/register")
//    public ResponseEntity<?> saveSeller(@RequestBody SellerDTO sellerDTO) {
//        LOGGER.info("Received request to register seller\n");
//
//        try {
//            // Step 1: Create the user
//            User user = new User();
//            user.setUserName(sellerDTO.getUserName());
//            user.setPassword(sellerDTO.getPassword());
//            user.setName(sellerDTO.getOwnerName()); // Ensure correct field name
//            user.setMobileNo(sellerDTO.getMobileNo());
//            user.setUserType(UserType.SELLER);
//
//            user = userService.saveUser(user); // Save and retrieve the created user
//
//            // Step 2: Create Seller
//            Seller seller = SellerDTO.toSeller(sellerDTO, user);
//            seller = sellerService.saveSeller(seller);
//
//            // Step 3: Prepare Response in the desired format
//            Map<String, Object> response = Map.of(
//                    "email", seller.getUser().getUserName(),
//                    "name", seller.getUser().getName(),
//                    "userId", seller.getUser().getUserId(),
//                    "sellerId", seller.getSellerId(),
//                    "userName", seller.getUser().getUserName(),
//                    "userType", seller.getUser().getUserType().toString()
//            );
//
//            LOGGER.info("Seller registration successful");
//            return new ResponseEntity<>(response, HttpStatus.CREATED);
//
//        } catch (Exception ex) {
//            LOGGER.error("Error in seller registration: {}", ex.getMessage());
//            return new ResponseEntity<>("Error in Saving Seller info: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }

    @PostMapping("/convert-seller")
    public ResponseEntity<?> convertseller(@RequestBody SellerDTO sellerDTO) {
        LOGGER.info("Received request to save product\n");
        User user = userService.getUserById(sellerDTO.getUserId());
        Seller seller = SellerDTO.toSeller(sellerDTO,user);

        try {
            seller = sellerService.saveSeller(seller);
            LOGGER.info("Save request for product Successful");
        } catch (Exception ex) {
            LOGGER.error("Get request for product Unsuccessful \nException = ", ex.getMessage());
            return new ResponseEntity<>("Error in Saving Seller info " + ex, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }
}
