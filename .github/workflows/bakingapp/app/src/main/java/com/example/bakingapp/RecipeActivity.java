package com.example.bakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bakingapp.data.Recipe;

import com.example.bakingapp.data.RecipeStep;
import com.example.bakingapp.ui.RecipeIngredientsFragment;
import com.example.bakingapp.ui.RecipeIngredientsSelectionFragment;
import com.example.bakingapp.ui.RecipeNavigationFragment;
import com.example.bakingapp.ui.RecipeStepDetailFragment;
import com.example.bakingapp.ui.RecipeStepsFragment;

public class RecipeActivity extends AppCompatActivity
        implements RecipeStepsFragment.OnClickRecipeStepListener,
        RecipeIngredientsSelectionFragment.OnClickIngredientsListener,
        View.OnClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener
{
    private Recipe mRecipe = null;
    private ActivityRecipeBinding mBinding = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe);
        mRecipe = null;

        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.key_recipe))) {
            mRecipe = savedInstanceState.getParcelable(getString(R.string.key_recipe));
        }
        else if (getIntent() != null && getIntent().hasExtra(getString(R.string.key_recipe))) {
            mRecipe = getIntent().getParcelableExtra(getString(R.string.key_recipe));
        }

        // Supporting going back to the list of recipes.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle = new Bundle();

        if (this.mRecipe != null) {
            setTitle(this.mRecipe.getName());
            bundle.putParcelableArrayList(
                    getString(R.string.key_recipe_steps),
                    this.mRecipe.getSteps());
        }

        // Populate the text of the button to be either `Start Recipe` or `Continue Recipe` based
        // on if shared preferences is set or for tablet mode select the detail.
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.shared_preferences_name), MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (getResources().getBoolean(R.bool.tablet)) {
            // Tablet mode
            String previousRecipe = sharedPreferences.getString(getString(R.string.key_recipe),
                    null);
            int recipeStep = -1;

            // First pane - ingreident and steps list
            RecipeIngredientsSelectionFragment recipeIngredientsSelectionFragment =
                    new RecipeIngredientsSelectionFragment();

            Bundle recipeStepsFragmentArgs = new Bundle();
            if (mRecipe != null) {
                recipeStepsFragmentArgs.putParcelableArrayList(getString(R.string.key_recipe_steps),
                        mRecipe.getSteps());
                if (previousRecipe != null && !previousRecipe.equals(mRecipe.getName())) {
                    sharedPreferences.edit().
                            putString(getString(R.string.key_recipe), mRecipe.getName()).
                            putInt(getString(R.string.key_recipe_step_id), -1).
                            apply();
                }
                BakingWidgetProvider.sendRefreshBoardcast(getApplicationContext());
            }

            RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
            recipeStepsFragment.setArguments(recipeStepsFragmentArgs);

            // Commit for the transaction.
            if (savedInstanceState == null) {
                fragmentManager.beginTransaction().
                        add(mBinding.flIngredients.getId(), recipeIngredientsSelectionFragment).
                        add(mBinding.flRecipeSteps.getId(), recipeStepsFragment).
                        commit();
            }

            // Second pane - details
            Bundle fragmentArgs = new Bundle();

            if (mRecipe != null && mRecipe.getName().equals(previousRecipe)) {
                // Load the correct view
                recipeStep = sharedPreferences.getInt(getString(R.string.key_recipe_step_id),
                        -1);

                if (recipeStep != -1 && savedInstanceState == null) {
                    // recipe step
                    fragmentArgs.putParcelable(getString(R.string.key_recipe_step),
                            mRecipe.getStep(recipeStep));
                    RecipeStepDetailFragment recipeStepDetailFragment =
                            new RecipeStepDetailFragment();
                    recipeStepDetailFragment.setArguments(fragmentArgs);
                    fragmentManager.beginTransaction().
                            add(mBinding.flDetails.getId(), recipeStepDetailFragment).
                            commit();
                }
                else if (savedInstanceState == null) {
                    // ingredient
                    fragmentArgs.putParcelableArrayList(getString(R.string.key_recipe_ingredients),
                            mRecipe.getIngredients());
                    RecipeIngredientsFragment recipeIngredientsFragment =
                            new RecipeIngredientsFragment();
                    recipeIngredientsFragment.setArguments(fragmentArgs);
                    fragmentManager.beginTransaction().
                            add(mBinding.flDetails.getId(), recipeIngredientsFragment).
                            commit();
                }
            }
            else if (mRecipe != null && savedInstanceState == null){
                // ingredient
                fragmentArgs.putParcelableArrayList(getString(R.string.key_recipe_ingredients),
                        mRecipe.getIngredients());
                RecipeIngredientsFragment recipeIngredientsFragment =
                        new RecipeIngredientsFragment();
                recipeIngredientsFragment.setArguments(fragmentArgs);
                fragmentManager.beginTransaction().
                        add(mBinding.flDetails.getId(), recipeIngredientsFragment).
                        commit();
            }
        }
        else {
            // Phone mode
            // Set the onclick listener to the RecipeActivity instance.
            mBinding.btnStartContinue.setOnClickListener(this);

            if (sharedPreferences.contains(getString(R.string.key_recipe)) && mRecipe != null) {
                if (sharedPreferences.getString(getString(R.string.key_recipe), "").
                        equals(mRecipe.getName()))
                {
                    mBinding.btnStartContinue.setText(getString(R.string.recipe_selection_continue_label));
                }
            }

            RecipeStepsFragment stepsFragment = new RecipeStepsFragment();
            stepsFragment.setArguments(bundle);

            RecipeIngredientsSelectionFragment recipeIngredientFragment = new RecipeIngredientsSelectionFragment();
            if (savedInstanceState == null) {
                fragmentManager.beginTransaction().
                        add(R.id.fl_ingredients, recipeIngredientFragment).
                        add(R.id.fl_recipe_steps, stepsFragment).
                        commit();
            }
        }

        fragmentManager.executePendingTransactions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mRecipe != null) {
            outState.putParcelable(getString(R.string.key_recipe), mRecipe);
        }
    }

    @Override
    public void onRecipeStepClick(RecipeStep recipeStep) {
        if (!getResources().getBoolean(R.bool.tablet)) {
            Intent intent = new Intent(this, RecipeDetailsActivity.class);
            intent.putExtra(getString(R.string.key_recipe), mRecipe);
            intent.putExtra(getString(R.string.key_recipe_step_id), recipeStep.getId());
            startActivity(intent);
        }
        else {
            // Click on the recipe step.
            FragmentManager fragmentManager = getSupportFragmentManager();

            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            Bundle stepData = new Bundle();
            stepData.putParcelable(getString(R.string.key_recipe_step), recipeStep);
            recipeStepDetailFragment.setArguments(stepData);

            fragmentManager.beginTransaction().
                    replace(mBinding.flDetails.getId(), recipeStepDetailFragment).
                    commit();

            // Save the step in shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences(
                    getString(R.string.shared_preferences_name), MODE_PRIVATE);

            sharedPreferences.edit().putInt(getString(R.string.key_recipe_step_id),
                    recipeStep.getId()).apply();
        }
    }

    @Override
    public void onIngredientsClick() {
        if (!getResources().getBoolean(R.bool.tablet)) {
            Intent intent = new Intent(this, RecipeDetailsActivity.class);
            intent.putExtra(getString(R.string.key_recipe), mRecipe);
            intent.putExtra(getString(R.string.key_recipe_step_id), -1);
            startActivity(intent);
        }
        else {
            // Click on the recipe ingredients.
            FragmentManager fragmentManager = getSupportFragmentManager();

            RecipeIngredientsFragment recipeIngredientDetailFragment = new RecipeIngredientsFragment();
            Bundle stepData = new Bundle();
            stepData.putParcelableArrayList(getString(R.string.key_recipe_ingredients),
                    mRecipe.getIngredients());
            recipeIngredientDetailFragment.setArguments(stepData);

            fragmentManager.beginTransaction().
                    replace(mBinding.flDetails.getId(), recipeIngredientDetailFragment).
                    commit();

            // Save the step in shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences(
                    getString(R.string.shared_preferences_name), MODE_PRIVATE);

            sharedPreferences.edit().putInt(getString(R.string.key_recipe_step_id), -1).apply();
        }
    }

    // Launch the actual process of wanting to bake to the recipe.
    private void startContinueBakingRecipe(int step) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(getString(R.string.key_recipe), mRecipe);
        intent.putExtra(getString(R.string.key_recipe_step_id), step);
        startActivity(intent);
    }

    private void handleClickEvent(int step) {
        // Method for starting or resuming a recipe.  Application will only support one at a time.
        // No matter what the view will check the shared preferences to determine the last recipe
        // and page navigated to.
        int recipeStep = -1;

        // if the current recipe name is the same as the one stored, use the page to go straight to
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.shared_preferences_name), MODE_PRIVATE);

        if (mRecipe == null) {
            Log.d ("RecipeActivity", "Null Recipe State Detected in onClick");
            return;
        }


        if (sharedPreferences.getString(getString(R.string.key_recipe), "").
                equals(mRecipe.getName()))
        {
            recipeStep = sharedPreferences.getInt(
                    getString(R.string.key_recipe_step_id), -1);
        }
        else {
            // Store the current recipe as the current recipe for the user.
            sharedPreferences.edit().
                    putString(getString(R.string.key_recipe), mRecipe.getName()).
                    putInt(getString(R.string.key_recipe_step_id), recipeStep).
                    apply();
            BakingWidgetProvider.sendRefreshBoardcast(getApplicationContext());
        }

        startContinueBakingRecipe(recipeStep);
    }

    @Override
    public void onClick(View v) {
        // Click for handling the start / continue button.
        handleClickEvent(-1);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key != null &&
                !key.isEmpty() &&
                !getResources().getBoolean(R.bool.tablet) &&
                mBinding != null &&
                mBinding.btnStartContinue != null)
        {
            mBinding.btnStartContinue.setText(getString(R.string.recipe_selection_continue_label));
        }
    }
}
