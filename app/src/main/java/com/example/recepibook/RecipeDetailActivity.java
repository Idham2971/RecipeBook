package com.example.recepibook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipebook.R;

public class RecipeDetailActivity extends AppCompatActivity {

    ImageView recipeImage;
    TextView recipeTitle, ingredientsText, stepsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipeImage = findViewById(R.id.recipeImage);
        recipeTitle = findViewById(R.id.recipeTitle);
        ingredientsText = findViewById(R.id.ingredientsText);
        stepsText = findViewById(R.id.stepsText);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recipeTitle.setText(extras.getString("title"));
            ingredientsText.setText(extras.getString("category")); // Digunakan sebagai "bahan"
            stepsText.setText(extras.getString("time")); // Digunakan sebagai "langkah"

            byte[] imageBytes = extras.getByteArray("image");
            if (imageBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                recipeImage.setImageBitmap(bitmap);
            }
        }
    }
}
