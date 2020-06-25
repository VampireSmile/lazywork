package com.arcsoft.arcfacedemo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.arcfacedemo.R;
import com.arcsoft.arcfacedemo.common.Constants;
import com.arcsoft.arcfacedemo.util.HttpHelper;

public class teacher extends AppCompatActivity {
    private static final String TAG = "teacher";
    private int TIME = 200;
    private String loginResultStr = "处理中...";
    private CountDownTimer loginTimer;
    private LinearLayout liner;
    private int Count = 0;
    private String allid;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        final TextView tvaa = findViewById(R.id.tv_aa);
        liner = findViewById(R.id.checkbox);
        Count = liner.getChildCount();
        final Button btn_begin = findViewById(R.id.beginSigned);
        final EditText et_time = findViewById(R.id.et_time);

        btn_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = et_time.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    TIME = Integer.parseInt(s.trim());
                }
                new Thread(new MyThread()).start();
                btn_begin.setClickable(false);
                btn_begin.setEnabled(false);//按钮置灰
                liner.setVisibility(View.INVISIBLE);
                tvaa.setVisibility(View.INVISIBLE);
                et_time.setEnabled(false);
                // 提示框
                dialog = new ProgressDialog(teacher.this);
                dialog.setTitle("提示");
                dialog.setMessage("正在签到中，请稍等...");
                dialog.setCancelable(false);
                dialog.show();
                loginTimer = new CountDownTimer((TIME + 1) * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        btn_begin.setText(loginResultStr + (TIME--) + "s");
                    }

                    @Override
                    public void onFinish() {
                        Toast.makeText(teacher.this, "签到结束", Toast.LENGTH_LONG).show();
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                            Toast.makeText(teacher.this, "获取数据失败", Toast.LENGTH_LONG).show();
                        }
                        Intent intent = new Intent(teacher.this, dealdata.class);
                        intent.putExtra("allid", allid);
                        startActivity(intent);
                        finish();
                    }
                };
                loginTimer.start();
            }
        });
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Count; i++) {
                //根据i 拿到每一个CheckBox
                CheckBox cb = (CheckBox) liner.getChildAt(i);
                //判断CheckBox是否被选中
                if (cb.isChecked()) {
                    //把被选中的文字添加到StringBuffer中
                    sb.append(cb.getText().toString() + ",");
                }
            }
            Log.i(TAG, "string info = " + sb.toString());
            allid = sb.toString();
            String getinfo = new HttpHelper().executeHttpPost(getString(R.string.ServletAddress) + "beginSigned", Constants.updateinfo + "3", allid);
            Log.i(TAG, "MyThread().info = " + getinfo);

            if (getinfo.equals(Constants.success + "")) {//成功
                loginResultStr = "发起成功,正在签到:";
                Log.i(TAG, "send _SUCCESS_MSG.MSG = " + Constants.success);
            } else {//失败
                loginResultStr = "发起失败,请重新发起签到";
            }

        }
    }

}
