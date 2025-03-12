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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(SubUserServiceImpl.class);

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
            LOGGER.info("Creating sub-user for seller ID: {}", request.getSellerId());

            Seller seller = sellerRepository.findByUser_UserId(request.getSellerId());

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
            subUser.setRole(SubRole.valueOf(request.getRole()));
            subUserRepository.save(subUser);

            LOGGER.info("Sub-user created successfully under Seller ID: {}", seller.getSellerId());
            return ResponseEntity.status(HttpStatus.CREATED).body("SubUser created successfully with ADMIN role under Seller ID: " + seller.getSellerId());
        } catch (Exception e) {
            LOGGER.error("Failed to create sub-user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create SubUser: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateSubUser(Integer subUserId, SubUserDTO request) {
        try {
            LOGGER.info("Updating sub-user with ID: {}", subUserId);

            SubUser subUser = subUserRepository.findById(subUserId)
                    .orElseThrow(() -> new EntityNotFoundException("SubUser not found"));

            User user = subUser.getUser();

            if (request.getUsername() != null) {
                user.setUserName(request.getUsername());
            }

            if (request.getFullName() != null) {
                user.setName(request.getFullName());
            }

            if (request.getPassword() != null) {
                user.setPassword(request.getPassword());
            }

            userRepository.save(user);
            subUserRepository.save(subUser);

            LOGGER.info("Sub-user updated successfully with ID: {}", subUserId);
            return ResponseEntity.ok("SubUser updated successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to update sub-user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update SubUser: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteSubUser(Integer subUserId) {
        try {
            LOGGER.info("Deleting sub-user with ID: {}", subUserId);

            SubUser subUser = subUserRepository.findById(subUserId)
                    .orElseThrow(() -> new EntityNotFoundException("SubUser not found"));

            subUserRepository.delete(subUser);
            userRepository.deleteById(subUser.getUser().getUserId());

            LOGGER.info("Sub-user deleted successfully with ID: {}", subUserId);
            return ResponseEntity.ok("SubUser deleted successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to delete sub-user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete SubUser: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getSubUsersBySellerUserId(Integer userId) {
        try {
            LOGGER.info("Fetching sub-users for seller with userId: {}", userId);

            Seller seller = sellerRepository.findByUser_UserId(userId);
            if (seller == null) {
                throw new EntityNotFoundException("Seller not found with userId: " + userId);
            }

            List<SubUser> subUsers = subUserRepository.findBySeller(seller);

            if (subUsers.isEmpty()) {
                LOGGER.warn("No sub-users found for seller with userId: {}", userId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No sub-users found for seller with userId: {}");
            }

            List<SubUserDTO> subUserDTOs = subUsers.stream()
                    .map(this::mapToSubUserDTO)
                    .collect(Collectors.toList());

            LOGGER.info("Successfully fetched {} sub-users for seller with userId: {}", subUserDTOs.size(), userId);
            return ResponseEntity.ok(subUserDTOs);
        } catch (Exception e) {
            LOGGER.error("Failed to fetch sub-users: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch sub-users: " + e.getMessage());
        }
    }

    private SubUserDTO mapToSubUserDTO(SubUser subUser) {
        SubUserDTO subUserDTO = new SubUserDTO();
        subUserDTO.setSubUserId(subUser.getId());
        subUserDTO.setSellerId(subUser.getSeller().getSellerId());
        subUserDTO.setUsername(subUser.getUser().getUserName());
        subUserDTO.setFullName(subUser.getUser().getName());
        subUserDTO.setRole(subUser.getRole().toString());
        return subUserDTO;
    }
}