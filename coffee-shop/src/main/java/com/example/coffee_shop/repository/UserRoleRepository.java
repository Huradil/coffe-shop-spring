package com.example.coffee_shop.repository;

import com.example.coffee_shop.model.CoffeeShopUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<CoffeeShopUserRole, Long> {
    CoffeeShopUserRole findByName(String name);
}
