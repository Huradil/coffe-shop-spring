package com.example.coffee_shop.service;

import com.example.coffee_shop.dto.OrderDto;
import com.example.coffee_shop.model.*;
import com.example.coffee_shop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private OrderMenuRepository orderMenuRepository;
    @Autowired
    private UserBonusRepository userBonusRepository;

    public void createOrder(OrderDto orderDto) {
        CoffeeShopUser user = userRepository.findById(orderDto.getUserId()).orElseThrow();
        OrderStatus orderStatus = orderStatusRepository.findByName("Created");
        Order order = new Order();
        order.setCustomer(user);
        order.setStatus(orderStatus);
        orderRepository.save(order);
        Double totalPrice = 0.0;
        for (Map.Entry<Long, Integer> entry : orderDto.getMenuIds().entrySet()){
            Menu menu = menuRepository.findById(entry.getKey()).orElseThrow();
            OrderMenu orderMenu = new OrderMenu();
            orderMenu.setMenu(menu);
            orderMenu.setOrder(order);
            orderMenu.setCount(entry.getValue());
            totalPrice += menu.getCost() * entry.getValue();
            orderMenuRepository.save(orderMenu);
        }
        order.setTotal_cost(totalPrice);
        orderRepository.save(order);
        UserBonus userBonus = user.getBonus();
        userBonus.setAmount(userBonus.getAmount() + totalPrice * 0.1);
        userBonusRepository.save(userBonus);
    }
}
