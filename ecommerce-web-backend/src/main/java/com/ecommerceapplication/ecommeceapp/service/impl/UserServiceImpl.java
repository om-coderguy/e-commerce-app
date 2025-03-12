package com.ecommerceapplication.ecommeceapp.service.impl;

import com.ecommerceapplication.ecommeceapp.constant.UserType;
import com.ecommerceapplication.ecommeceapp.entity.User;
import com.ecommerceapplication.ecommeceapp.exception.CustomException;
import com.ecommerceapplication.ecommeceapp.exception.ResourceNotFoundException;
import com.ecommerceapplication.ecommeceapp.repository.UserRepository;
import com.ecommerceapplication.ecommeceapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    static final Logger LOGGER= LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository usersRepo;

    @Override
    public List<User> getAllUsers(){
        LOGGER.info("Trying to fetch the users from database\n");
        try {
            return (List<User>) usersRepo.findAll();
        }
        catch (ResourceNotFoundException ex){
            LOGGER.error("Fetching not completed \n Error - "+ex.getMessage());
            throw new ResourceNotFoundException("Got exception while fetching the user from database\nException = ",ex);
        }
    }

    @Override
    public User getUserById(int userId){
        LOGGER.info("Trying to fetch the user from database\n");
        return usersRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User Not Found at Id - "+userId));
    }

    @Override
    public User saveUser(User user){
        LOGGER.info("Trying to save the user in database\n");
        try {
            return usersRepo.save(user);
        }
        catch(Exception ex){
            throw new CustomException("Enable to save the product\nException = ",ex);
        }
    }

    @Override
    public User registerDeliveryMan(User user){
        LOGGER.info("Trying to register delivery man in database\n");
        try {
            user.setUserType(UserType.DELIVERY);
            return usersRepo.save(user);
        }
        catch(Exception ex){
            throw new CustomException("Enable to save the product\nException = ",ex);
        }
    }



    @Override
    public User updateUser(User updatedUser, int userId){
        try {
            User user = getUserById(userId);
            user.setUserName(updatedUser.getUserName());
            user.setName(updatedUser.getName());
            user.setUserType(updatedUser.getUserType());
            user.setPassword(updatedUser.getPassword());
            user.setEmail(updatedUser.getEmail());
            user.setMobileNo(updatedUser.getMobileNo());
            return usersRepo.save(user);
        }
        catch(ResourceNotFoundException ex){
            throw ex;
        }
        catch(Exception ex){
            throw new CustomException("Unhandled exception while updating the user at Id - " +userId+" \nException = ",ex);
        }
    }

    @Override
    public void deleteUser(int userId) throws Exception {
        try{
            usersRepo.deleteById(userId);
        }
        catch (Exception ex) {
            throw new CustomException("Exception received while deleting user at Id -"+userId,ex);
        }
    }

}

