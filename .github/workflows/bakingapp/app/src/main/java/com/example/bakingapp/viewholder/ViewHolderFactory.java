package com.example.bakingapp.viewholder;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.bakingapp.AppAdapter;
import com.example.bakingapp.R;
import com.example.bakingapp.data.Ingredient;
import com.example.bakingapp.data.Recipe;
import com.example.bakingapp.data.RecipeStep;

public class ViewHolderFactory {
    public static AppViewHolder createViewHolder(@NonNull LayoutInflater inflater,
                                                 @NonNull ViewGroup viewGroup,
                                                 int layoutID,
                                                 @NonNull AppAdapter.AdapterOnClickListener listener) {

        AppViewHolder appViewHolder = null;

        if (layoutID == R.layout.recipe_item) {
            RecipeItemBinding binding = DataBindingUtil.inflate(
                    inflater,
                    R.layout.recipe_item,
                    viewGroup,
                    false);

            appViewHolder = new com.example.bakingapp.viewholder.RecipeViewHolder<Recipe>(binding, listener);
        }
        else if (layoutID == R.layout.recipe_step_item) {
            RecipeStepItemBinding binding = DataBindingUtil.inflate(
                    inflater,
                    R.layout.recipe_step_item,
                    viewGroup,
                    false);

            appViewHolder = new com.example.bakingapp.viewholder.RecipeStepViewHolder<RecipeStep>(binding, listener);
        }
        else if (layoutID == R.layout.recipe_ingredient_item) {
            RecipeIngredientItemBinding binding = DataBindingUtil.inflate(
                    inflater,
                    R.layout.recipe_ingredient_item,
                    viewGroup,
                    false);

            appViewHolder = new RecipeIngredientViewHolder<Ingredient>(binding, listener);
        }
        else {
            throw new java.lang.UnsupportedOperationException(
                    "Un-supported view holder class exception"
            );
        }

        return appViewHolder;
    }
}
