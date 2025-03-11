package com.ecommerceapplication.ecommeceapp.controller;

import com.ecommerceapplication.ecommeceapp.dto.SubUserDTO;
import com.ecommerceapplication.ecommeceapp.service.SubUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subusers")
public class SubUserController {

    @Autowired
    private SubUserService subUserService;

    // Create SubUser
    @PostMapping("/create")
    public ResponseEntity<String> createSubUser(@RequestBody SubUserDTO request) {
        try {
            return subUserService.createSubUser(request);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create subuser: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Update SubUser
    @PutMapping("/{subUserId}")
    public ResponseEntity<String> updateSubUser(@PathVariable Integer subUserId, @RequestBody SubUserDTO request) {
        try {
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

    @GetMapping("/")
    public ResponseEntity<?> getAllSubUsers() {
        try {
            return subUserService.getAllSubUsers();
        } catch (Exception e) {
            return new ResponseEntity<>("Failed fetch subusers", HttpStatus.BAD_REQUEST);
        }

    }
}
