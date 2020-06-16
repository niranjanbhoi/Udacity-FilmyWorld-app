package com.developer.kimy.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.developer.kimy.R;


/**
 * Implementation of App Widget functionality.
 */
public class MovieReviewWidget extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            int layoutId = R.layout.movie_review_widget;
            RemoteViews widget = new RemoteViews(context.getPackageName(), layoutId);

            Intent serviceIntent = new Intent(context, MovieReviewWidgetService.class);
            widget.setRemoteAdapter(R.id.widget_list_view, serviceIntent);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
            appWidgetManager.updateAppWidget(appWidgetId, widget);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}

