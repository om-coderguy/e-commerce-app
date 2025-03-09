package com.ecommerceapplication.ecommeceapp.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDTO {
    @NotBlank
    private String username;
    private String password;
}
