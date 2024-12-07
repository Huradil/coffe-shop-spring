package com.example.coffee_shop.model;

import lombok.Data;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(nullable = true)
    private String description;
    private String ingredients;
    private Double cost;
    private String size;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private MenuCategory category;
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    private List<OrderMenu> order_menus;

    public Menu() {}
}
