package com.example.semantic_web_hw1.model;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Recipe {
    private int id;
    private String title;
    private List<String> cuisineTypes;
    private List<String> difficultyLevels;
    private String description;
    private String cuisineInput;
    private String difficultyInput;

    public Recipe() {
    }

    public Recipe(int id, String title, List<String> cuisineTypes, List<String> difficultyLevels, String description) {
        this.id = id;
        this.title = title;
        this.cuisineTypes = cuisineTypes;
        this.difficultyLevels = difficultyLevels;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getCuisineTypes() {
        return cuisineTypes;
    }

    public void setCuisineTypes(List<String> cuisineTypes) {
        this.cuisineTypes = cuisineTypes;
    }

    public List<String> getDifficultyLevels() {
        return difficultyLevels;
    }

    public void setDifficultyLevels(List<String> difficultyLevels) {
        this.difficultyLevels = difficultyLevels;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCuisineInput() {
        return cuisineInput;
    }

    public void setCuisineInput(String cuisineInput) {
        this.cuisineInput = cuisineInput;
    }

    public String getDifficultyInput() {
        return difficultyInput;
    }

    public void setDifficultyInput(String difficultyInput) {
        this.difficultyInput = difficultyInput;
    }
}