package com.example.recepibook;

import java.util.Arrays;

public class RecipeModel {
    private final int id;
    private final String title;
    private final String category;
    private final String time;
    private final byte[] image;

    public RecipeModel(int id, String title, String category, String time, byte[] image) {
        this.id = id;
        this.title = title != null ? title : "";
        this.category = category != null ? category : "";
        this.time = time != null ? time : "";
        this.image = image != null ? Arrays.copyOf(image, image.length) : new byte[0];
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getTime() {
        return time;
    }

    public byte[] getImage() {
        return Arrays.copyOf(image, image.length);
    }
}