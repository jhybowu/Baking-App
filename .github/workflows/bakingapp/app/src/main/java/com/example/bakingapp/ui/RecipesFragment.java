package com.example.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bakingapp.AppAdapter;
import com.example.bakingapp.R;
import com.example.bakingapp.data.Recipe;

import java.util.ArrayList;

public class RecipesFragment extends Fragment
        implements AppAdapter.AdapterOnClickListener {

    private FragmentRecipesBinding mBinding = null;
    private AppAdapter<Recipe, RecipeViewHolder<Recipe>> mAdapter = null;
    private OnClickRecipeListener mCallback = null;

    public interface OnClickRecipeListener {
        void onClickRecipe(Recipe recipe);
    }

    public RecipesFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnClickRecipeListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(
                    context.toString() + " must implement OnClickRecipeListener."
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_recipes,
                container,
                false);

        ArrayList<Recipe> recipes = null;

        if (getArguments() != null && getArguments().containsKey(getString(R.string.key_recipes))) {
            recipes = getArguments().getParcelableArrayList(getString(R.string.key_recipes));
        }

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(),
                        LinearLayoutManager.VERTICAL,
                        false);

        mAdapter = new AppAdapter<>(R.layout.recipe_item, this);

        mBinding.rvRecipes.setAdapter(mAdapter);
        mBinding.rvRecipes.setLayoutManager(layoutManager);
        mBinding.rvRecipes.setHasFixedSize(true);

        mAdapter.setData(recipes);

        return mBinding.getRoot();
    }

    // This onClick is provided to the recycler view view handler objects.
    @Override
    public void onClick(int position) {
        Recipe recipe = mAdapter.get(position);

        if (recipe != null && mCallback != null) {
            // Calls the fragment's instance of the host provided callback.
            mCallback.onClickRecipe(recipe);
        }
    }
}
