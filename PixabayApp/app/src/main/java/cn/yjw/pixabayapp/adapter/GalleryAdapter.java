package cn.yjw.pixabayapp.adapter;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.Objects;

import cn.yjw.pixabayapp.R;
import cn.yjw.pixabayapp.entity.remote.PhotoItemEntity;
import io.supercharge.shimmerlayout.ShimmerLayout;

/**
 * @author yinjiawei
 * @date 2020/12/21
 */
public class GalleryAdapter extends ListAdapter<PhotoItemEntity, GalleryAdapter.GalleryHolder> {

    private static DiffUtil.ItemCallback<PhotoItemEntity> diffCallBack =
            new DiffUtil.ItemCallback<PhotoItemEntity>() {

                @Override
                public boolean areItemsTheSame(@NonNull PhotoItemEntity oldItem, @NonNull PhotoItemEntity newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull PhotoItemEntity oldItem, @NonNull PhotoItemEntity newItem) {
                    return oldItem.getId() == newItem.getId();
                }
            };

    public GalleryAdapter() {
        super(diffCallBack);
    }

    @NonNull
    @Override
    public GalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.gallery_cell, parent, false);
        GalleryHolder holder = new GalleryHolder(view);
        holder.itemView.setOnClickListener(v -> {
            PhotoItemEntity item = getItem(holder.getAdapterPosition());
            String key = parent.getContext().getString(R.string.gallery_photo_bundle_key);
            Bundle bundle = new Bundle();
            bundle.putParcelable(key, item);
            Navigation.findNavController(holder.itemView)
                    .navigate(R.id.action_galleryFragment_to_photoFragment, bundle);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryHolder holder, int position) {
        holder.shimmerLayout.setShimmerColor(0x55FFFFFF);
        holder.shimmerLayout.setShimmerAngle(0);
        holder.shimmerLayout.startShimmerAnimation();
        Glide.with(holder.itemView)
                .load(getItem(position).getLargeImageUrl())
                .placeholder(R.drawable.ic_baseline_gray_photo_24)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Objects.requireNonNull(holder.shimmerLayout).stopShimmerAnimation();
                        return false;
                    }
                })
                .into(holder.imageView);
    }

    static class GalleryHolder extends RecyclerView.ViewHolder {
        private ShimmerLayout shimmerLayout;
        private ImageView imageView;

        public GalleryHolder(@NonNull View itemView) {
            super(itemView);
            shimmerLayout = itemView.findViewById(R.id.shimmerCell);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
