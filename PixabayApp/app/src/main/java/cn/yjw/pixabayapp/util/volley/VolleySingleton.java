package cn.yjw.pixabayapp.util.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * @author yinjiawei
 * @date 2020/12/21
 */
public class VolleySingleton {

    private volatile static VolleySingleton INSTANCE = null;

    private volatile static RequestQueue requestQueue = null;

    /**
     * doubleCheck
     *
     * @return
     */
    public static VolleySingleton getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (VolleySingleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new VolleySingleton(context);
                }
            }
        }
        return INSTANCE;
    }

    public <T> void addRequest(Request<T> request) {
        requestQueue.add(request);
    }

    private VolleySingleton(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
    }
}
