package com.ecommerceapplication.ecommeceapp.dto;

import com.ecommerceapplication.ecommeceapp.entity.Category;
import com.ecommerceapplication.ecommeceapp.exception.CustomException;
import com.ecommerceapplication.ecommeceapp.exception.ResourceNotFoundException;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryDTO {
    private Integer catId;
    private String catName;


    public static CategoryDTO toDTO(Category category) {

            Integer id = category.getCatId();
            String name = category.getCatName();

            return new CategoryDTO(id, name);
    }

    public static Category toCategory(CategoryDTO categoryDTO) {
        return new Category(categoryDTO.getCatName());
    }
}
