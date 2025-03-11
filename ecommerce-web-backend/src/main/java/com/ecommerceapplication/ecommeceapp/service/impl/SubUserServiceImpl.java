package com.ecommerceapplication.ecommeceapp.service.impl;

import com.ecommerceapplication.ecommeceapp.constant.SubRole;
import com.ecommerceapplication.ecommeceapp.constant.UserType;
import com.ecommerceapplication.ecommeceapp.dto.SubUserDTO;
import com.ecommerceapplication.ecommeceapp.entity.Seller;
import com.ecommerceapplication.ecommeceapp.entity.SubUser;
import com.ecommerceapplication.ecommeceapp.entity.User;
import com.ecommerceapplication.ecommeceapp.repository.SellerRepository;
import com.ecommerceapplication.ecommeceapp.repository.SubUserRepository;
import com.ecommerceapplication.ecommeceapp.repository.UserRepository;
import com.ecommerceapplication.ecommeceapp.service.SubUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubUserServiceImpl implements SubUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private SubUserRepository subUserRepository;

    @Override
    @Transactional
    public ResponseEntity<String> createSubUser(SubUserDTO request) {
        try {
            Seller seller = sellerRepository.findById(request.getSellerId())
                    .orElseThrow(() -> new EntityNotFoundException("Seller not found"));

            User user = new User();
            user.setUserName(request.getUsername());
            user.setEmail(request.getUsername());
            user.setName(request.getFullName());
            user.setPassword(request.getPassword()); // Encrypt in real use
            user.setUserType(UserType.SELLER);
            userRepository.save(user);

            SubUser subUser = new SubUser();
            subUser.setSeller(seller);
            subUser.setUser(user);
            subUser.setRole(SubRole.MANAGER);
            subUserRepository.save(subUser);

            return ResponseEntity.status(HttpStatus.CREATED).body("SubUser created successfully with ADMIN role under Seller ID: " + seller.getSellerId());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create SubUser: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateSubUser(Integer subUserId, SubUserDTO request) {
        try {
            SubUser subUser = subUserRepository.findById(subUserId)
                    .orElseThrow(() -> new EntityNotFoundException("SubUser not found"));

            User user = subUser.getUser();

            if (request.getUsername() != null) {
                user.setUserName(request.getUsername());
            }

            if (request.getFullName() != null) {
                user.setName(request.getFullName()); // Encrypt in real use
            }
            if (request.getPassword() != null) {
                user.setPassword(request.getPassword()); // Encrypt in real use
            }

            userRepository.save(user);
            subUserRepository.save(subUser);

            return ResponseEntity.ok("SubUser updated successfully");
        } catch (Exception e) {
            throw new RuntimeException("Failed to update SubUser: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteSubUser(Integer subUserId) {
        try {
            SubUser subUser = subUserRepository.findById(subUserId)
                    .orElseThrow(() -> new EntityNotFoundException("SubUser not found"));

            subUserRepository.delete(subUser);
            userRepository.deleteById(subUser.getUser().getUserId());

            return ResponseEntity.ok("SubUser deleted successfully");
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete SubUser: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<List<SubUserDTO>> getAllSubUsers() {
        List<SubUser> subUsers = subUserRepository.findAll();

        if (subUsers.isEmpty()) {
            throw new EntityNotFoundException("No subusers found.");
        }

        List<SubUserDTO> subUserDTOs = subUsers.stream()
                .map(subUser -> {
                    SubUserDTO subUserDTO = new SubUserDTO();
                    subUserDTO.setSellerId(subUser.getSeller().getSellerId());
                    subUserDTO.setUsername(subUser.getUser().getUserName());
                    subUserDTO.setFullName(subUser.getUser().getName());
                    subUserDTO.setSellerId(subUserDTO.getSellerId());
                    // Add other fields as needed
                    return subUserDTO;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(subUserDTOs);
    }

}
