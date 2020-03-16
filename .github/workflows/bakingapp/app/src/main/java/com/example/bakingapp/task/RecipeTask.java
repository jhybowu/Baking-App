package com.example.bakingapp.task;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.bakingapp.BakingActivity;
import com.example.bakingapp.IdlingResource.NetworkIdlingResource;
import com.example.bakingapp.R;
import com.example.bakingapp.data.Ingredient;
import com.example.bakingapp.data.Recipe;
import com.example.bakingapp.data.RecipeContract;

import java.util.ArrayList;
import java.util.List;

public class RecipeTask extends AsyncTaskLoader<Cursor> {

    private final Bundle mArgs;
    private NetworkIdlingResource mIdlingResource;

    public RecipeTask(Context context,
                      @Nullable Bundle args,
                      @Nullable NetworkIdlingResource resource) {
        super(context);
        this.mArgs = args;
        this.mIdlingResource = resource;
    }

    @Nullable
    @Override
    public Cursor loadInBackground() {
        if (mIdlingResource != null) {
            mIdlingResource.setmIsIdle(false);
        }

        Cursor cursor = null;
        ContentResolver resolver = getContext().getContentResolver();

        if (resolver != null && mArgs != null &&
                mArgs.containsKey(getContext().getResources().getString(
                        R.string.key_recipe_task_operation)))
        {
            // Inspect the bundle of arguments to determine what type of operation is going to be
            // performed.
            int operation = mArgs.getInt(getContext().getResources().
                    getString(R.string.key_recipe_task_operation));

            switch(operation) {
                case BakingActivity.DB_QUERY_ALL_RECIPES:
                    cursor = resolver.query(RecipeContract.RecipeIngredient.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                    break;
                case BakingActivity.DB_INSERT_RECIPE_INGREDIENTS:
                    if (mArgs.containsKey(getContext().getResources().
                            getString(R.string.key_recipes)))
                    {
                        ArrayList<Recipe> recipeList = mArgs.getParcelableArrayList(
                                getContext().getResources().getString(R.string.key_recipes));

                        if (recipeList == null) {
                            break;
                        }

                        for (Recipe recipe : recipeList) {
                            for (Ingredient ingredient : recipe.getIngredients()) {
                                // Construct the content values
                                ContentValues values = new ContentValues();
                                values.put(RecipeContract.RecipeIngredient.COLUMN_RECIPE_NAME,
                                        recipe.getName());
                                values.put(RecipeContract.RecipeIngredient.COLUMN_INGREDIENT,
                                        ingredient.getIngredient());
                                values.put(RecipeContract.RecipeIngredient.COLUMN_MEASURING,
                                        ingredient.getMeasure());
                                values.put(RecipeContract.RecipeIngredient.COLUMN_QUANTITY,
                                        ingredient.getQuantity());

                                resolver.insert(RecipeContract.RecipeIngredient.CONTENT_URI, values);
                            }
                        }
                    }
                    break;
                default:
                    throw new UnsupportedOperationException(
                            "Unsupported operation for RecipeTask."
                    );
            }

        }

        return cursor;
    }
}
