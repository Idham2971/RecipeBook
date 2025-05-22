package com.example.recepibook;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipebook.R;

import java.io.ByteArrayOutputStream;

public class AddRecipeActivity extends AppCompatActivity {

    EditText recipeNameInput, ingredientsInput, stepsInput;
    ImageView recipeImagePreview;
    Button selectImageButton, saveRecipeBtn;
    Bitmap selectedImage;
    com.example.recipebook.DBHelper dbHelper;
    static final int IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.recipebook.R.layout.activity_addrecipe);

        dbHelper = new com.example.recipebook.DBHelper(this);

        recipeNameInput = findViewById(R.id.recipeNameInput);
        ingredientsInput = findViewById(R.id.ingredientsInput);
        stepsInput = findViewById(R.id.stepsInput);
        recipeImagePreview = findViewById(R.id.recipeImagePreview);
        selectImageButton = findViewById(R.id.selectImageButton);
        saveRecipeBtn = findViewById(R.id.saveRecipeBtn);

        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGE_REQUEST);
        });

        saveRecipeBtn.setOnClickListener(v -> {
            String title = recipeNameInput.getText().toString();
            String ingredients = ingredientsInput.getText().toString();
            String steps = stepsInput.getText().toString();

            if (title.isEmpty() || ingredients.isEmpty() || steps.isEmpty() || selectedImage == null) {
                Toast.makeText(this, "Lengkapi semua data dan gambar", Toast.LENGTH_SHORT).show();
                return;
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] imageBytes = stream.toByteArray();

            boolean inserted = dbHelper.insertRecipe(title, ingredients, steps, imageBytes);
            if (inserted) {
                Toast.makeText(this, "Resep disimpan", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal menyimpan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            try {
                selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                recipeImagePreview.setImageBitmap(selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
