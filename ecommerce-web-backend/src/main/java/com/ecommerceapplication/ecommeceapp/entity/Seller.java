package com.ecommerceapplication.ecommeceapp.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "seller")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Seller {
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    User user;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sellerId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "gst_in")
    private String gstIn;

}
