package com.developer.kimy.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.developer.kimy.models.MoreImagesModel;
import com.developer.kimy.models.MoreImagesResponse;
import com.developer.kimy.models.MovieCharacterResponse;
import com.developer.kimy.models.MovieCharacters;
import com.developer.kimy.models.MovieModel;
import com.developer.kimy.models.MovieResponse;
import com.developer.kimy.models.TrailerModel;
import com.developer.kimy.models.TrailersResponse;
import com.developer.kimy.repository.network.GetMovieDataService;
import com.developer.kimy.repository.network.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class GetMovieData {

    private static final String API_KEY = "f4c4f61b1ba00bcb643537c908ad5abe";

    private static GetMovieData getMovieData;

    private GetMovieDataService mGetMovieDataService;

    /**
     * Implemented singleton class so that no two connection open to
     * the network
     *
     * @return instance of GetMovieData class
     */
    public static GetMovieData getInstance() {
        if (getMovieData == null) {
            getMovieData = new GetMovieData();
        }

        return getMovieData;
    }

    private GetMovieData() {
        mGetMovieDataService = RetrofitInstance.getRetrofitInstance().create(GetMovieDataService.class);
    }

    /**
     * Retrieves the popular movies list from TmDb API
     *
     * @return list of popular movies
     */
    public MutableLiveData<List<MovieModel>> getMovies() {

        final MutableLiveData<List<MovieModel>> data = new MutableLiveData<>();

        mGetMovieDataService.getPopularMovie(API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {

            }
        });

        return data;
    }

    /**
     * Query for top Rated movies
     * @return list of top rated movies
     */
    public MutableLiveData<List<MovieModel>> getTopRated(){
        final MutableLiveData<List<MovieModel>> mTopRated = new MutableLiveData<>();

        mGetMovieDataService.getTopRatedMovies(API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call,@NonNull Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResults() != null){
                    mTopRated.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call,@NonNull Throwable t) {

            }
        });

        return mTopRated;
    }

    /**
     * Retrieves list of new release
     *
     * @return list of new releases movies
     */
    public MutableLiveData<List<MovieModel>> getNewReleases() {
        final MutableLiveData<List<MovieModel>> data = new MutableLiveData<>();

        mGetMovieDataService.getNewReleases(API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body().getResults());

                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {

            }
        });

        return data;
    }

    /**
     * Method to return cast of movie.
     *
     * @param movieId Id of movie
     * @return return Live data of list of Movie Character model
     */
    public MutableLiveData<List<MovieCharacters>> getCredits(int movieId) {

        final MutableLiveData<List<MovieCharacters>> castData = new MutableLiveData<>();

        mGetMovieDataService.getCharacter(movieId, API_KEY).enqueue(new Callback<MovieCharacterResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieCharacterResponse> call, @NonNull Response<MovieCharacterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    castData.setValue(response.body().getCast());
                }

            }

            @Override
            public void onFailure(@NonNull Call<MovieCharacterResponse> call,@NonNull Throwable t) {

            }
        });
        return castData;
    }

    /** This method is used to retrieve more images related to movie
     * @param movieId of the selected
     * @return the list of MoreImagesModel
     */
    public MutableLiveData<List<MoreImagesModel>> getMoreImages(int movieId) {
        final MutableLiveData<List<MoreImagesModel>> moreImagesData = new MutableLiveData<>();

        mGetMovieDataService.getMoreImages(movieId, API_KEY).enqueue(new Callback<MoreImagesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoreImagesResponse> call, @NonNull Response<MoreImagesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    moreImagesData.setValue(response.body().getBackdrops());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoreImagesResponse> call, @NonNull Throwable t) {

            }
        });

        return moreImagesData;
    }

    /**
     * This method query for similar movies of the selected movies
     * @param movieId of the selected movie
     * @return the list of similar movies
     */
    public MutableLiveData<List<MovieModel>> getSimilarMovies(int movieId){
        final MutableLiveData<List<MovieModel>> mSimilarMovies = new MutableLiveData<>();

        mGetMovieDataService.getSimilarMovies(movieId, API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call,@NonNull Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    mSimilarMovies.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call,@NonNull Throwable t) {
                Log.d(TAG, "onFailure: here we are in failure");
                try {
                    throw new Throwable(t);
                }catch (Throwable e){
                    e.printStackTrace();
                }
            }
        });
        return mSimilarMovies;
    }

    /**
     * Method which calls for videos of movie
     * @param movieId for which videos are requested
     * @return list of trailer models
     */
    public MutableLiveData<List<TrailerModel>> getMoviesTrailer(int movieId){
        final MutableLiveData<List<TrailerModel>> mTrailersList = new MutableLiveData<>();

        mGetMovieDataService.getMovieTrailers(movieId, API_KEY).enqueue(new Callback<TrailersResponse>() {
            @Override
            public void onResponse(@NonNull Call<TrailersResponse> call,@NonNull Response<TrailersResponse> response) {
                if (response.isSuccessful()){

                    if (response.body() != null && response.body().getResults() != null){
                        mTrailersList.setValue(response.body().getResults());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TrailersResponse> call,@NonNull Throwable t) {

            }
        });
        return mTrailersList;
    }

    /**
     * gets the list of movie by search key
     * @param searchKey search key
     * @return list of movie matched
     */
    public MutableLiveData<List<MovieModel>> getMoviesBySearch(String searchKey){
        final MutableLiveData<List<MovieModel>> mMovieList = new MutableLiveData<>();

        mGetMovieDataService.getSearchedMovies(API_KEY, searchKey).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call,@NonNull Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    mMovieList.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call,@NonNull Throwable t) {

            }
        });

        return mMovieList;
    }

}
