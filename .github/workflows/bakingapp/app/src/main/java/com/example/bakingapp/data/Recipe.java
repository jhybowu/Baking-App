package com.example.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable{
    private int id;
    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<RecipeStep> steps;
    private int servings;
    private String image;

    public String getName() {
        return this.name;
    }

    public ArrayList<RecipeStep> getSteps() {
        return steps;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public RecipeStep getStep(int position) {
        if (position < 0 || this.steps == null || position >= this.steps.size()) {
            return null;
        }
        else {
            return this.steps.get(position);
        }
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    private Recipe(Parcel src) {
        if (src != null) {
            this.id = src.readInt();
            this.name = src.readString();
            this.ingredients = src.createTypedArrayList(Ingredient.CREATOR);
            this.steps = src.createTypedArrayList(RecipeStep.CREATOR);
            this.servings = src.readInt();
            this.image = src.readString();
        }
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            if (size < 1) {
                return new Recipe[0];
            }
            else {
                return new Recipe[size];
            }
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (dest != null) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
            dest.writeTypedList(this.ingredients);
            dest.writeTypedList(this.steps);
            dest.writeInt(this.servings);
            dest.writeString(this.image);
        }
    }
}
