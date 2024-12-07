package com.example.coffee_shop.repository;

import com.example.coffee_shop.model.CoffeeShopUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<CoffeeShopUser, Long> {
    CoffeeShopUser findByUsername(String username);
}
