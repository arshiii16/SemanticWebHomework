package com.example.semantic_web_hw1.controller;

import com.example.semantic_web_hw1.service.RecipeService;
import com.example.semantic_web_hw1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("recipeCount", recipeService.getAllRecipes().size());
        model.addAttribute("userCount", userService.getAllUsers().size());
        model.addAttribute("firstUser", userService.getFirstUser());
        return "index";
    }
}