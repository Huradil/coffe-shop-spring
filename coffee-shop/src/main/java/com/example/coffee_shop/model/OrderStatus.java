package com.example.coffee_shop.model;

import lombok.Data;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "order_status")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "status", fetch = FetchType.EAGER)
    private List<Order> orders;

    public OrderStatus() {}
}
