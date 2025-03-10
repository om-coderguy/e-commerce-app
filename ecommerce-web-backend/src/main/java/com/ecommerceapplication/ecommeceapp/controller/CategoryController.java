package com.ecommerceapplication.ecommeceapp.controller;

import com.ecommerceapplication.ecommeceapp.dto.CategoryDTO;
import com.ecommerceapplication.ecommeceapp.entity.Category;
import com.ecommerceapplication.ecommeceapp.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @GetMapping()
    public ResponseEntity<?> getAllCategories() {
        LOGGER.info("Received request to get all the Category");
        List<Category> category;
        List<CategoryDTO> categoryDTO;
        try {
            categoryDTO = categoryService.getAllCategory().stream().map(cat -> CategoryDTO.toDTO(cat)).collect(Collectors.toList());
            LOGGER.info("Request for Category Successfull");
        } catch (Exception ex) {
            LOGGER.error("Unable to process request to get all categories \n", ex.getMessage());
            return new ResponseEntity<>("Enable to fetch the Category" + ex, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDTO categoryDTO ) {
        LOGGER.info("Received request to save  the Category");
        try {
            Category category=CategoryDTO.toCategory(categoryDTO);
            category=categoryService.saveCategory(category);
            LOGGER.info("Request for Category Successfull");
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("Unable to process request to save category \n", ex.getMessage());
            return new ResponseEntity<>("Enable to save the Category" + ex, HttpStatus.BAD_REQUEST);
        }
    }
}
