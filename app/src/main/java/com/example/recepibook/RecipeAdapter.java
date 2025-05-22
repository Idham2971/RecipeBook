package com.example.recepibook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recepibook.R;
import com.example.recepibook.RecipeModel;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final Context context;
    private List<RecipeModel> recepibook;

    public RecipeAdapter(Context context, List<RecipeModel> recipeList) {
        this.context = context;
        this.recepibook = recipeList != null ? recipeList : new ArrayList<>();
    }

    public void updateData(List<RecipeModel> newRecipes) {
        this.recepibook.clear();
        if (newRecipes != null) {
            this.recepibook.addAll(newRecipes);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeModel recipe = recepibook.get(position);
        holder.bind(recipe);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra("recipe_id", recipe.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recepibook.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivImage;
        private final TextView tvTitle, tvCategory, tvTime;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivRecipeImage);
            tvTitle = itemView.findViewById(R.id.tvRecipeTitle);
            tvCategory = itemView.findViewById(R.id.tvRecipeCategory);
            tvTime = itemView.findViewById(R.id.tvRecipeTime);
        }

        public void bind(RecipeModel recipe) {
            tvTitle.setText(recipe.getTitle());
            tvCategory.setText(recipe.getCategory());
            tvTime.setText(recipe.getTime());

            if (recipe.getImage() != null && recipe.getImage().length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(
                        recipe.getImage(), 0, recipe.getImage().length);
                ivImage.setImageBitmap(bitmap);
                ivImage.setVisibility(View.VISIBLE);
            } else {
                ivImage.setVisibility(View.GONE);
            }
        }
    }
}