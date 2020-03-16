package com.example.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDBHelper extends SQLiteOpenHelper {

    private static final int SQL_DB_SCHEMA_VERSION_NUMBER = 1;
    private static final String SQL_FILE_NAME = "recipe-ingredients.db";

    public RecipeDBHelper(Context context) {
        super(context, SQL_FILE_NAME, null, SQL_DB_SCHEMA_VERSION_NUMBER, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_STATEMENT = "CREATE TABLE " +
                RecipeContract.RecipeIngredient.TABLE_NAME + " (" +
                RecipeContract.RecipeIngredient._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeContract.RecipeIngredient.COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                RecipeContract.RecipeIngredient.COLUMN_INGREDIENT + " TEXT NOT NULL, " +
                RecipeContract.RecipeIngredient.COLUMN_MEASURING + " TEXT NOT NULL, " +
                RecipeContract.RecipeIngredient.COLUMN_QUANTITY + " FLOAT NOT NULL " +
                ");";

        db.execSQL(SQL_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeIngredient.TABLE_NAME);
        onCreate(db);
    }
}
