package cn.yjw.pixabayapp.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.Objects;

import cn.yjw.pixabayapp.R;
import cn.yjw.pixabayapp.databinding.FragmentPhotoBinding;
import cn.yjw.pixabayapp.entity.remote.PhotoItemEntity;

/**
 * @author yinjiawei
 * @date 2020/12/21
 */
public class PhotoFragment extends Fragment {
    private FragmentPhotoBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPhotoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.shimmerLayout.setShimmerColor(0x55FFFFFF);
        binding.shimmerLayout.setShimmerAngle(0);
        binding.shimmerLayout.startShimmerAnimation();
        PhotoItemEntity photoItem = requireArguments().getParcelable(getString(R.string.gallery_photo_bundle_key));
        String url = Objects.requireNonNull(photoItem).getLargeImageUrl();
        Glide.with(requireContext())
                .load(url)
                .placeholder(R.drawable.ic_baseline_gray_photo_24)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Objects.requireNonNull(binding.shimmerLayout).stopShimmerAnimation();
                        return false;
                    }
                })
                .into(binding.photoView);
    }
}