package com.ecommerceapplication.ecommeceapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer catId;

    @Column(name = "name")
    private String catName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private List<Product> product;

    public Category(String catName) {
        this.catName = catName;
    }

    public Category(String catName, List<Product> product) {
        this.catName = catName;
        this.product = product;
    }
}
