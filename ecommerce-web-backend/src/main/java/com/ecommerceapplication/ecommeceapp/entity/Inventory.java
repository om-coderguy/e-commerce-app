package com.ecommerceapplication.ecommeceapp.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @OneToOne(mappedBy = "inventory")
    private Product product;

    public Inventory(Integer totalQuantity) {
        this.totalQuantity=totalQuantity;
    }
}
