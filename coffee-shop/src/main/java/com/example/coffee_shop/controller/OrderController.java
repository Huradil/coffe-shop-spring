package com.example.coffee_shop.controller;

import com.example.coffee_shop.dto.OrderDto;
import com.example.coffee_shop.model.*;
import com.example.coffee_shop.repository.MenuCategoryRepository;
import com.example.coffee_shop.repository.OrderRepository;
import com.example.coffee_shop.repository.OrderStatusRepository;
import com.example.coffee_shop.repository.UserRepository;
import com.example.coffee_shop.service.OrderService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Autowired
    private MenuCategoryRepository menuCategoryRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @GetMapping("/order")
    public String oderCreateForm(Model model){
        List<MenuCategory> categories = menuCategoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "order_create";
    }

    @PostMapping("/order_create")
    public String orderCreate(@RequestParam("menuIds") String menuData){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            if (username == null) {
                throw new RuntimeException("User not found");
            }
            Long userId = userRepository.findByUsername(username).getId();
            Map<Long, Map<String, Integer>> tempMenuData = objectMapper.readValue(menuData, new TypeReference<Map<Long, Map<String, Integer>>>() {});

            Map<Long, Integer> menuIds = new HashMap<>();
            for (Map.Entry<Long, Map<String, Integer>> entry : tempMenuData.entrySet()) {
                Long menuId = entry.getKey();
                Integer count = entry.getValue().get("count");
                menuIds.put(menuId, count);
            }

            OrderDto orderDto = new OrderDto();
            orderDto.setUserId(userId);
            orderDto.setMenuIds(menuIds);
            orderService.createOrder(orderDto);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/order";
        }
        return "redirect:/order";
    }


    @GetMapping("/menus")
    @ResponseBody
    public List<Menu> getMenuByCategory(@RequestParam Long categoryId){
        MenuCategory category = menuCategoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
        return category.getMenus();
    }

    @GetMapping("/order_list")
    public String orderList(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (username == null) {
            throw new RuntimeException("User not found");
        }
        CoffeeShopUser user = userRepository.findByUsername(username);
        if (!user.getRole().getName().equalsIgnoreCase("admin")){
            return "redirect:/permission_denied";
        }
        model.addAttribute("orders", orderRepository.findAll());
        model.addAttribute("statuses", orderStatusRepository.findAll());
        return "order_list";
    }


    @PostMapping("/admin/order/status_change")
    public String orderStatusChange(@RequestParam("orderId") Long orderId, @RequestParam("statusName") String statusName) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        OrderStatus status = orderStatusRepository.findByName(statusName);
        order.setStatus(status);
        orderRepository.save(order);
        return "redirect:/order_list";
    }
}
