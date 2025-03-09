package com.ecommerceapplication.ecommeceapp.service;
import com.ecommerceapplication.ecommeceapp.entity.User;
import java.util.List;
public interface UserService {
    public List<User> getAllUsers();
    public User getUserById(int userId);
    public User saveUser(User user);
    public User registerDeliveryMan(User user);
    public User updateUser(User user, int userId);
    public void deleteUser(int userId) throws Exception;
}
