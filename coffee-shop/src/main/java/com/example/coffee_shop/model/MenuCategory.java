package com.example.coffee_shop.model;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
public class MenuCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<Menu> menus;

    public MenuCategory() {}
}
