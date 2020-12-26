package cn.yjw.pixabayapp.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.Objects;

import cn.yjw.pixabayapp.R;
import cn.yjw.pixabayapp.entity.remote.PhotoItemEntity;
import io.supercharge.shimmerlayout.ShimmerLayout;

/**
 * @author yinjiawei
 * @date 2020/12/23
 */
public class GalleryViewPagerAdapter extends ListAdapter<PhotoItemEntity, GalleryViewPagerAdapter.GalleryViewPagerHolder> {

    private static DiffUtil.ItemCallback<PhotoItemEntity> diffCallback =
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

    public GalleryViewPagerAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public GalleryViewPagerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.pager_photo_view, parent, false);
        return new GalleryViewPagerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewPagerHolder holder, int position) {
        holder.shimmerLayout.setShimmerColor(0x55FFFFFF);
        holder.shimmerLayout.setShimmerAngle(0);
        holder.shimmerLayout.startShimmerAnimation();
        PhotoItemEntity item = getItem(position);
        Glide.with(holder.itemView)
                .load(item.getLargeImageUrl())
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
                .into(holder.photoView);
    }

    public static class GalleryViewPagerHolder extends RecyclerView.ViewHolder {
        private ShimmerLayout shimmerLayout;
        private PhotoView photoView;

        public GalleryViewPagerHolder(@NonNull View itemView) {
            super(itemView);
            shimmerLayout = itemView.findViewById(R.id.shimmerLayout);
            photoView = itemView.findViewById(R.id.page_photo_view);
        }
    }
}
