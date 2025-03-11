package com.ecommerceapplication.ecommeceapp.dto;

import com.ecommerceapplication.ecommeceapp.entity.SubUser;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubUserDTO {

    private Integer subUserId;
    private Integer sellerId;
    private String username;
    private String fullName;
    private String password;
    private String role;

    // Converts SubUser entity to SubUserDTO
    public static SubUserDTO toDTO(SubUser subUser) {
        return new SubUserDTO(
                subUser.getId(),
                subUser.getSeller().getSellerId(),
                subUser.getUser().getUserName(),
                subUser.getRole().toString(),
                subUser.getUser().getName(),
                subUser.getUser().getPassword()
        );
    }
}
