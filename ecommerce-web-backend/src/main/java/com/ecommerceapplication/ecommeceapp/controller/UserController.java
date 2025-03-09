package com.ecommerceapplication.ecommeceapp.controller;

import com.ecommerceapplication.ecommeceapp.constant.UserType;
import com.ecommerceapplication.ecommeceapp.dto.UserDTO;
import com.ecommerceapplication.ecommeceapp.entity.User;
import com.ecommerceapplication.ecommeceapp.exception.CustomException;
import com.ecommerceapplication.ecommeceapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/users")
public class UserController {

    static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<?> addUser(@RequestBody UserDTO userDTO) {
        LOGGER.info("Received request save user at id - " + userDTO.getUserId());
        User user;
        try {
            user = UserDTO.toUser(userDTO);
            user=userService.saveUser(user);
            LOGGER.info("User saved successfully");
        } catch (Exception ex) {
            LOGGER.error("Save request for user Unsuccessful\n" + ex.getMessage());
            return new ResponseEntity<>("Error in saving user" + ex, HttpStatus.OK);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/registerdelivery")
    public ResponseEntity<?> registerDelivery(@RequestBody UserDTO userDTO) {
        LOGGER.info("Received request register user at id - " + userDTO.getUserId());
        User user;
        try {
            user = UserDTO.toUser(userDTO);
            user=userService.registerDeliveryMan(user);
            LOGGER.info("Delivery User registered successfully");
        } catch (Exception ex) {
            LOGGER.error("Request to register delivery man Unsuccessful\n" + ex.getMessage());
            return new ResponseEntity<>("Error in registering delivery man" + ex, HttpStatus.OK);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO, @PathVariable("id") int userId) {
        LOGGER.info("Received request to update user for Id- " + userId);
        try {
            User user = UserDTO.toUser(userDTO);
            user = userService.updateUser(user, userId);
            LOGGER.info("Updated user successfully");
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Save request for user Unsuccessful\n" + ex.getMessage());
            return new ResponseEntity<>("Error in updating product" + ex, HttpStatus.OK);
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAllUsers() {
        LOGGER.info("Received request to get all the users");
        List<User> users = null;
        List<UserDTO> userDTO;
        try {
            users = userService.getAllUsers();
            userDTO = users.stream().map(user -> UserDTO.toDTO(user)).collect(Collectors.toList());
            LOGGER.info("Request for users Successfull");
        } catch (Exception ex) {
            LOGGER.error("Get request for users Unsuccessful");
            return new ResponseEntity<>("Unhandled error while fetching users" + ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") int userId) {
        LOGGER.info("Received request to get all the users");
        User user;
        UserDTO userDTO;
        try {
            user = userService.getUserById(userId);
            userDTO = UserDTO.toDTO(user);

            LOGGER.info("Request for product Successful" + userId);
        } catch (Exception ex) {
            LOGGER.error("Get request for user Unsuccessful\n" + ex.getMessage());
            return new ResponseEntity<>("Enable to fetch the Product for user ID- " + userId + ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int userId) {
        LOGGER.info("Received request to delete the user at Id - " + userId);
        User product = null;
        try {
            userService.deleteUser(userId);
            LOGGER.info("Deleted user successfully");
        } catch (Exception ex) {
            LOGGER.error("Delete request for user Unsuccessful\n" + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpeted error while deleting product");
        }
        return ResponseEntity.ok("Success");
    }
}
