package com.example.semantic_web_hw1.controller;

import com.example.semantic_web_hw1.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.semantic_web_hw1.model.Recipe;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.semantic_web_hw1.service.UserService;
import com.example.semantic_web_hw1.model.UserProfile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Arrays;
import java.util.List;

@Controller
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @GetMapping("/recipes")
    public String showRecipes(Model model) {
        model.addAttribute("recipes", recipeService.getAllRecipes());
        return "recipes";
    }

    @GetMapping("/add-recipe")
    public String showAddRecipeForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "add-recipe";
    }

    @PostMapping("/add-recipe")
    public String addRecipe(@ModelAttribute Recipe recipe, Model model, RedirectAttributes redirectAttributes) {
        try {
            recipeService.addRecipe(recipe);
            redirectAttributes.addFlashAttribute("successMessage", "Recipe added successfully.");
            return "redirect:/recipes";
        } catch (Exception e) {
            model.addAttribute("recipe", recipe);
            model.addAttribute("errorMessage", e.getMessage());
            return "add-recipe";
        }
    }

    @GetMapping("/recommend-by-skill")
    public String recommendBySkill(Model model) {
        UserProfile firstUser = userService.getFirstUser();

        if (firstUser == null) {
            model.addAttribute("errorMessage", "No users found in XML.");
            return "recommend-by-skill";
        }

        model.addAttribute("user", firstUser);
        model.addAttribute("recommendedRecipes",
                recipeService.getRecipesBySkillLevel(firstUser.getCookingSkillLevel()));

        return "recommend-by-skill";
    }

    @GetMapping("/recommend-by-skill-and-cuisine")
    public String recommendBySkillAndCuisine(Model model) {
        UserProfile firstUser = userService.getFirstUser();

        if (firstUser == null) {
            model.addAttribute("errorMessage", "No users found in XML.");
            return "recommend-by-skill-and-cuisine";
        }

        model.addAttribute("user", firstUser);
        model.addAttribute("recommendedRecipes",
                recipeService.getRecipesBySkillLevelAndCuisine(
                        firstUser.getCookingSkillLevel(),
                        firstUser.getPreferredCuisineType()
                ));

        return "recommend-by-skill-and-cuisine";
    }

    @GetMapping("/recipes-xsl")
    public String showRecipesWithXsl(Model model) {
        UserProfile firstUser = userService.getFirstUser();

        if (firstUser == null) {
            model.addAttribute("transformedHtml", "<h2>No users found in XML.</h2>");
            return "recipes-xsl";
        }

        String transformedHtml = recipeService.transformRecipesWithXsl(firstUser.getCookingSkillLevel());
        model.addAttribute("transformedHtml", transformedHtml);

        return "recipes-xsl";
    }

    @GetMapping("/recipe/{id}")
    public String showRecipeDetails(@PathVariable int id, Model model) {
        Recipe recipe = recipeService.getRecipeById(id);

        if (recipe == null) {
            model.addAttribute("errorMessage", "Recipe not found.");
            return "recipe-details";
        }

        model.addAttribute("recipe", recipe);
        return "recipe-details";
    }

    @GetMapping("/filter-by-cuisine")
    public String filterByCuisine(@RequestParam(required = false) String cuisine, Model model) {
        List<String> cuisineOptions = Arrays.asList(
                "Italian", "European", "Mediterranean", "Indian", "Asian",
                "Chinese", "Mexican", "Latin American", "Greek", "Japanese",
                "French", "Spanish", "American", "Fast Food", "Thai",
                "Middle Eastern", "British"
        );

        model.addAttribute("cuisineOptions", cuisineOptions);
        model.addAttribute("selectedCuisine", cuisine);

        if (cuisine != null && !cuisine.isBlank()) {
            model.addAttribute("recipes", recipeService.getRecipesByCuisineType(cuisine));
        }

        return "filter-by-cuisine";
    }
}
