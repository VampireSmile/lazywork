package cn.yjw.pixabayapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
        //通过强转来获取当点击搜索图标后显示的搜索框
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        //设置searchView的最大宽度
        searchView.setMaxWidth(1000);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                galleryViewModel.fetchData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.swipeIndicator) {
            binding.swipeRefresh.setRefreshing(true);
            new Handler().postDelayed(() -> galleryViewModel.fetchData(null), 1000);
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
//        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.recycleview.setLayoutManager(layoutManager);
        galleryViewModel.getPhotoLiveList().observe(getViewLifecycleOwner(), photoItemEntities -> {
                    adapter.submitList(photoItemEntities);
                    if (binding.swipeRefresh.isRefreshing()) {
                        binding.swipeRefresh.setRefreshing(false);
                    }
                });
        binding.swipeRefresh.setOnRefreshListener(() -> galleryViewModel.fetchData(null));
    }
}