package com.ecommerceapplication.ecommeceapp.service;

import com.ecommerceapplication.ecommeceapp.dto.SubUserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SubUserService {
    ResponseEntity<String> createSubUser(SubUserDTO request);

    ResponseEntity<String> updateSubUser(Integer subUserId, SubUserDTO request);

    ResponseEntity<String> deleteSubUser(Integer subUserId);

    ResponseEntity<List<SubUserDTO>> getAllSubUsers();
}
