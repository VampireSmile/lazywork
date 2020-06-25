package com.arcsoft.arcfacedemo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.arcfacedemo.R;
import com.arcsoft.arcfacedemo.common.Constants;
import com.arcsoft.arcfacedemo.util.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dealdata extends AppCompatActivity {
    private String TAG = "dealdata";
    private String loginResultStr;
    private ProgressDialog dialog;
    private static Handler handler = new Handler();
    private CountDownTimer loginTimer;
    private String alluid;
    private List<Map<String, String>> infolist = null;
    private ListView allstulist;
    private android.support.v7.widget.SearchView searchView;
    private adapter myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealdata);
        Intent intent = getIntent();
        alluid = intent.getStringExtra("allid");
        //子线程执行
        new Thread(new MyThread()).start();
        // 提示框
        dialog = new ProgressDialog(dealdata.this);
        dialog.setTitle("提示");
        dialog.setMessage("正在获取签到数据，请稍后...");
        dialog.setCancelable(false);
        dialog.show();

        loginTimer = new CountDownTimer(30 * 1000, 10 * 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                    Toast.makeText(dealdata.this, "获取数据失败", Toast.LENGTH_LONG).show();
                }
            }
        };
        allstulist = findViewById(R.id.teacher_listview);
        allstulist.setTextFilterEnabled(true);
        searchView = (android.support.v7.widget.SearchView) findViewById(R.id.select_args);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("请输入关键字查询");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {//设置searchview的监听
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Filter filter = ((Filterable) myadapter).getFilter();//注意这个adapter和lv中一致，不要重新new一个
                if (!TextUtils.isEmpty(s)) {
                    filter.filter(s);
                } else {
                    filter.filter(null);
                }
                return true;
            }
        });
    }

    class adapter extends BaseAdapter implements Filterable {//数据适配器
        private List<Map<String, String>> mList;//这个数据会改变的
        private List<Map<String, String>> backData;//用于备份原始数据

        public adapter(List<Map<String, String>> data) {
            mList = data;
            backData = data;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(dealdata.this).inflate(R.layout.list_item, viewGroup, false);
                holder = new ViewHolder();
                holder.item_id = view.findViewById(R.id.tv_id);
                holder.item_name = view.findViewById(R.id.tv_item_name);
                holder.item_dept = view.findViewById(R.id.tv_item_dept);
                holder.item_state = view.findViewById(R.id.tv_state);
                holder.item_time = view.findViewById(R.id.tv_time);
                holder.item_location = view.findViewById(R.id.tv_location);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.item_id.setText("学号:" + mList.get(i).get("id"));
            holder.item_name.setText("姓名:" + mList.get(i).get("name"));
            holder.item_dept.setText("专业班级:" + mList.get(i).get("dept"));
            holder.item_state.setText("签到状态:" + mList.get(i).get("state"));
            if (mList.get(i).get("state").equals("未签到")) {
                holder.item_state.setTextColor(Color.rgb(255, 0, 0));//修改颜色
            } else holder.item_state.setTextColor(Color.rgb(0, 255, 0));
            holder.item_time.setText("签到时间:" + mList.get(i).get("time"));
            holder.item_location.setText("签到地点:" + mList.get(i).get("location"));
            return view;
        }

        @Override
        public Filter getFilter() {
            return new MyFilter();
        }

        class MyFilter extends Filter {//设置过滤器的过滤规则

            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Map<String, String>> list;
                if (TextUtils.isEmpty(constraint)) {//当过滤关键词未空我们显示所有数据
                    list = backData;
                } else {//否则把符合的数据对象添加到集合中
                    list = new ArrayList<>();
                    for (Map<String, String> mComplainListBean : backData) {
                        //设置过滤的关键词
                        if (mComplainListBean.get("id").contains(constraint) || mComplainListBean.get("name").contains(constraint)
                                || mComplainListBean.get("dept").contains(constraint) || mComplainListBean.get("state").contains(constraint)
                                || mComplainListBean.get("time").contains(constraint) || mComplainListBean.get("location").contains(constraint)) {
                            Log.d("performFiltering:", mComplainListBean.toString());
                            list.add(mComplainListBean);
                        }
                    }
                }
                results.values = list;//将得到的集合保存到FilterResult 的value变量中
                results.count = list.size();//将集合的大小保存到FilterResult的count变量中
                return results;
            }

            protected void publishResults(CharSequence constraint, FilterResults results) {
                mList = (List<Map<String, String>>) results.values;
                Log.d("publishResults", "" + results.count);
                if (results.count > 0) {
                    //当数据发生改变
                    notifyDataSetChanged();
                    Log.d("publishResults", "notifyDataSetChanged");
                } else {
                    //当数据没有改变
                    notifyDataSetInvalidated();
                    Log.d("publishResults", "notifyDataSetInvalidated");
                }
            }
        }
    }

    class ViewHolder {
        TextView item_id;
        TextView item_name;
        TextView item_dept;
        TextView item_state;
        TextView item_time;
        TextView item_location;
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            String getinfo = new HttpHelper().executeHttpPost(getString(R.string.ServletAddress) + "teacherdealdata", Constants.selectinfo + "", alluid);
            Log.i(TAG, "MyThread().info = " + getinfo);
            if (getinfo != null) {//成功
                loginResultStr = "获取数据成功";
                try {
                    infolist = new ArrayList<Map<String, String>>();
                    JSONArray jsonArray = new JSONArray(getinfo);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Map<String, String> map = new HashMap<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        map.put("id", jsonObject.getString("id"));
                        map.put("name", jsonObject.getString("name"));
                        map.put("dept", jsonObject.getString("dept"));
                        if (jsonObject.getString("state").equals("0")) {
                            map.put("state", "未签到");
                        } else map.put("state", "已签到");
                        map.put("time", jsonObject.getString("time"));
                        map.put("location", jsonObject.getString("location"));
                        infolist.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {//没得数据
                loginResultStr = "获取数据失败";
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(dealdata.this, loginResultStr, Toast.LENGTH_LONG).show();
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    myadapter = new adapter(infolist);
                    allstulist.setAdapter(myadapter);
                }
            });
        }
    }
}
