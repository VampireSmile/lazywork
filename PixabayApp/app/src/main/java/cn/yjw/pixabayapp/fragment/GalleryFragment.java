package cn.yjw.pixabayapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import cn.yjw.pixabayapp.R;
import cn.yjw.pixabayapp.adapter.GalleryAdapter;
import cn.yjw.pixabayapp.databinding.FragmentGalleryBinding;
import cn.yjw.pixabayapp.viewmodel.GalleryViewModel;

/**
 * @author yinjiawei
 * @date 2020/12/21
 */
public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private GalleryViewModel galleryViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.swipeIndicator) {
            binding.swipeRefresh.setRefreshing(true);
            new Handler().postDelayed(galleryViewModel::fetchData, 1000);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        galleryViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(GalleryViewModel.class);
        GalleryAdapter adapter = new GalleryAdapter();
        binding.recycleview.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        binding.recycleview.setLayoutManager(layoutManager);
        galleryViewModel.getPhotoLiveList().observe(getViewLifecycleOwner(),
                photoItemEntities -> {
                    adapter.submitList(photoItemEntities);
                    if (binding.swipeRefresh.isRefreshing()) {
                        binding.swipeRefresh.setRefreshing(false);
                    }
                });
        binding.swipeRefresh.setOnRefreshListener(galleryViewModel::fetchData);
    }
}