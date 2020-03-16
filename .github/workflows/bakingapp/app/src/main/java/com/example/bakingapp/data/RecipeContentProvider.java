package com.example.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RecipeContentProvider extends ContentProvider {
    private static RecipeDBHelper sDB = null;
    private static final UriMatcher sUriMatcher = RecipeContentProvider.buildUriMatcher();

    public static final int RECIPE_INGREDIENTS = 100;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,
                RecipeContract.PATH_RECIPE_INGREDIENTS,
                RECIPE_INGREDIENTS);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        sDB = new RecipeDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder)
    {
        Cursor cursor = null;
        switch (sUriMatcher.match(uri)) {
            case RECIPE_INGREDIENTS:
                cursor = sDB.getReadableDatabase().query(
                    RecipeContract.PATH_RECIPE_INGREDIENTS,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("query with " + uri.toString() +
                        " is currently not supported, sorry please create an issue."
                );
        }

        ContentResolver resolver = null;

        if (getContext() != null) {
            resolver = getContext().getContentResolver();
        }

        if (cursor != null && resolver != null) {
            cursor.setNotificationUri(resolver, uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // Not going to be supported
        throw new UnsupportedOperationException(
                "getType is currently not supported, sorry please create an issue."
        );
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long insertId = 0;
        Uri result = null;

        switch (sUriMatcher.match(uri)) {
            case RECIPE_INGREDIENTS:
                insertId = sDB.getWritableDatabase().
                    insert(RecipeContract.PATH_RECIPE_INGREDIENTS,
                            null,
                            values);

                if (insertId < 0) {
                    throw new SQLException("Could not insert values into the database.");
                } else {
                    // likely un-necessary since this will actually return the total number of
                    // entries added not a proper id.
                    result = Uri.withAppendedPath(RecipeContract.RecipeIngredient.CONTENT_URI,
                            Long.toString(insertId));
                }
                break;
            default:
                throw new UnsupportedOperationException("insert with " + uri.toString() +
                        " is currently not supported, sorry please create an issue."
                );
        }

        ContentResolver resolver = null;

        if (getContext() != null) {
            resolver = getContext().getContentResolver();
        }

        if (result != null && resolver != null) {
            resolver.notifyChange(result, null);
        }

        return result;
    }

    @Override
    public int delete(@NonNull Uri uri,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs)
    {
        // Not going to be supported
        throw new UnsupportedOperationException(
                "delete is currently not supported, sorry please create an issue."
        );
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs)
    {
        // Not going to be supported
        throw new UnsupportedOperationException(
                "update is currently not supported, sorry please create an issue."
        );
    }
}
