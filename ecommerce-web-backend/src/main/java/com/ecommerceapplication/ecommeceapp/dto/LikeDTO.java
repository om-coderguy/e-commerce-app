package com.ecommerceapplication.ecommeceapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeDTO {
    private Integer productId;
    private Integer userId;
    private boolean liked;
}
