package com.ecommerceapplication.ecommeceapp.controller;

import com.ecommerceapplication.ecommeceapp.dto.LoginDTO;
import com.ecommerceapplication.ecommeceapp.dto.UserDTO;
import com.ecommerceapplication.ecommeceapp.entity.User;
import com.ecommerceapplication.ecommeceapp.exception.ResourceNotFoundException;
import com.ecommerceapplication.ecommeceapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private UserRepository userRepo;

    @PostMapping("login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginRequest) {
        logger.info("Authentication controller {} " + loginRequest);
        try {
            User user = userRepo.findOneByUserName(loginRequest.getUsername());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No User Found");
            }
            if (user.getPassword().equals(loginRequest.getPassword())) {
                UserDTO userDTO = UserDTO.toDTO(user);
                return ResponseEntity.ok(userDTO);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Authentication Unsuccessful");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<>("Error in validating " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
