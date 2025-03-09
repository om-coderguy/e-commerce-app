package com.ecommerceapplication.ecommeceapp.repository;
import com.ecommerceapplication.ecommeceapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    List<Product> findByCategory_catId(Integer id);

    List<Product> findBySeller_User_UserId(Integer userId);

    boolean existsById(Integer productId);

    void deleteById(Integer productId);


}
