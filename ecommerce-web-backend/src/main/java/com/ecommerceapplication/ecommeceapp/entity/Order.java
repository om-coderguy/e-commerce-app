package com.ecommerceapplication.ecommeceapp.entity;

import com.ecommerceapplication.ecommeceapp.constant.OrderStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_id",unique = true)
    private String orderId;//in format 'ORD:string' where 'ORD:' is starting string and 'string' is unique random string

    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "create_date")
    private Date creationDate;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "total_cost")
    private float totalCost;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Order(Integer id, Integer quantity, Product product, User user) {
        this.id = id;
        this.quantity = quantity;
        this.user = user;
        this.product = product;
    }
}
