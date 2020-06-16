package com.developer.kimy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.kimy.R;
import com.developer.kimy.models.MovieModel;
import com.developer.kimy.utils.ImagePath;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<MovieModel> mMovieList;
    private OnMovieClickHandler mOnMovieClickHandler;

    /*
        Interface to handle clicks on movie
     */
    public interface OnMovieClickHandler{
        void onMovieClick(MovieModel movie);
    }

    public MovieAdapter(OnMovieClickHandler onMovieClickHandler) {
        mOnMovieClickHandler = onMovieClickHandler;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        final MovieModel movie = mMovieList.get(position);

        holder.bind(movie);

    }

    @Override
    public int getItemCount() {
        if (mMovieList == null){
            return 0;
        }
        return mMovieList.size();
    }

    public void setMovieList(List<MovieModel> movieList) {
        mMovieList = movieList;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View mView;

    public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            itemView.setOnClickListener(this);
        }

        public void bind(MovieModel movie){
            ImageView imageView = mView.findViewById(R.id.new_release_img);
            TextView title = mView.findViewById(R.id.title);
            TextView dateText = mView.findViewById(R.id.date);

            String movieTitle = movie.getTitle();

            if (movieTitle.length() > 20){
                movieTitle = movieTitle.substring(0,17) + "...";
            }

            title.setText(movieTitle);

            Picasso.get()
                    .load(ImagePath.movieImagePathBuilder(movie.getPosterPath()))
                    .into(imageView);

            //_________________** Converting date in month,dd yyyy format **______________________________
            String date = "";
            if (movie.getReleaseDate() != null && !movie.getReleaseDate().isEmpty()){
                try {
                    Date stringToDate = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).parse(movie.getReleaseDate());
                    if (stringToDate != null){
                        long milliseconds = stringToDate.getTime();
                        date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(milliseconds);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            //__________________________________________________________________________________________

            dateText.setText(date);


        }

        @Override
        public void onClick(View v) {
            if (mOnMovieClickHandler != null){
                mOnMovieClickHandler.onMovieClick(mMovieList.get(getAdapterPosition()));
            }
        }
    }

}
