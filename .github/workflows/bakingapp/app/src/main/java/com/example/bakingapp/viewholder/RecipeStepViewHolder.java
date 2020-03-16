package com.example.bakingapp.viewholder;


import androidx.annotation.NonNull;

import com.example.bakingapp.AppAdapter;
import com.example.bakingapp.data.RecipeStep;

public class RecipeStepViewHolder <D> extends AppViewHolder<D> {

    private RecipeStepItemBinding mBinding = null;

    public RecipeStepViewHolder(@NonNull RecipeStepItemBinding binding,
                                AppAdapter.AdapterOnClickListener listener) {
        super(binding.getRoot(), listener);
        mBinding = binding;
    }

    @Override
    public void bind(D data) {
        if (data != null && data.getClass().equals(RecipeStep.class)) {
            RecipeStep recipeStep = (RecipeStep) data;

            if (mBinding != null) {
                mBinding.tvRecipeStepId.setText(Integer.toString(recipeStep.getId()));
                mBinding.tvRecipeStepDescriptionBrief.setText(recipeStep.getShortDescription());
            }
        }
    }
}
