package com.example.coffee_shop.controller;

import com.example.coffee_shop.model.CoffeeShopUser;
import com.example.coffee_shop.model.CoffeeShopUserRole;
import com.example.coffee_shop.repository.UserRoleRepository;
import com.example.coffee_shop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
}
