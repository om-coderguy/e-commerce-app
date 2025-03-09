package com.ecommerceapplication.ecommeceapp.repository;
import com.ecommerceapplication.ecommeceapp.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Integer> {
    User findOneByUserName(String username);

}
