package com.example.recipebook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class AddRecipeActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText recipeNameInput, ingredientsInput, stepsInput;
    private ImageView recipeImagePreview;
    private Button selectImageButton, saveRecipeBtn;
    private Bitmap selectedImage;
    private RecipeDBHelper dbHelper;
    private long recipeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrecipe);

        dbHelper = new RecipeDBHelper(this);

        // Initialize views
        recipeNameInput = findViewById(R.id.recipeNameInput);
        ingredientsInput = findViewById(R.id.ingredientsInput);
        stepsInput = findViewById(R.id.stepsInput);
        recipeImagePreview = findViewById(R.id.recipeImagePreview);
        selectImageButton = findViewById(R.id.selectImageButton);
        saveRecipeBtn = findViewById(R.id.saveRecipeBtn);

        // Check if we're editing an existing recipe
        if (getIntent().hasExtra("recipe_id")) {
            recipeId = getIntent().getLongExtra("recipe_id", -1);
            loadRecipeData(recipeId);
        }

        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });

        saveRecipeBtn.setOnClickListener(v -> saveRecipe());
    }

    private void loadRecipeData(long id) {
        Recipe recipe = dbHelper.getRecipe(id);
        if (recipe != null) {
            recipeNameInput.setText(recipe.getTitle());
            ingredientsInput.setText(recipe.getIngredients());
            stepsInput.setText(recipe.getSteps());
            if (recipe.getImage() != null) {
                recipeImagePreview.setImageBitmap(recipe.getImage());
                selectedImage = recipe.getImage();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                recipeImagePreview.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveRecipe() {
        String title = recipeNameInput.getText().toString().trim();
        String ingredients = ingredientsInput.getText().toString().trim();
        String steps = stepsInput.getText().toString().trim();

        if (title.isEmpty()) {
            recipeNameInput.setError("Recipe name is required");
            return;
        }

        if (ingredients.isEmpty()) {
            ingredientsInput.setError("Ingredients are required");
            return;
        }

        if (steps.isEmpty()) {
            stepsInput.setError("Steps are required");
            return;
        }

        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setIngredients(ingredients);
        recipe.setSteps(steps);
        recipe.setImage(selectedImage);
        recipe.setCategory("General"); // Default category, you can add a category selector
        recipe.setTime("30 min"); // Default time, you can add a time input

        if (recipeId == -1) {
            // Add new recipe
            long id = dbHelper.addRecipe(recipe);
            if (id != -1) {
                Toast.makeText(this, "Recipe saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to save recipe", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Update existing recipe
            recipe.setId(recipeId);
            int updated = dbHelper.updateRecipe(recipe);
            if (updated > 0) {
                Toast.makeText(this, "Recipe updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update recipe", Toast.LENGTH_SHORT).show();
            }
        }
    }
}