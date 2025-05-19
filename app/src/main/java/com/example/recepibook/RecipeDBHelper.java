package com.example.recipebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class RecipeDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "RecipeBook.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_SAVED_RECIPES = "saved_recipes";

    // Common column names
    private static final String KEY_ID = "id";

    // Recipes table columns
    private static final String KEY_TITLE = "title";
    private static final String KEY_INGREDIENTS = "ingredients";
    private static final String KEY_STEPS = "steps";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_TIME = "time";

    // Saved recipes table columns
    private static final String KEY_RECIPE_ID = "recipe_id";

    // Create tables SQL
    private static final String CREATE_TABLE_RECIPES = "CREATE TABLE " + TABLE_RECIPES + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TITLE + " TEXT,"
            + KEY_INGREDIENTS + " TEXT,"
            + KEY_STEPS + " TEXT,"
            + KEY_IMAGE + " BLOB,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_TIME + " TEXT"
            + ")";

    private static final String CREATE_TABLE_SAVED_RECIPES = "CREATE TABLE " + TABLE_SAVED_RECIPES + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_RECIPE_ID + " INTEGER"
            + ")";

    public RecipeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RECIPES);
        db.execSQL(CREATE_TABLE_SAVED_RECIPES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED_RECIPES);
        onCreate(db);
    }

    // Recipe CRUD operations
    public long addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, recipe.getTitle());
        values.put(KEY_INGREDIENTS, recipe.getIngredients());
        values.put(KEY_STEPS, recipe.getSteps());
        values.put(KEY_CATEGORY, recipe.getCategory());
        values.put(KEY_TIME, recipe.getTime());

        if (recipe.getImage() != null) {
            values.put(KEY_IMAGE, getBytes(recipe.getImage()));
        }

        long id = db.insert(TABLE_RECIPES, null, values);
        db.close();
        return id;
    }

    public Recipe getRecipe(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES,
                new String[]{KEY_ID, KEY_TITLE, KEY_INGREDIENTS, KEY_STEPS, KEY_IMAGE, KEY_CATEGORY, KEY_TIME},
                KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Recipe recipe = new Recipe(
                cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_INGREDIENTS)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_STEPS)),
                getImage(cursor.getBlob(cursor.getColumnIndexOrThrow(KEY_IMAGE))),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIME))
        );

        cursor.close();
        return recipe;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RECIPES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID)));
                recipe.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)));
                recipe.setIngredients(cursor.getString(cursor.getColumnIndexOrThrow(KEY_INGREDIENTS)));
                recipe.setSteps(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STEPS)));
                recipe.setImage(getImage(cursor.getBlob(cursor.getColumnIndexOrThrow(KEY_IMAGE))));
                recipe.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)));
                recipe.setTime(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIME)));
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipes;
    }

    public int updateRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, recipe.getTitle());
        values.put(KEY_INGREDIENTS, recipe.getIngredients());
        values.put(KEY_STEPS, recipe.getSteps());
        values.put(KEY_CATEGORY, recipe.getCategory());
        values.put(KEY_TIME, recipe.getTime());

        if (recipe.getImage() != null) {
            values.put(KEY_IMAGE, getBytes(recipe.getImage()));
        }

        return db.update(TABLE_RECIPES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(recipe.getId())});
    }

    public void deleteRecipe(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPES, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // Saved recipes operations
    public void saveRecipeToCollection(long recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_RECIPE_ID, recipeId);
        db.insert(TABLE_SAVED_RECIPES, null, values);
        db.close();
    }

    public void removeRecipeFromCollection(long recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SAVED_RECIPES, KEY_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)});
        db.close();
    }

    public List<Recipe> getSavedRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        String selectQuery = "SELECT r.* FROM " + TABLE_RECIPES + " r INNER JOIN " +
                TABLE_SAVED_RECIPES + " s ON r." + KEY_ID + " = s." + KEY_RECIPE_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID)));
                recipe.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)));
                recipe.setIngredients(cursor.getString(cursor.getColumnIndexOrThrow(KEY_INGREDIENTS)));
                recipe.setSteps(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STEPS)));
                recipe.setImage(getImage(cursor.getBlob(cursor.getColumnIndexOrThrow(KEY_IMAGE))));
                recipe.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)));
                recipe.setTime(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIME)));
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipes;
    }

    public boolean isRecipeSaved(long recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SAVED_RECIPES,
                new String[]{KEY_ID},
                KEY_RECIPE_ID + "=?",
                new String[]{String.valueOf(recipeId)},
                null, null, null, null);

        boolean isSaved = cursor.getCount() > 0;
        cursor.close();
        return isSaved;
    }

    // Search operations
    public List<Recipe> searchRecipes(String query) {
        List<Recipe> recipes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RECIPES + " WHERE " +
                KEY_TITLE + " LIKE '%" + query + "%' OR " +
                KEY_INGREDIENTS + " LIKE '%" + query + "%' OR " +
                KEY_CATEGORY + " LIKE '%" + query + "%'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID)));
                recipe.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)));
                recipe.setIngredients(cursor.getString(cursor.getColumnIndexOrThrow(KEY_INGREDIENTS)));
                recipe.setSteps(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STEPS)));
                recipe.setImage(getImage(cursor.getBlob(cursor.getColumnIndexOrThrow(KEY_IMAGE))));
                recipe.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)));
                recipe.setTime(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIME)));
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipes;
    }

    // Helper methods for image conversion
    private byte[] getBytes(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    private Bitmap getImage(byte[] image) {
        if (image == null) return null;
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}