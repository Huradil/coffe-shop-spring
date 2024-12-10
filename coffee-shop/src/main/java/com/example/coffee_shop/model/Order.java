package com.example.coffee_shop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonBackReference
    private OrderStatus status;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference
    private CoffeeShopUser customer;
    private Double total_cost;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderMenu> order_menus;

    public Order() {}

    public String getMenuNames() {
        StringBuilder menuNames = new StringBuilder();
        for (OrderMenu orderMenu : order_menus) {
            if (orderMenu.getCount() != 1) {
                menuNames.append(orderMenu.getCount()).append(" x ");
            }
            menuNames.append(orderMenu.getMenu().getName()).append(", ");
        }
        if (menuNames.length() > 0) {
            menuNames.setLength(menuNames.length() - 2);
        }
        return menuNames.toString();
    }
}
