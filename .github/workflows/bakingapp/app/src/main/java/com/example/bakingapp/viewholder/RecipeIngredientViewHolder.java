package com.example.bakingapp.viewholder;


import com.example.bakingapp.AppAdapter;
import com.example.bakingapp.data.Ingredient;

public class RecipeIngredientViewHolder <D> extends AppViewHolder<D> {

    private RecipeIngredientItemBinding mBinding = null;

    public RecipeIngredientViewHolder(RecipeIngredientItemBinding binding,
                                      AppAdapter.AdapterOnClickListener listener) {
        super(binding.getRoot(), listener);
        mBinding = binding;
    }

    @Override
    public void bind(D data) {
        if (data != null && data.getClass().equals(Ingredient.class)) {
            Ingredient ingredient = (Ingredient) data;

            mBinding.tvIngredientName.setText(ingredient.getIngredient());
            mBinding.tvIngredientMeasureType.setText(ingredient.getMeasure());
            mBinding.tvIngredientQuantity.setText(Float.toString(ingredient.getQuantity()));
        }
    }
}
