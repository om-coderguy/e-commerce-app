package com.ecommerceapplication.ecommeceapp.controller;

import com.ecommerceapplication.ecommeceapp.dto.SubUserDTO;
import com.ecommerceapplication.ecommeceapp.service.SubUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/subusers")
@CrossOrigin
public class SubUserController {

    static final Logger LOGGER = LoggerFactory.getLogger(String.valueOf(SubUserController.class));
    @Autowired
    private SubUserService subUserService;

    // Create SubUser
    @PostMapping("/create")
    public ResponseEntity<String> createSubUser(@RequestBody SubUserDTO request) {
        try {
            LOGGER.info("SubUSer registration successful");
            return subUserService.createSubUser(request);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create subuser: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Update SubUser
    @PutMapping("/{subUserId}")
    public ResponseEntity<String> updateSubUser(@PathVariable Integer subUserId, @RequestBody SubUserDTO request) {
        try {
            LOGGER.info("Subuser updated successfully");
            return subUserService.updateSubUser(subUserId, request);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update subuser: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Delete SubUser
    @DeleteMapping("/{subUserId}")
    public ResponseEntity<String> deleteSubUser(@PathVariable Integer subUserId) {
        try {
            return subUserService.deleteSubUser(subUserId);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete subuser: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/seller/{userId}")
    public ResponseEntity<?> getAllSubUsers(@PathVariable Integer userId) {
        try {
            LOGGER.info("Received request to fetch sub-users for userId: " + userId);
            return subUserService.getSubUsersBySellerUserId(userId);
        } catch (EntityNotFoundException e) {
            LOGGER.error("No sub-users found for userId: " + userId, e);
            return new ResponseEntity<>("No sub-users found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error("Error fetching sub-users for userId: " + userId, e);
            return new ResponseEntity<>("Failed to fetch sub-users", HttpStatus.BAD_REQUEST);
        }
    }
}
