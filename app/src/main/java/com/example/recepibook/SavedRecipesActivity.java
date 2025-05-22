package com.example.recepibook;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipebook.DBHelper;
import com.example.recipebook.R;

import java.util.ArrayList;

public class SavedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private DBHelper dbHelper;
    private ArrayList<RecipeModel> recepibook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipes);

        initializeViews();
        setupRecyclerView();
        loadRecipesFromDatabase();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recipeCollectionList);
        dbHelper = new DBHelper(this);
        recepibook = new ArrayList<>();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(this, recepibook);
        recyclerView.setAdapter(adapter);
    }

    private void loadRecipesFromDatabase() {
        try (Cursor cursor = dbHelper.getAllRecipes()) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    RecipeModel recipe = new RecipeModel(
                            cursor.getInt(0),     // id
                            cursor.getString(1),  // title
                            cursor.getString(2),  // category
                            cursor.getString(3),  // time
                            cursor.getBlob(4)     // image
                    );
                    recepibook.add(recipe);
                } while (cursor.moveToNext());

                adapter.updateData(recepibook);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle error (show Toast, etc.)
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}