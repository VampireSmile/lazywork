package com.arcsoft.arcfacedemo.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.arcfacedemo.R;
import com.arcsoft.arcfacedemo.common.Constants;
import com.arcsoft.arcfacedemo.model.LoginInfo;
import com.arcsoft.arcfacedemo.util.HttpHelper;
import com.arcsoft.face.ActiveFileInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.enums.RuntimeABI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Login extends BaseActivity {
    private static final String TAG = "Login";
    private LocationManager locationManager;
    private static final String GPS_PROVIDER_NAME = android.location.LocationManager.GPS_PROVIDER;
    private String locateType;
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    private String loginResultStr;//保存最终Toast信息
    private CountDownTimer loginTimer;//计时器
    private ProgressDialog dialog;//进度条
    private static Handler handler = new Handler();//handler可以更改UI
    private InputMethodManager manager;//键盘服务
    private LoginInfo loginInfo;//保存从服务器获取的数据
    //控件
    private EditText et_id, et_pass;
    private Button btn_login;
    private TextView tv_edit_pass;
    // 在线激活所需的权限
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    boolean libraryExists = true;
    // Demo 所需的动态库文件
    private static final String[] LIBRARIES = new String[]{
            // 人脸相关
            "libarcsoft_face_engine.so",
            "libarcsoft_face.so",
            // 图像库相关
            "libarcsoft_image_util.so",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //去掉窗口标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏顶部状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        libraryExists = checkSoFile(LIBRARIES);
        ApplicationInfo applicationInfo = getApplicationInfo();
        Log.i(TAG, "onCreate: " + applicationInfo.nativeLibraryDir);
        if (!libraryExists) {//.so文件不存在
            showToast(getString(R.string.library_not_found));
        } else {//.so文件存在
            initView();//初始化控件
            activeEngine();//激活引擎
        }
        //获取定位服务
        locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        locateType = LocationManager.GPS_PROVIDER;
        if (gpsIsEnable() == false)//"访问我的位置信息" 开关没有打开,低于6.0版本的系统可以通过这个直接开启定位
            requstGPS();

        checkpermission();//高版本使用此方法动态获取app所需权限

        tv_edit_pass.setOnClickListener(new View.OnClickListener() {//修改密码
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, editPass.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 检查能否找到动态链接库，如果找不到，请修改工程配置
     *
     * @param libraries 需要的动态链接库
     * @return 动态库是否存在
     */
    private boolean checkSoFile(String[] libraries) {
        File dir = new File(getApplicationInfo().nativeLibraryDir);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return false;
        }
        List<String> libraryNameList = new ArrayList<>();
        for (File file : files) {
            libraryNameList.add(file.getName());
        }
        boolean exists = true;
        for (String library : libraries) {
            exists &= libraryNameList.contains(library);
        }
        return exists;
    }

    /**
     * 激活人脸识别引擎
     */
    public void activeEngine() {
        if (!libraryExists) {
            showToast(getString(R.string.library_not_found));
            return;
        }
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
            return;
        }
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                RuntimeABI runtimeABI = FaceEngine.getRuntimeABI();
                Log.i(TAG, "subscribe: getRuntimeABI() " + runtimeABI);
                int activeCode = FaceEngine.activeOnline(Login.this, Constants.APP_ID, Constants.SDK_KEY);
                emitter.onNext(activeCode);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        if (activeCode == ErrorInfo.MOK) {
                            showToast(getString(R.string.active_success));
                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                            //showToast(getString(R.string.already_activated));
                        } else {
                            showToast(getString(R.string.active_failed, activeCode));
                        }
                        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
                        int res = FaceEngine.getActiveFileInfo(Login.this, activeFileInfo);
                        if (res == ErrorInfo.MOK) {
                            Log.i(TAG, activeFileInfo.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /*初始化控件*/
    public void initView() {
        et_id = findViewById(R.id.edit_userid);
        et_pass = findViewById(R.id.edit_userpass);
        btn_login = findViewById(R.id.btn_login);
        tv_edit_pass = findViewById(R.id.tv_edit_pass);
    }

    public boolean gpsIsEnable() {//判断gps是否可用
        boolean result = locationManager.isProviderEnabled(GPS_PROVIDER_NAME);
        Log.i(TAG, "gpsIsEnable(). result = " + result);
        return result;
    }

    private List initPermission() {                        //高版本动态请求权限
        List<String> permissionlist = new ArrayList<String>();
        String ask_permission_Access[] = new String[]{     //需要请求的权限
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.REQUEST_DELETE_PACKAGES,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //高版本动态查询哪些权限需要开启
            for (String pa : ask_permission_Access) {
                if (ActivityCompat.checkSelfPermission(this, pa) != PackageManager.PERMISSION_GRANTED)
                    permissionlist.add(pa);
            }
        }
        return permissionlist;
    }

    private void checkpermission() {//高版本执行权限开启
        List<String> need_permission_request = initPermission();
        if (null != need_permission_request && need_permission_request.size() > 0)
            ActivityCompat.requestPermissions(this, need_permission_request.toArray(new String[need_permission_request.size()]), 1);
        return;
    }

    private void requstGPS() {
        // TODO Auto-generated method stub
        final String provider = locateType;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String requestTitleStr = "GPS定位请求";
        builder.setTitle(requestTitleStr);
        String requestMsgHintStr;
        requestMsgHintStr = "    为了提供更好的服务,系统请求打开";
        String requestMsgMobileStr = "移动网络.";
        String requestMsgGpsStr = "GPS定位功能.";
        if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
            requestMsgHintStr = requestMsgHintStr + requestMsgMobileStr;
        } else {
            requestMsgHintStr = requestMsgHintStr + requestMsgGpsStr;
        }
        builder.setMessage(requestMsgHintStr);

        String confirmStr = getResources().getString(R.string.ok);
        builder.setPositiveButton(confirmStr,
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        arg0.dismiss();
                        if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                            // 打开移动网络
                        } else {
                            Log.i(TAG, "试图打开GPS服务");
                            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 101);// 这里使用的是Intent将数据返回给上一个活动
                        }
                    }
                });
        String cancelStr = getResources().getString(R.string.cancel);
        builder.setNegativeButton(cancelStr,
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        arg0.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    //public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101) {
            if (!gpsIsEnable()) {
                String requestGpsFailedStr = "请求服务失败，请手动打开";
                Toast.makeText(this, requestGpsFailedStr, Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "GPS服务开启成功");
                Toast.makeText(this, "GPS服务开启成功", Toast.LENGTH_SHORT).show();
                String strAction = "BroadcastRequestGpsResult";
                Bundle bundle = new Bundle();
                Intent intent = new Intent(strAction);
                bundle.putInt("reqGpsResult", 1);
                intent.putExtras(bundle);
                sendBroadcast(intent);
            }
        }
    }

    @Override
    void afterRequestPermission(int requestCode, boolean isAllGranted) {

    }

    public void Dologin(View view) {//登录按钮响应事件
        String uid = et_id.getText().toString().trim();
        String upass = et_pass.getText().toString().trim();

        //子线程执行
        new Thread(new MyThread(uid, upass)).start();
        // 提示框
        dialog = new ProgressDialog(Login.this);
        dialog.setTitle("提示");
        dialog.setMessage("正在登录，请稍后...");
        dialog.setCancelable(false);
        dialog.show();

        loginTimer = new CountDownTimer(20 * 1000, 10 * 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                //获得系统键盘服务
                manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //隐藏键盘防止遮挡Toast信息
                manager.hideSoftInputFromWindow(Login.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                Toast.makeText(Login.this, "登录失败", Toast.LENGTH_LONG).show();
            }
        };
    }

    public class MyThread implements Runnable {
        private String uid;
        private String upass;

        public MyThread(String userid, String pass) {
            this.uid = userid;
            this.upass = pass;
        }

        @Override
        public void run() {
            loginInfo = new LoginInfo();
            String info = new HttpHelper().executeHttpPost(getString(R.string.ServletAddress) + "login", Constants.selectinfo + "", uid, upass);
            Log.i(TAG, "MyThread().info = " + info);
            if (info != null) {
                try {
                    JSONArray jsonArray = new JSONArray(info);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        loginInfo.setState(jsonObject.getInt("state"));
                        loginInfo.setStudentid(jsonObject.getString("studentid"));
                        loginInfo.setRole(jsonObject.getString("role"));
                        loginInfo.setFaceInfo(jsonObject.getString("faceInfo"));
                        loginInfo.setShouldSigned(jsonObject.getString("shouldSigned"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (loginInfo.getState() == Constants.success) {//登录成功
                    loginResultStr = "登录成功";
                    Message msg = new Message();
                    Log.i(TAG, "send LOGIN_SUCCESS_MSG.MSG = " + Constants.success);
                    msg.what = 9;
                    mHandler.sendMessage(msg);
                } else
                    loginResultStr = "登录失败，账号或密码错误";//登录失败，请重新登录
            } else
                loginResultStr = "连接失败,请检查网络状态";//未连接网络
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    if (!(loginResultStr.equals("登录成功")))
                        Toast.makeText(Login.this, loginResultStr, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Log.i(TAG, "receive a message. msg.what = " + msg.what);
            switch (msg.what) {
                case 9://登录成功消息
                    if (loginInfo.getShouldSigned().equals("0") && loginInfo.getRole().equals("0")) {//学生 but老师没有发起签到
                        Toast.makeText(Login.this, "请等待老师发起签到", Toast.LENGTH_LONG).show();
                        break;
                    }
                    if (loginInfo.getRole().equals("0") && loginInfo.getFaceInfo().equals("")) {//学生 but无脸部特征数据
                        Toast.makeText(Login.this, loginResultStr, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Login.this, SingleImageActivity.class);
                        intent.putExtra("studentid", loginInfo.getStudentid());
                        intent.putExtra("role", loginInfo.getRole());
                        startActivity(intent);
                        finish();
                        break;
                    }
                    if (loginInfo.getRole().equals("0")) {//学生
                        Toast.makeText(Login.this, loginResultStr, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Login.this, FaceAttrPreviewActivity.class);
                        intent.putExtra("studentid", loginInfo.getStudentid());
                        intent.putExtra("role", loginInfo.getRole());
                        intent.putExtra("faceinfo", new HttpHelper().base64decode(loginInfo.getFaceInfo()));
                        Log.i(TAG, "receive " + new HttpHelper().base64decode(loginInfo.getFaceInfo()).length);
                        startActivity(intent);
                        finish();
                        break;
                    } else if (loginInfo.getRole().equals("1")) {//老师
                        Toast.makeText(Login.this, loginResultStr, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Login.this, teacher.class);
                        intent.putExtra("studentid", loginInfo.getStudentid());
                        intent.putExtra("role", loginInfo.getRole());
                        startActivity(intent);
                        finish();
                        break;
                    } else {//管理员
                        Intent intent = new Intent(Login.this, admin.class);
                        intent.putExtra("studentid", loginInfo.getStudentid());
                        intent.putExtra("role", loginInfo.getRole());
                        startActivity(intent);
                        finish();
                        break;
                    }
                default:
                    break;
            }
        }
    };
}

