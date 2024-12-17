package com.example.coffee_shop.repository;

import com.example.coffee_shop.model.CoffeeShopUser;
import com.example.coffee_shop.model.UserBonus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBonusRepository extends JpaRepository<UserBonus, Long> {
    UserBonus findByUser(CoffeeShopUser user);
}
