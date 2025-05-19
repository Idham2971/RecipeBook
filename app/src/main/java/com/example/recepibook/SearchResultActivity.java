package com.example.recipebook;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
    private RecyclerView searchResultList;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;
    private RecipeDBHelper dbHelper;
    private TextView searchTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        dbHelper = new RecipeDBHelper(this);
        String query = getIntent().getStringExtra("query");

        // Initialize views
        searchTitle = findViewById(R.id.searchTitle);
        searchResultList = findViewById(R.id.searchResultList);

        // Setup RecyclerView
        searchResultList.setLayoutManager(new LinearLayoutManager(this));
        recipeList = dbHelper.searchRecipes(query);
        recipeAdapter = new RecipeAdapter(this, recipeList);
        searchResultList.setAdapter(recipeAdapter);

        searchTitle.setText("Search results for: " + query);
    }
}