package com.ecommerceapplication.ecommeceapp.service.impl;

import com.ecommerceapplication.ecommeceapp.entity.Category;
import com.ecommerceapplication.ecommeceapp.exception.ResourceNotFoundException;
import com.ecommerceapplication.ecommeceapp.repository.CategoryRepository;
import com.ecommerceapplication.ecommeceapp.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    static final Logger LOGGER= LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryRepository categoryRepo;

    @Override
    public List<Category> getAllCategory(){
        LOGGER.info("Received request for all categories\n");
        try {
            return (List<Category>) categoryRepo.findAll();
        }
        catch (ResourceNotFoundException ex){
            LOGGER.error("Unable to fetch categories from database\n");
            throw ex;
        }
    }

    @Override
    public Category getCategoryById(int categoryId){
        LOGGER.info("Received request for category at Id - "+categoryId);
        return categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("\nCategory not found at Id - "+categoryId));
    }

    @Override
    public Category saveCategory(Category category){
        try{
            return categoryRepo.save(category);
        }
        catch (ResourceNotFoundException ex){
            LOGGER.error("Unable to save category to database\n");
            throw ex;
        }
    }

}
