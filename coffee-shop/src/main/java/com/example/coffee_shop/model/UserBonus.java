package com.example.coffee_shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
@Table(name = "user_bonus")
public class UserBonus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name="user_id", nullable = false)
    private CoffeeShopUser user;
    private Double amount;

    public UserBonus() {}

    @PrePersist
    public void setDefaultBonus() {
        this.amount = 0.0;
    }
}
