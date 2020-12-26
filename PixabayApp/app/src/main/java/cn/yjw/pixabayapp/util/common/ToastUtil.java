package cn.yjw.pixabayapp.util.common;

import android.content.Context;
import android.widget.Toast;

/**
 * @author yinjiawei
 * @date 2020/12/24
 */
public class ToastUtil {
    public static void showMessageLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showMessageShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
