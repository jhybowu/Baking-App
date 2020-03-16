package com.example.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeContract {

    public static final String CONTENT_AUTHORITY = "com.example.bakingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_RECIPE_INGREDIENTS = RecipeIngredient.TABLE_NAME;

    public static final class RecipeIngredient implements BaseColumns {
        public static final Uri CONTENT_URI = RecipeContract.BASE_CONTENT_URI.
                buildUpon().
                appendPath(RecipeIngredient.TABLE_NAME).
                build();

        public static final String TABLE_NAME = "Recipe_Ingredients";
        public static final String COLUMN_RECIPE_NAME = "Recipe";
        public static final String COLUMN_INGREDIENT = "Ingredient";
        public static final String COLUMN_MEASURING = "Measurement";
        public static final String COLUMN_QUANTITY = "Quantity";
    }
}
