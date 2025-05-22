package com.example.recipebook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RecipeBook.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "recipes";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_CATEGORY = "category";
    public static final String COL_TIME = "time";
    public static final String COL_IMAGE = "image";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_CATEGORY + " TEXT, " +
                COL_TIME + " TEXT, " +
                COL_IMAGE + " BLOB)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertRecipe(String title, String category, String time, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, title);
        values.put(COL_CATEGORY, category);
        values.put(COL_TIME, time);
        values.put(COL_IMAGE, image);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public Cursor getAllRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public boolean deleteRecipe(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean updateRecipe(int id, String title, String category, String time, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, title);
        values.put(COL_CATEGORY, category);
        values.put(COL_TIME, time);
        values.put(COL_IMAGE, image);
        return db.update(TABLE_NAME, values, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }
}
