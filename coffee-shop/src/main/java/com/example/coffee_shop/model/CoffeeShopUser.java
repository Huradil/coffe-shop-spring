package com.example.coffee_shop.model;

import lombok.Data;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
public class CoffeeShopUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private CoffeeShopUserRole role;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = true)
    private String email;
    @Column(nullable = true)
    private String first_name;
    @Column(nullable = true)
    private String last_name;
    @Column(nullable = true)
    private String phone_number;
    @Column(nullable = true)
    private String address;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "bonus_id", nullable = true)
    private UserBonus bonus;
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private List<Order> orders;

    public CoffeeShopUser() {}
}
