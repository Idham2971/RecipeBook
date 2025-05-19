package com.example.recipebook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView popularRecipeList;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;
    private RecipeDBHelper dbHelper;
    private EditText searchEditText;
    private ImageButton micButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database helper
        dbHelper = new RecipeDBHelper(this);

        // Initialize views
        popularRecipeList = findViewById(R.id.popularRecipeList);
        searchEditText = findViewById(R.id.searchEditText);
        micButton = findViewById(R.id.micButton);

        // Setup RecyclerView
        popularRecipeList.setLayoutManager(new LinearLayoutManager(this));
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(this, recipeList);
        popularRecipeList.setAdapter(recipeAdapter);

        // Load popular recipes
        loadPopularRecipes();

        // Setup search functionality
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            performSearch();
            return true;
        });

        micButton.setOnClickListener(v -> {
            // TODO: Implement voice search
        });

        // Setup bottom navigation
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    // Already in home
                    return true;
                } else if (itemId == R.id.nav_search) {
                    performSearch();
                } else if (itemId == R.id.nav_add) {
                    startActivity(new Intent(MainActivity.this, AddRecipeActivity.class));
                } else if (itemId == R.id.nav_saved) {
                    startActivity(new Intent(MainActivity.this, SavedRecipesActivity.class));
                } else if (itemId == R.id.nav_profile) {
                    // TODO: Implement profile
                }
                return true;
            }
        });
    }

    private void loadPopularRecipes() {
        // Get all recipes from database (for demo, in real app you might want to implement popularity)
        recipeList.clear();
        recipeList.addAll(dbHelper.getAllRecipes());
        recipeAdapter.notifyDataSetChanged();
    }

    private void performSearch() {
        String query = searchEditText.getText().toString().trim();
        if (!query.isEmpty()) {
            Intent intent = new Intent(this, SearchResultActivity.class);
            intent.putExtra("query", query);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPopularRecipes();
    }
}