package com.example.bakingapp.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingapp.R;
import com.example.bakingapp.data.RecipeContract;


public class ListViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext = null;
    private Cursor mCursor = null;

    public ListViewFactory(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {}

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                mContext.getResources().getString(R.string.shared_preferences_name),
                Context.MODE_PRIVATE);

        Uri uri = RecipeContract.RecipeIngredient.CONTENT_URI;
        String recipeName = null;

        if (sharedPreferences.contains(mContext.getResources().getString(R.string.key_recipe))) {
            recipeName = sharedPreferences.getString(
                    mContext.getResources().getString(R.string.key_recipe), null);

            String selection = RecipeContract.RecipeIngredient.COLUMN_RECIPE_NAME + "=?";
            String[] selectionArgs = new String[]{recipeName};

            mCursor = mContext.getContentResolver().query(
                    uri,
                    null,
                    selection,
                    selectionArgs,
                    null);
        }

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null ||
                !mCursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_widget_item);
        rv.setTextViewText(R.id.tv_widget_ingredient,
                mCursor.getString(mCursor.getColumnIndex(
                        RecipeContract.RecipeIngredient.COLUMN_INGREDIENT)));
        rv.setTextViewText(R.id.tv_widget_measuring,
                mCursor.getString(mCursor.getColumnIndex(
                        RecipeContract.RecipeIngredient.COLUMN_MEASURING)));
        rv.setTextViewText(R.id.tv_widget_quantity,
                Float.toString(mCursor.getFloat(mCursor.getColumnIndex(
                        RecipeContract.RecipeIngredient.COLUMN_QUANTITY))));

        Intent intent = new Intent();
        rv.setOnClickFillInIntent(R.id.ll_widget_item, intent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return mCursor.moveToPosition(position) ? mCursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
