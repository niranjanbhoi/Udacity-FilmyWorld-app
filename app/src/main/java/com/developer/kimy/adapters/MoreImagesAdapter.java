package com.developer.kimy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.kimy.R;
import com.developer.kimy.models.MoreImagesModel;
import com.developer.kimy.utils.ImagePath;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoreImagesAdapter extends RecyclerView.Adapter<MoreImagesAdapter.MoreImageViewHolder> {


    private List<MoreImagesModel> mMoreImagesModels;


    @NonNull
    @Override
    public MoreImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_images_item, parent, false);
        return new MoreImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreImageViewHolder holder, int position) {
        holder.bindImage(mMoreImagesModels.get(position).getFilePath());
    }

    @Override
    public int getItemCount() {
        if (mMoreImagesModels == null){
            return 0;
        }
        return mMoreImagesModels.size();
    }

    public void setMoreImagesModels(List<MoreImagesModel> moreImagesModels) {
        mMoreImagesModels = moreImagesModels;
        notifyDataSetChanged();
    }

    static class MoreImageViewHolder extends RecyclerView.ViewHolder{
        private ImageView moreImages;
        public MoreImageViewHolder(@NonNull View itemView) {
            super(itemView);

            moreImages = itemView.findViewById(R.id.more_image_item_view);
        }

        void bindImage(String url){
            Picasso.get()
                    .load(ImagePath.movieImagePathBuilder(url))
                    .into(moreImages);
        }
    }
}
