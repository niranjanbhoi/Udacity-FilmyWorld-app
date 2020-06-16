package com.developer.kimy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.kimy.R;
import com.developer.kimy.models.MovieCharacters;
import com.developer.kimy.utils.ImagePath;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 *  Adapter for cast i.e. it shows the list of
 *  cast and directors.
 */

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    private List<MovieCharacters> mMovieCharacters;


    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_item, parent, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        MovieCharacters cast = mMovieCharacters.get(position);

        holder.setView(cast);

    }

    @Override
    public int getItemCount() {
        if (mMovieCharacters == null){
            return 0;
        }
        return mMovieCharacters.size();
    }

    public void setMovieCharacters(List<MovieCharacters> movieCharacters) {
        mMovieCharacters = movieCharacters;
        notifyDataSetChanged();
    }

    static class CastViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        public CastViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mView = itemView;
        }

        public void setView(MovieCharacters cast){
            ImageView imageView = mView.findViewById(R.id.cast_image);
            TextView name = mView.findViewById(R.id.cast_real_name);

            String castName;
            if (cast.getName().length() > 18){
                castName = cast.getName().substring(0,15)+"...";
            }else{
                castName = cast.getName();
            }

            Picasso.get()
                    .load(ImagePath.movieImagePathBuilder(cast.getProfilePath()))
                    .placeholder(R.mipmap.default_cast)
                    .into(imageView);

            name.setText(castName);
        }
    }

}
