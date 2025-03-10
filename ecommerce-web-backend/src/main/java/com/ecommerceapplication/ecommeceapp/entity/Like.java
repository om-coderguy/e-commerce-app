package com.ecommerceapplication.ecommeceapp.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "product_likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "liked", nullable = false)
    private boolean liked;
}

