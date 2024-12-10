package com.example.coffee_shop.controller;

import com.example.coffee_shop.model.CoffeeShopUser;
import com.example.coffee_shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/index")
    public String mainPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String role;
        if (username == null) {
            role = "anonymous";
        }
        else {
            role = userRepository.findByUsername(username).getRole().getName().toLowerCase();
        }
        model.addAttribute("role", role);
        return "index";
    }

    @GetMapping("/permission_denied")
    public String permissionDeniedPage(){
        return "permission_denied";
    }
}
