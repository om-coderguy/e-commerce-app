package com.ecommerceapplication.ecommeceapp.entity;

import com.ecommerceapplication.ecommeceapp.constant.SubRole;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "sub_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private SubRole role;
}
