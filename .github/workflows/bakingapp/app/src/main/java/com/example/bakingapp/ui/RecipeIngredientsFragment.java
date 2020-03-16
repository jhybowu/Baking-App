package com.example.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bakingapp.R;
import com.example.bakingapp.data.Ingredient;


public class RecipeIngredientsFragment extends Fragment {

    private FragmentRecipeIngredientsBinding mBinding = null;
    private AppAdapter<Ingredient, RecipeIngredientViewHolder<Ingredient>> mAdapter = null;

    public RecipeIngredientsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_recipe_ingredients,
                container,
                false);

        if (mBinding != null) {
            // Binding is not null thus the inflate worked and can setup the recycler view now.
            mAdapter = new AppAdapter<>(R.layout.recipe_ingredient_item, null);

            mBinding.rvRecipeIngredients.setLayoutManager(
                    new LinearLayoutManager(getContext(),
                            LinearLayoutManager.VERTICAL,
                            false));
            mBinding.rvRecipeIngredients.setAdapter(mAdapter);
            mBinding.rvRecipeIngredients.setHasFixedSize(true);

            Bundle fragmentArgs = getArguments();
            if (fragmentArgs != null &&
                    fragmentArgs.containsKey(getString(R.string.key_recipe_ingredients)))
            {
                mAdapter.setData(fragmentArgs.<Ingredient>getParcelableArrayList(
                        getString(R.string.key_recipe_ingredients)));
            }

            return mBinding.getRoot();
        }
        else {
            return null;
        }
    }
}
