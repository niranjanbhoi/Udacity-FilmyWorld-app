package com.developer.kimy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.developer.kimy.adapters.CastAdapter;
import com.developer.kimy.adapters.MoreImagesAdapter;
import com.developer.kimy.adapters.MovieAdapter;
import com.developer.kimy.adapters.TrailerAdapter;
import com.developer.kimy.models.MovieModel;
import com.developer.kimy.repository.database.AppDatabase;
import com.developer.kimy.utils.AppExecutors;
import com.developer.kimy.utils.GenreList;
import com.developer.kimy.utils.ImagePath;
import com.developer.kimy.utils.YouTubePath;
import com.developer.kimy.viewmodels.MovieDetails;
import com.developer.kimy.widgets.MovieReviewWidget;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

public class MovieActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickHandler, TrailerAdapter.TrailerClickHandler {


    // Constants
    public static final String EXTRA_MOVIE = "movie";
    private static final int GRID_SPAN_FOR_SIMILAR_MOVIES = 2;

    // Views and Adapters
    private MovieModel movieDetail;
    private CastAdapter mCastAdapter;
    private ImageView backDropImage;
    private ImageView posterImage;
    private TextView movieTitle;
    private TextView releaseDate;
    private TextView rating;
    private TextView genres;
    private TextView overview;
    private FloatingActionButton mFavoriteBtn;
    private RecyclerView castRecyclerView;
    private RecyclerView moreImagesView;
    private RecyclerView similarMovies;
    private RecyclerView mTrailersView;
    private MovieDetails movieDetailViewModel;
    private MoreImagesAdapter mMoreImagesAdapter;
    private MovieAdapter mSimilarMoviesAdapter;
    private TrailerAdapter mTrailerAdapter;
    private Button mShareButton;

    private AppDatabase appDatabase;
    private FirebaseAnalytics mFirebaseAnalytics;

    //flags
    private boolean isFavourite = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        movieDetail = (MovieModel) Objects.requireNonNull(getIntent().getExtras()).getSerializable(EXTRA_MOVIE);


        movieDetailViewModel = ViewModelProviders.of(this).get(MovieDetails.class);
        // getting database instance
        appDatabase = AppDatabase.getInstance(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        movieDetailViewModel.init(movieDetail.getId());
        movieDetailViewModel.initializeFavouriteMovie(appDatabase, movieDetail.getId());

        //    Retrieving the views
        backDropImage = findViewById(R.id.back_drop_image);
        posterImage = findViewById(R.id.poster_image);
        overview = findViewById(R.id.overview);
        movieTitle = findViewById(R.id.movie_title);
        releaseDate = findViewById(R.id.release_date);
        rating = findViewById(R.id.rating);
        genres = findViewById(R.id.genre_text);
        castRecyclerView = findViewById(R.id.cast_view);
        moreImagesView = findViewById(R.id.more_images_view);

        mTrailersView = findViewById(R.id.trailers_view);
        mFavoriteBtn = findViewById(R.id.favorite_btn);
        mShareButton = findViewById(R.id.share_btn);


        mCastAdapter = new CastAdapter();
        mMoreImagesAdapter = new MoreImagesAdapter();
        mSimilarMoviesAdapter = new MovieAdapter(this);
        mTrailerAdapter = new TrailerAdapter(this);

        if (movieDetail != null){
            bindViews();
        }

    }


    public void bindViews() {

        checkForFavorite();

        //   Setting up the backdrop image
        Picasso.get()
                .load(ImagePath.movieImagePathBuilder(movieDetail.getBackdropPath()))
                .placeholder(R.drawable.default_image_placeholder)
                .into(backDropImage);

        // Setting up poster image
        Picasso.get()
                .load(ImagePath.movieImagePathBuilder(movieDetail.getPosterPath()))
                .placeholder(R.drawable.default_image_placeholder)
                .into(posterImage);

        // Setting up Title, Release date, rating
        overview.setText(movieDetail.getOverview());
        movieTitle.setText(movieDetail.getTitle());

        //_________________** Converting date in month,dd yyyy format **______________________________
        String date = "";
        if (movieDetail.getReleaseDate() != null && !movieDetail.getReleaseDate().isEmpty()) {
            try {
                Date stringToDate = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).parse(movieDetail.getReleaseDate());
                if (stringToDate != null) {
                    long milliseconds = stringToDate.getTime();
                    date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(milliseconds);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //__________________________________________________________________________________________
        releaseDate.setText(date);

        rating.setText(String.valueOf(movieDetail.getVoteAverage()));

        // Creating the genres string
        StringBuilder genreStringBuilder = new StringBuilder();
        GenreList genreList = new GenreList();
        HashMap<Integer, String> genre = genreList.getGenres();
        Iterator<Integer> iterator = movieDetail.getGenreId().iterator();
        while (iterator.hasNext()) {
            genreStringBuilder.append(genre.get(iterator.next()));
            if (iterator.hasNext()) {
                genreStringBuilder.append(" | ");
            }
        }

        genres.setText(genreStringBuilder.toString());

        /*
            Calling methods to set cast, more Images and similar movies
         */
        setMovieCharacters();
        setMoreImages();

        setTrailersView();

        mFavoriteBtn.setOnClickListener(v -> changeFavouriteStatus());
        mShareButton.setOnClickListener(v -> {
            startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText("Watch "+movieDetail.getTitle()+" with me.\n #MovieReviewApp")
                    .getIntent(), getString(R.string.share_btn_txt)));
            fireBaseLogs("5", movieDetail.getTitle() + " share");
        });


    }

    private void checkForFavorite(){
        movieDetailViewModel.getMovieById().observe(this, movieModel -> {
            if (movieModel == null){
               mFavoriteBtn.setImageDrawable(getDrawable(R.drawable.ic_outline_favorite_border_24));
                isFavourite = false;
            }else {
                mFavoriteBtn.setImageDrawable(getDrawable(R.drawable.ic_baseline_favorite_24));
                isFavourite = true;
            }
        });
    }

    private void setTrailersView() {
        movieDetailViewModel.getTrailerLiveData().observe(this, movieTrailers -> {
            if (movieTrailers == null || movieTrailers.size() == 0){
                TextView trailersTitle = findViewById(R.id.trailers_heading);
                trailersTitle.setVisibility(View.GONE);
            }
            mTrailerAdapter.setTrailersList(movieTrailers);
        });
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mTrailersView.setLayoutManager(linearLayout);
        mTrailersView.setAdapter(mTrailerAdapter);

    }

    private void setMovieCharacters() {
        movieDetailViewModel.getCharacters().observe(this, movieCharacters -> {
            if (movieCharacters == null || movieCharacters.size() == 0) {
                TextView movieCharacterTitle = findViewById(R.id.cast_heading);
                movieCharacterTitle.setVisibility(View.GONE);
            }
            mCastAdapter.setMovieCharacters(movieCharacters);
        });

        LinearLayoutManager linearLayout = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        castRecyclerView.setLayoutManager(linearLayout);
        castRecyclerView.setAdapter(mCastAdapter);

    }

    private void setMoreImages() {

        movieDetailViewModel.getMoreImages().observe(this, moreImagesModels -> {
            if (moreImagesModels == null || moreImagesModels.size() == 0) {
                TextView moreImagesTitle = findViewById(R.id.more_images_heading);
                moreImagesTitle.setVisibility(View.GONE);
            }
            mMoreImagesAdapter.setMoreImagesModels(moreImagesModels);
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        moreImagesView.setLayoutManager(linearLayoutManager);
        moreImagesView.setAdapter(mMoreImagesAdapter);


    }



    /**
     * This method adds or delete the favorite movie from the database
     * It uses executors to run this task on background thread
     */
    private void changeFavouriteStatus(){
        if (isFavourite){
            AppExecutors.getInstance().diskIO().execute(() -> {
                appDatabase.movieDao().deleteMovie(movieDetail);
                fireBaseLogs("3", movieDetail.getTitle() + " removed to favorite");
                updateWidgets();
                isFavourite = false;
                runOnUiThread(() -> mFavoriteBtn.setImageDrawable(getDrawable(R.drawable.ic_outline_favorite_border_24)));
            });
        }else {
            AppExecutors.getInstance().diskIO().execute(() -> {
                appDatabase.movieDao().insertMovie(movieDetail);
                fireBaseLogs("4", movieDetail.getTitle() + " added to favorite");
                updateWidgets();
                isFavourite = true;
                runOnUiThread(() -> mFavoriteBtn.setImageDrawable(getDrawable(R.drawable.ic_baseline_favorite_24)));
            });
        }
    }

    @Override
    public void onMovieClick(MovieModel movie) {
        Intent movieDetail = new Intent(this, MovieActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MovieActivity.EXTRA_MOVIE, movie);
        movieDetail.putExtras(bundle);
        startActivity(movieDetail);
    }

    @Override
    public void onTrailerClickHandle(String videoId) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YouTubePath.buildAppVideoPath(videoId)));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(YouTubePath.buildVideoPath(videoId)));
        try {
            this.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            this.startActivity(webIntent);
        }
    }

    /**
     * method to log events of app
     * @param id id of item
     * @param log string
     */
    private void fireBaseLogs(String id, String log){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, log);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void updateWidgets(){
        ComponentName componentName = new ComponentName(this, MovieReviewWidget.class);
        int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(componentName);
        Intent intent = new Intent(this, MovieReviewWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }
}
