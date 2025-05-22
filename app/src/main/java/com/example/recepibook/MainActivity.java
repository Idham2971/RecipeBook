package com.example.recepibook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipebook.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.recipebook.R.layout.activity_main);

        // Initialize UI components
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        ImageButton searchButton = findViewById(R.id.search_button);
        searchEditText = findViewById(R.id.search_edittext);

        // Set up bottom navigation
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

        // Set up search functionality
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        // Set border for EditText (using the edittext_border.xml)
        searchEditText.setBackgroundResource(R.drawable.edittext_border);
    }

    private void performSearch() {
        String searchQuery = searchEditText.getText().toString().trim();
        if (!searchQuery.isEmpty()) {
            // Perform search operation
            Toast.makeText(this, "Searching for: " + searchQuery, Toast.LENGTH_SHORT).show();
            // You can add your actual search logic here
        } else {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();

                    if (itemId == R.id.nav_home) {
                        // Already in MainActivity, just refresh if needed
                        recreate();
                        return true;
                    } else if (itemId == R.id.nav_add) {
                        startActivity(new Intent(MainActivity.this, AddRecipeActivity.class));
                        return true;
                    } else if (itemId == R.id.nav_saved) {
                        startActivity(new Intent(MainActivity.this, SavedRecipesActivity.class));
                        return true;
                    } else if (itemId == R.id.nav_search) {
                        // Focus on search field when search icon is clicked
                        searchEditText.requestFocus();
                        return true;
                    }
                    // Profile item has been removed
                    return false;
                }
            };
}