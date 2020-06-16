package com.developer.kimy.widgets;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import com.developer.kimy.R;
import com.developer.kimy.models.MovieModel;
import com.developer.kimy.repository.database.AppDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MovieWidgetListProvider implements RemoteViewsService.RemoteViewsFactory {

    private AppDatabase mAppDatabase;
    private List<MovieModel> favMovieList;
    private Context mContext;

    public MovieWidgetListProvider(Context context, Intent intent){
        this.mContext = context;
       
    }

    @Override
    public void onCreate() {
        mAppDatabase = AppDatabase.getInstance(mContext);
        favMovieList = new ArrayList<>();

    }

    /**
     * This method will retrieve list of all favorite movies.
     * The main trick is it will make sync calls because this method
     * will be called from onDataSetChanged and this method will not
     * run on main UI thread whereas onCreate method will run on
     * main UI thread
     */
    private void updateFavList(){

        final long identityToken = Binder.clearCallingIdentity();
        favMovieList =  mAppDatabase.movieDao().loadAllMoviesForWidget();


        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDataSetChanged() {
        if (favMovieList != null){
            favMovieList.clear();
        }
        Log.d(TAG, "onDataSetChanged: changed");
        updateFavList();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (favMovieList == null){
            return 0;
        }
        return favMovieList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item_widget);
        Log.d(TAG, "getViewAt: here at getViews Item "+favMovieList.size());
        if (favMovieList != null && favMovieList.size() != 0){
            remoteViews.setTextViewText(R.id.widget_list_item, favMovieList.get(position).getTitle());
        }

        return remoteViews;
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
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
