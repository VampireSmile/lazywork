package com.arcsoft.arcfacedemo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arcsoft.arcfacedemo.R;
import com.arcsoft.arcfacedemo.common.Constants;
import com.arcsoft.arcfacedemo.util.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class editPass extends AppCompatActivity {
    private static final String TAG = "editPass";
    private EditText et_name, et_orignPass, et_newPass, et_newPass2;
    private Button btn_DoEdit;
    private String loginResultStr;
    private ProgressDialog dialog;
    private static Handler handler = new Handler();
    private CountDownTimer loginTimer;
    private InputMethodManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pass);
        initview();
    }

    public void initview() {
        et_name = findViewById(R.id.edit_Euserid);
        et_orignPass = findViewById(R.id.edit_Euserpass);
        et_newPass = findViewById(R.id.edit_Eusernewpass);
        et_newPass2 = findViewById(R.id.edit_Eusernewpass2);
        btn_DoEdit = findViewById(R.id.btn_edit_pass);
    }

    public void DoEdit(View view) {//点击事件
        if (TextUtils.isEmpty(et_name.getText().toString())) {
            Toast.makeText(this, "账号不能为空", Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(et_orignPass.getText().toString())) {
            Toast.makeText(this, "原密码不能为空", Toast.LENGTH_LONG).show();
            return;
        } else if (!((et_newPass.getText().toString()).equals(et_newPass2.getText().toString()))) {
            Toast.makeText(this, "输入的两次新账号不一致", Toast.LENGTH_LONG).show();
            return;
        }
        //子线程执行
        new Thread(new MyThread(et_name.getText().toString(), et_orignPass.getText().toString(), et_newPass.getText().toString())).start();
        // 提示框
        dialog = new ProgressDialog(editPass.this);
        dialog.setTitle("提示");
        dialog.setMessage("正在修改，请稍后...");
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
                manager.hideSoftInputFromWindow(editPass.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                Toast.makeText(editPass.this, "修改失败", Toast.LENGTH_LONG).show();
            }
        };
    }

    public class MyThread implements Runnable {
        private String uid;
        private String orignpass;
        private String upass;

        public MyThread(String userid, String orignpass, String pass) {
            this.uid = userid;
            this.orignpass = orignpass;
            this.upass = pass;
        }

        @Override
        public void run() {
            //验证原账号和原密码
            String checkinfo = new HttpHelper().executeHttpPost(getString(R.string.ServletAddress) + "login", Constants.selectinfo + "", uid, orignpass);
            Log.i(TAG, "MyThread().info = " + checkinfo);
            if (checkinfo == null)//未连接网络
                loginResultStr = "连接失败,请检查网络状态";
            else {
                int check = 0;
                try {
                    JSONArray jsonArray = new JSONArray(checkinfo);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    check = jsonObject.getInt("state");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (check == Constants.success) {//源账号和原密码查询成功
                    //修改密码
                    String info = new HttpHelper().executeHttpPost(getString(R.string.ServletAddress) + "editpass", Constants.selectinfo + "", uid, upass);
                    if (info.equals(Constants.success + ""))
                        loginResultStr = "修改成功";
                    else
                        loginResultStr = "修改失败,请重试";
                } else
                    loginResultStr = "原账号或原密码错误,请重试";//失败
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    if (loginResultStr.equals("修改成功")) {
                        Intent intent = new Intent(editPass.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                    Toast.makeText(editPass.this, loginResultStr, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
