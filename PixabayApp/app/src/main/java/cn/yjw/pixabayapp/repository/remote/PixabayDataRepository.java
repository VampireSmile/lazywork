package cn.yjw.pixabayapp.repository.remote;

import android.app.Application;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.List;
import java.util.Random;

import cn.yjw.pixabayapp.R;
import cn.yjw.pixabayapp.entity.remote.PhotoItemEntity;
import cn.yjw.pixabayapp.entity.remote.PixabayEntity;
import cn.yjw.pixabayapp.util.volley.VolleySingleton;

/**
 * @author yinjiawei
 * @date 2020/12/21
 */
public class PixabayDataRepository {
    private static final String TAG = "PixabayDataRepository";
    private static final String[] DEFAULT_PARAMS = new String[]{
            "car", "photo", "air", "computer",
            "flower", "animal", "cat", "dog",
            "smile", "snow", "sky", "nature",
            "city", "people", "star", "moon"
    };

    private Application application;

    public PixabayDataRepository(Application application) {
        this.application = application;
    }

    /**
     * 从Pixabay网站拉取数据并设置到LiveData中
     *
     * @param photoLiveList 被观察的LiveData
     * @param param         url的参数
     */
    public void fetchDataTo(MutableLiveData<List<PhotoItemEntity>> photoLiveList, @Nullable String param) {
        String url = param == null ? urlWrap(randomParam(DEFAULT_PARAMS)) : urlWrap(param);
        Log.d(TAG, "fetchDataUrl: " + url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    PixabayEntity pixabayEntity = new Gson().fromJson(response, PixabayEntity.class);
                    photoLiveList.setValue(pixabayEntity.getPhotoItemList());
                },
                error -> Log.e(TAG, "fetchDataTo:", error));
        VolleySingleton.getInstance(application).addRequest(request);
    }

    /**
     * 保对url进行组合包装
     *
     * @param param 参数
     * @return 包装后的url
     */
    private String urlWrap(String param) {
        return application.getString(R.string.pixabay_url) + param;
    }

    /**
     * 获取随机参数
     *
     * @param query 参数数组
     * @return 查询参数
     */
    private String randomParam(String[] query) {
        Random random = new Random();
        return query[random.nextInt(query.length)];
    }
}
