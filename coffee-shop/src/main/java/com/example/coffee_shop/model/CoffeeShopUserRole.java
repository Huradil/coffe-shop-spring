package com.example.coffee_shop.model;

import lombok.Data;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
public class CoffeeShopUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = true)
    private String description;
    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    private List<CoffeeShopUser> users;

    public CoffeeShopUserRole() {}
}
