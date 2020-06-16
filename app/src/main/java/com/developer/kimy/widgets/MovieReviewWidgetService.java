package com.developer.kimy.widgets;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class MovieReviewWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MovieWidgetListProvider( this.getApplicationContext(), intent);
    }
}
