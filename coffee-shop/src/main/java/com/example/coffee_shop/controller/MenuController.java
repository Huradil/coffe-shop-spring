package com.example.coffee_shop.controller;

import com.example.coffee_shop.model.CoffeeShopUser;
import com.example.coffee_shop.model.Menu;
import com.example.coffee_shop.repository.MenuCategoryRepository;
import com.example.coffee_shop.repository.MenuRepository;
import com.example.coffee_shop.repository.UserRepository;
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
public class MenuController {
    @Autowired
    private MenuCategoryRepository menuCategoryRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/menu_create")
    public String creatMenu(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (username == null) {
            throw new RuntimeException("User not found");
        }
        CoffeeShopUser user = userRepository.findByUsername(username);
        if (!user.getRole().getName().equalsIgnoreCase("admin")){
            return "redirect:/permission_denied";
        }
        model.addAttribute("menuCategories", menuCategoryRepository.findAll());
        return "menu_create";
    }

    @PostMapping("/menu_create")
    public String createMenu(@Valid @ModelAttribute Menu menu, BindingResult result) {
        if (result.hasErrors()) {
            return "menu_create";
        }
        menuRepository.save(menu);
        return "redirect:/menu_create";
    }
}
