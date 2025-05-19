package com.example.recipebook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private Context context;
    private List<Recipe> recipeList;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        holder.tvRecipeTitle.setText(recipe.getTitle());
        holder.tvRecipeCategory.setText(recipe.getCategory());
        holder.tvRecipeTime.setText(recipe.getTime());

        if (recipe.getImage() != null) {
            holder.ivRecipeImage.setImageBitmap(recipe.getImage());
        }

        holder.btnView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra("recipe_id", recipe.getId());
            context.startActivity(intent);
        });

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddRecipeActivity.class);
            intent.putExtra("recipe_id", recipe.getId());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            RecipeDBHelper dbHelper = new RecipeDBHelper(context);
            dbHelper.deleteRecipe(recipe.getId());
            recipeList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, recipeList.size());
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecipeTitle, tvRecipeCategory, tvRecipeTime;
        ImageView ivRecipeImage;
        MaterialButton btnView, btnEdit, btnDelete;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecipeTitle = itemView.findViewById(R.id.tvRecipeTitle);
            tvRecipeCategory = itemView.findViewById(R.id.tvRecipeCategory);
            tvRecipeTime = itemView.findViewById(R.id.tvRecipeTime);
            ivRecipeImage = itemView.findViewById(R.id.ivRecipeImage);
            btnView = itemView.findViewById(R.id.btnView);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}