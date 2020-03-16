package com.example.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeStep implements Parcelable {
    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    private RecipeStep(Parcel src) {
        if (src != null) {
            this.id = src.readInt();
            this.shortDescription = src.readString();
            this.description = src.readString();
            this.videoURL = src.readString();
            this.thumbnailURL = src.readString();
        }
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public int getId() {
        return this.id;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public static final Parcelable.Creator<RecipeStep> CREATOR = new Parcelable.Creator<RecipeStep>() {

        @Override
        public RecipeStep createFromParcel(Parcel source) {
            return new RecipeStep(source);
        }

        @Override
        public RecipeStep[] newArray(int size) {
            if (size < 1) {
                return new RecipeStep[0];
            }
            else {
                return new RecipeStep[size];
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
            dest.writeString(this.shortDescription);
            dest.writeString(this.description);
            dest.writeString(this.videoURL);
            dest.writeString(this.thumbnailURL);
        }
    }
}
