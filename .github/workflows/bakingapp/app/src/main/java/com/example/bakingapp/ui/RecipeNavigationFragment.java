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


public class RecipeNavigationFragment extends Fragment {

    private FragmentRecipeNavigationBinding mBinding = null;
    private NavigationOnClickListener mCallback = null;

    public RecipeNavigationFragment() {}

    public interface NavigationOnClickListener {
        void onNavigationClick(int direction);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (NavigationOnClickListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(
                    context.toString() + " must implement the NavigationOnClickListener."
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_recipe_navigation,
                container,
                false);

        if (mBinding == null) {
            return null;
        }
        else {
            mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecipeNavigationFragment.this.mCallback.onNavigationClick(-1);
                }
            });

            mBinding.ivForward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecipeNavigationFragment.this.mCallback.onNavigationClick(1);
                }
            });

            return mBinding.getRoot();
        }
    }
}
