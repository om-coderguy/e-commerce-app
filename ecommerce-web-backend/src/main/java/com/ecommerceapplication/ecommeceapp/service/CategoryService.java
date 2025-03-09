package com.ecommerceapplication.ecommeceapp.service;
import com.ecommerceapplication.ecommeceapp.entity.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> getAllCategory();

    public Category getCategoryById(int categoryId);

    public Category saveCategory(Category category);


}
