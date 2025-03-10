package com.ecommerceapplication.ecommeceapp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewDTO {
    private Long id;
    private Integer productId;
    private Integer userId;
    private int rating;
    private String comment;
    private String userName;
}
