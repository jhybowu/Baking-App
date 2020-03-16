package com.example.bakingapp;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.widget.RemoteViews;

import com.example.bakingapp.service.ListViewWidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider
{
    static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                String recipeName,
                                int appWidgetId)
    {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
        views.setTextViewText(R.id.tv_widget_recipe_name, recipeName);

        Intent onClickIntent = new Intent(context, BakingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0,
                onClickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // setup support for click anywhere on the widget.
        views.setOnClickPendingIntent(R.id.ll_widget, pendingIntent);

        // Setup the list Details
        Intent remoteAdapterIntent = new Intent(context, ListViewWidgetService.class);
        views.setRemoteAdapter(R.id.lv_widget_ingredient_list, remoteAdapterIntent);

        // Reference
        // https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget/
        // for the click intent per list view item.
        Intent clickIntentListView = new Intent(context, BakingActivity.class);
        PendingIntent listViewItemPendingIntent = TaskStackBuilder.create(context).
                addNextIntentWithParentStack(clickIntentListView).
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // is only being applied to the non ListView.
        views.setPendingIntentTemplate(R.id.lv_widget_ingredient_list, listViewItemPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    // By default is called when the time limit expires as specified within the
    // `baking_widget_info.xml` resource file.
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getResources().getString(R.string.shared_preferences_name),
                Context.MODE_PRIVATE);

        String recipeName = sharedPreferences.getString(
                context.getResources().getString(R.string.key_recipe), "Recipe");

        // Intent service will be responsible for updating all the UI elements.
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipeName, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            // An intent can send anything in a boardcast only want to perform an action if
            // it meets the correct action.
            if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
                // intent is an update intent for the appwidget manager.
                int [] widgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(
                        new ComponentName(context, BakingWidgetProvider.class));

                SharedPreferences sharedPreferences = context.getSharedPreferences(
                        context.getResources().getString(R.string.shared_preferences_name),
                        Context.MODE_PRIVATE);

                String recipeName = sharedPreferences.getString(
                        context.getResources().getString(R.string.key_recipe), "Recipe");

                for (int appWidgetId : widgetIds) {
                    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
                    views.setTextViewText(R.id.tv_widget_recipe_name, recipeName);
                    AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, views);
                }

                AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(
                        widgetIds,
                        R.id.lv_widget_ingredient_list);
            }
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    // Reference
    // https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget/
    // for supporting my activity to be able to inform the widget service to update.
    public static void sendRefreshBoardcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, BakingWidgetProvider.class));
        context.sendBroadcast(intent);
    }
}

