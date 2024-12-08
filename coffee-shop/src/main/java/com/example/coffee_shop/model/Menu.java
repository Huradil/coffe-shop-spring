package com.example.coffee_shop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = true)
    private String description;
    private String ingredients;
    @Column(nullable = false)
    private Double cost;
    private String size;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private MenuCategory category;
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderMenu> order_menus;

    public Menu() {}
}
