package com.example.recipebook;

import android.graphics.Bitmap;

public class Recipe {
    private long id;
    private String title;
    private String ingredients;
    private String steps;
    private Bitmap image;
    private String category;
    private String time;

    public Recipe() {
    }

    public Recipe(long id, String title, String ingredients, String steps, Bitmap image, String category, String time) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
        this.image = image;
        this.category = category;
        this.time = time;
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}