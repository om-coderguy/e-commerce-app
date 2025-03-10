package com.ecommerceapplication.ecommeceapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private Integer productId;
    private Integer userId;
    private int rating;
    private String comment;
}
