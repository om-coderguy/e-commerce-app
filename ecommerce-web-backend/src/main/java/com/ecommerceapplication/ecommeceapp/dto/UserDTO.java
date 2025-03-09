package com.ecommerceapplication.ecommeceapp.dto;

import com.ecommerceapplication.ecommeceapp.constant.UserType;
import com.ecommerceapplication.ecommeceapp.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
    private Integer userId;
    private String name;
    private String userName;
    private UserType userType;
    private String password;
    private String email;
    private Long mobileNo;

    public static UserDTO toDTO(User user) {

            Integer userId = user.getUserId();
            String name = user.getName();
            String userName = user.getUserName();
            UserType userType = user.getUserType();
            String password = user.getPassword();
            String email=user.getEmail();
            Long mobileNo=user.getMobileNo();

            return new UserDTO(userId, name, userName, userType, password,email,mobileNo);
    }

    public static User toUser(UserDTO userDTO) {

            Integer userId = userDTO.getUserId();
            String name = userDTO.getName();
            String userName = userDTO.getUserName();
            UserType userType = userDTO.getUserType();
            String password = userDTO.getPassword();
            String email=userDTO.getEmail();
            Long mobileNo=userDTO.getMobileNo();
            return new User(userId, name, userName, password, userType,email,mobileNo);
    }
}
