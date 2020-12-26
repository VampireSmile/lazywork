package cn.yjw.pixabayapp.fragment;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

import cn.yjw.pixabayapp.R;
import cn.yjw.pixabayapp.adapter.GalleryViewPagerAdapter;
import cn.yjw.pixabayapp.databinding.FragmentPagerPhotoBinding;
import cn.yjw.pixabayapp.entity.remote.PhotoItemEntity;
import cn.yjw.pixabayapp.util.common.SimpleAsyncUtil;
import cn.yjw.pixabayapp.util.common.ToastUtil;

/**
 * @author yinjiawei
 * @date 2020/12/23
 */
public class PagerPhotoFragment extends Fragment {
    private static final String TAG = "PagerPhotoFragment";
    private static Handler handler = new Handler(Looper.getMainLooper());
    /**
     * 小于minSdkInt的SDK版本需要请求权限
     */
    private int requestId;
    /**
     * 请求码
     */
    private int minSdkInt;
    private FragmentPagerPhotoBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requestId = getResources().getInteger(R.integer.DEFAULT_REQUEST_PERMISSION_CODE);
        minSdkInt = getResources().getInteger(R.integer.DEFAULT_PERMISSION_SDK_INT);
        binding = FragmentPagerPhotoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<PhotoItemEntity> list =
                Objects.requireNonNull(requireArguments().getParcelableArrayList(getString(R.string.gallery_photo_bundle_key)));
        int curPosition = requireArguments().getInt(getString(R.string.photo_position_bundle_key));
        GalleryViewPagerAdapter adapter = new GalleryViewPagerAdapter();
        binding.galleryViewpager.setAdapter(adapter);
        adapter.submitList(list);
        //这一句不能在adapter.submitList(list);之前出现
        binding.galleryViewpager.setCurrentItem(curPosition, false);
        binding.photoTag.setText(getString(R.string.photo_tag, curPosition + 1, list.size()));
        binding.galleryViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.photoTag.setText(getString(R.string.photo_tag, position + 1, list.size()));
            }
        });

        binding.ivSave.setOnClickListener(v -> {
            //需要请求的权限
            String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //检查是否需要发起WRITE_EXTERNAL_STORAGE权限请求
            //api小于minSdkInt需要动态申请
            if (Build.VERSION.SDK_INT < minSdkInt && ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, requestId);
            } else {
                savePhoto();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestId) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                savePhoto();
            } else {
                ToastUtil.showMessageShort(requireContext(), getString(R.string.request_permission_failed));
            }
        }
    }

    /**
     * 保存图片操作
     */
    private void savePhoto() {
        RecyclerView view = (RecyclerView) binding.galleryViewpager.getChildAt(0);
        GalleryViewPagerAdapter.GalleryViewPagerHolder holder =
                (GalleryViewPagerAdapter.GalleryViewPagerHolder) view.findViewHolderForAdapterPosition(binding.galleryViewpager.getCurrentItem());
        Objects.requireNonNull(holder);
        ImageView img = holder.itemView.findViewById(R.id.page_photo_view);
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ContentValues values = new ContentValues();
        Uri uri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (uri == null) {
            ToastUtil.showMessageShort(requireContext(), getString(R.string.save_failed));
            return;
        }
        //简单封装了下线程池,提交任务后台执行
        SimpleAsyncUtil.submitTask(() -> {
            try (OutputStream outputStream = requireContext().getContentResolver().openOutputStream(uri)) {
                boolean asyncResult = bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                SimpleAsyncUtil.setAsyncResult(asyncResult);
            } catch (IOException e) {
                Log.e(TAG, "savePhoto: failed - ", e);
            } finally {
                Object asyncResult = SimpleAsyncUtil.getAsyncResult();
                //需要显示移除避免内存泄露
                SimpleAsyncUtil.removeAsyncResult();
                handler.post(() -> {
                    boolean success = asyncResult != null && (boolean) asyncResult;
                    String message = success ? getString(R.string.save_success) : getString(R.string.save_failed);
                    ToastUtil.showMessageShort(requireContext(), message);
                });
            }
        });
    }
}