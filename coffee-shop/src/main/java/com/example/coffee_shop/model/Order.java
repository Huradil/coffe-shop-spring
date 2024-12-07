package com.example.coffee_shop.model;

import lombok.Data;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "order_table")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = true)
    private OrderStatus status;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CoffeeShopUser customer;
    private Double total_cost;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderMenu> order_menus;

    public Order() {}
}
