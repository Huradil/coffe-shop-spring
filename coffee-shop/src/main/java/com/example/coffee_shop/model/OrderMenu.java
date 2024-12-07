package com.example.coffee_shop.model;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
@Table(name = "order_menu")
public class OrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer count;
    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public OrderMenu() {}
}
