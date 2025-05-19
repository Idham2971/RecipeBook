package com.example.recipebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RecipeDetailActivity extends AppCompatActivity {
    private ImageView recipeImage;
    private TextView recipeTitle, ingredientsText, stepsText;
    private FloatingActionButton saveButton;
    private RecipeDBHelper dbHelper;
    private long recipeId;
    private boolean isSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        dbHelper = new RecipeDBHelper(this);
        recipeId = getIntent().getLongExtra("recipe_id", -1);

        // Initialize views
        recipeImage = findViewById(R.id.recipeImage);
        recipeTitle = findViewById(R.id.recipeTitle);
        ingredientsText = findViewById(R.id.ingredientsText);
        stepsText = findViewById(R.id.stepsText);
        saveButton = findViewById(R.id.saveButton);

        // Load recipe data
        loadRecipeData(recipeId);

        // Check if recipe is saved
        isSaved = dbHelper.isRecipeSaved(recipeId);
        updateSaveButton();

        saveButton.setOnClickListener(v -> toggleSaveRecipe());
    }

    private void loadRecipeData(long id) {
        Recipe recipe = dbHelper.getRecipe(id);
        if (recipe != null) {
            recipeTitle.setText(recipe.getTitle());
            ingredientsText.setText(recipe.getIngredients());
            stepsText.setText(recipe.getSteps());

            if (recipe.getImage() != null) {
                recipeImage.setImageBitmap(recipe.getImage());
            }
        }
    }

    private void toggleSaveRecipe() {
        if (isSaved) {
            dbHelper.removeRecipeFromCollection(recipeId);
        } else {
            dbHelper.saveRecipeToCollection(recipeId);
        }
        isSaved = !isSaved;
        updateSaveButton();
    }

    private void updateSaveButton() {
        if (isSaved) {
            saveButton.setImageResource(R.drawable.ic_save);
        } else {
            saveButton.setImageResource(R.drawable.ic_save);
        }
    }
}