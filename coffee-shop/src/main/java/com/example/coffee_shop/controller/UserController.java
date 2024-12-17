package com.example.coffee_shop.controller;

import com.example.coffee_shop.model.CoffeeShopUser;
import com.example.coffee_shop.model.CoffeeShopUserRole;
import com.example.coffee_shop.model.OrderStatus;
import com.example.coffee_shop.model.UserBonus;
import com.example.coffee_shop.repository.OrderRepository;
import com.example.coffee_shop.repository.OrderStatusRepository;
import com.example.coffee_shop.repository.UserRepository;
import com.example.coffee_shop.repository.UserRoleRepository;
import com.example.coffee_shop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new CoffeeShopUser());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute CoffeeShopUser user,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "register";
        }

        CoffeeShopUserRole role = userRoleRepository.findByName("Customer");
        if (role == null) {
            model.addAttribute("errors", "Role 'Customer' not found.");
            return "register";
        }

        try {
            user.setRole(role);
            userService.createUser(user);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "register";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new CoffeeShopUser());
        return "login";
    }

    @GetMapping("/user_detail")
    public String showUserDetails(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        CoffeeShopUser user = userRepository.findByUsername(username);
        UserBonus bonus = user.getBonus();
        Double bonusAmount;
        if (bonus != null) {
            bonusAmount = bonus.getAmount();
        } else {
            bonusAmount = 0.0;
        }
        OrderStatus status = orderStatusRepository.findByName("Created");
        model.addAttribute("orders", orderRepository.findByCustomerAndStatus(user, status));
        model.addAttribute("bonusAmount", bonusAmount);
        model.addAttribute("user", user);
        return "user_detail";
    }

    @PostMapping("/update_user")
    public String updateUser(@Valid @ModelAttribute CoffeeShopUser user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "user_detail";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        CoffeeShopUser currentUser = userRepository.findByUsername(username);
        try {
            currentUser.setFirst_name(user.getFirst_name());
            currentUser.setLast_name(user.getLast_name());
            currentUser.setPhone_number(user.getPhone_number());
            currentUser.setAddress(user.getAddress());
            currentUser.setUsername(user.getUsername());
            userRepository.save(currentUser);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "user_detail";
        }
        if (!username.equals(user.getUsername())){
            return "redirect:/logout";
        }
        return "redirect:/user_detail";
    }
}
