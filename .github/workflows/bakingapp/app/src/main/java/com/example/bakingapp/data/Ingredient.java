package com.example.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    private float quantity;
    private String measure;
    private String ingredient;

    public float getQuantity() {
        return quantity;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getMeasure() {
        return measure;
    }

    private Ingredient(Parcel src) {
        if (src != null) {
            this.quantity = src.readFloat();
            this.measure = src.readString();
            this.ingredient = src.readString();
        }
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {

        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            if (size < 1) {
                return new Ingredient[0];
            }
            else {
                return new Ingredient[size];
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
            dest.writeFloat(this.quantity);
            dest.writeString(this.measure);
            dest.writeString(this.ingredient);
        }
    }
}
