package com.example.recipebook;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SavedRecipesActivity extends AppCompatActivity {
    private RecyclerView recipeCollectionList;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;
    private RecipeDBHelper dbHelper;
    private TextView collectionTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipes);

        dbHelper = new RecipeDBHelper(this);

        // Initialize views
        collectionTitle = findViewById(R.id.collectionTitle);
        recipeCollectionList = findViewById(R.id.recipeCollectionList);

        // Setup RecyclerView
        recipeCollectionList.setLayoutManager(new LinearLayoutManager(this));
        recipeList = dbHelper.getSavedRecipes();
        recipeAdapter = new RecipeAdapter(this, recipeList);
        recipeCollectionList.setAdapter(recipeAdapter);

        if (recipeList.isEmpty()) {
            collectionTitle.setText("No saved recipes yet");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshSavedRecipes();
    }

    private void refreshSavedRecipes() {
        recipeList.clear();
        recipeList.addAll(dbHelper.getSavedRecipes());
        recipeAdapter.notifyDataSetChanged();

        if (recipeList.isEmpty()) {
            collectionTitle.setText("No saved recipes yet");
        } else {
            collectionTitle.setText("Your Collection");
        }
    }
}