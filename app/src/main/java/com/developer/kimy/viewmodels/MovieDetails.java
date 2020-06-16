package com.developer.kimy.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.developer.kimy.models.MoreImagesModel;
import com.developer.kimy.models.MovieCharacters;
import com.developer.kimy.models.MovieModel;
import com.developer.kimy.models.TrailerModel;
import com.developer.kimy.repository.GetMovieData;
import com.developer.kimy.repository.database.AppDatabase;

import java.util.List;


public class MovieDetails extends ViewModel {

    private MutableLiveData<List<MovieCharacters>> mMovieCharacters;
    private MutableLiveData<List<MoreImagesModel>> mMoreImages;
    private MutableLiveData<List<MovieModel>> mSimilarMovies;
    private MutableLiveData<List<TrailerModel>> mTrailerLiveData;
    private LiveData<MovieModel> mMovieById;

    public void init(int movieId){
        if (mMovieCharacters != null && mMoreImages != null && mSimilarMovies != null && mTrailerLiveData == null){
            return;
        }

        GetMovieData getMovieData = GetMovieData.getInstance();

        if (mMovieCharacters == null){
            mMovieCharacters = getMovieData.getCredits(movieId);
        }

        if (mMoreImages == null){
            mMoreImages = getMovieData.getMoreImages(movieId);
        }

        if (mSimilarMovies == null){
            mSimilarMovies = getMovieData.getSimilarMovies(movieId);
        }

        if (mTrailerLiveData == null){
            mTrailerLiveData = getMovieData.getMoviesTrailer(movieId);
        }


    }

    public void initializeFavouriteMovie(AppDatabase appDatabase, int id){
        if (mMovieById != null){
            return;
        }

        mMovieById = appDatabase.movieDao().loadMovieById(id);
    }

    public LiveData<List<MovieCharacters>> getCharacters(){
        return this.mMovieCharacters;
    }

    public LiveData<List<MoreImagesModel>> getMoreImages(){
        return this.mMoreImages;
    }

    public LiveData<List<MovieModel>> getSimilarMovies(){
        return this.mSimilarMovies;
    }

    public LiveData<List<TrailerModel>> getTrailerLiveData() {
        return mTrailerLiveData;
    }

    public LiveData<MovieModel> getMovieById(){
        return mMovieById;
    }
}
