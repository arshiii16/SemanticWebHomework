package com.example.semantic_web_hw1.controller;

import com.example.semantic_web_hw1.model.UserProfile;
import com.example.semantic_web_hw1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/add-user")
    public String showAddUserForm(Model model) {
        model.addAttribute("userProfile", new UserProfile());
        return "add-user";
    }

    @PostMapping("/add-user")
    public String addUser(@ModelAttribute UserProfile userProfile, Model model, RedirectAttributes redirectAttributes) {
        try {
            userService.addUser(userProfile);
            redirectAttributes.addFlashAttribute("successMessage", "User added successfully.");
            return "redirect:/users";
        } catch (Exception e) {
            model.addAttribute("userProfile", userProfile);
            model.addAttribute("errorMessage", e.getMessage());
            return "add-user";
        }
    }
}