package com.example.bakingapp.viewholder;

import com.example.bakingapp.AppAdapter;
import com.example.bakingapp.R;
import com.example.bakingapp.data.Recipe;
import com.squareup.picasso.Picasso;

public class RecipeViewHolder<D> extends AppViewHolder<D> {
    private final RecipeItemBinding BINDING;
    private static final String DEFAULT_PICTURE_PATH = "file//ic_cake_black_40dp.xml";

    public RecipeViewHolder(RecipeItemBinding binding, AppAdapter.AdapterOnClickListener listener) {
        super(binding.getRoot(), listener);
        BINDING = binding;
    }

    @Override
    public void bind(D data) {
        if (data.getClass().equals(Recipe.class)) {
            Recipe recipe = (Recipe) data;
            BINDING.tvRecipeItemName.setText(recipe.getName());

            if (recipe.getImage() == null || recipe.getImage().isEmpty()) {
                Picasso.get().load(DEFAULT_PICTURE_PATH).
                        error(R.drawable.ic_cake_black_40dp).
                        into(BINDING.ivRecipeItemImage);
            }
            else {
                Picasso.get().load(recipe.getImage()).
                        error(R.drawable.ic_cake_black_40dp).
                        into(BINDING.ivRecipeItemImage);
            }

            BINDING.tvRecipeServingSize.setText(Integer.toString(recipe.getServings()));
        }
    }
}
