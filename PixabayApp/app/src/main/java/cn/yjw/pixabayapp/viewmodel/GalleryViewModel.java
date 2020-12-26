package cn.yjw.pixabayapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import cn.yjw.pixabayapp.entity.remote.PhotoItemEntity;
import cn.yjw.pixabayapp.repository.remote.PixabayDataRepository;

/**
 * @author yinjiawei
 * @date 2020/12/21
 */
public class GalleryViewModel extends AndroidViewModel {

    private LiveData<List<PhotoItemEntity>> photoLiveList;
    private PixabayDataRepository repository;

    public GalleryViewModel(@NonNull Application application) {
        super(application);
        photoLiveList = new MutableLiveData<>();
        repository = new PixabayDataRepository(application);
        //初始化LiveData数据
        repository.fetchDataTo((MutableLiveData<List<PhotoItemEntity>>) photoLiveList, null);
    }

    public void fetchData(String param) {
        repository.fetchDataTo((MutableLiveData<List<PhotoItemEntity>>) photoLiveList, param);
    }

    public LiveData<List<PhotoItemEntity>> getPhotoLiveList() {
        return this.photoLiveList;
    }
}
