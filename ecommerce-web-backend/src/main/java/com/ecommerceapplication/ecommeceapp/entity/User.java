package com.ecommerceapplication.ecommeceapp.entity;

import com.ecommerceapplication.ecommeceapp.constant.UserType;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer userId;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "user_type")
    private UserType userType;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile_no")
    private Long mobileNo;

    @OneToOne(fetch = FetchType.LAZY,mappedBy = "user")
    private Cart cart;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
    private List<RecentProduct> recentProducts;

    public User(Integer userId, String name, String userName, String password, UserType userType, String email, Long mobileNo) {
        this.userId = userId;
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.userType = userType;
        this.email = email;
        this.mobileNo = mobileNo;
    }


//    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
//    private List<Order> order;

    public boolean isSeller() {
        return this.getUserType().equals(UserType.SELLER);
    }

    public boolean isSiteUser() {
        return this.getUserType().equals(UserType.SITE_USER);
    }

    public boolean isDelivery() {
        return this.getUserType().equals(UserType.DELIVERY);
    }

}
