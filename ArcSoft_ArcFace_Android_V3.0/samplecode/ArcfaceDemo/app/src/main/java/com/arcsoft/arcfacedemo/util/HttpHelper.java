package com.arcsoft.arcfacedemo.util;

import android.os.Build;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Base64;

public class HttpHelper {
    private static final String TAG = "HttpHelper";

    public String base64encode(byte[] b) {//byte数组转为base64字符串
        String base64Str = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            base64Str = Base64.getEncoder().encodeToString(b);
        }
        return base64Str;
    }
    public byte[] base64decode(String s){//base64字符串转为byte数组
        byte[] byteOfbase64=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            byteOfbase64=Base64.getDecoder().decode(s);
        }
        return byteOfbase64;
    }

    // 通过 POST 方式获取HTTP服务器数据
    public String executeHttpPost(String url, String type, String userid, String info) {//登录+更新
        Log.i(TAG, "executeHttpPost()."+userid+" "+info);
        try {
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", type);
            params.put("userid", userid);
            params.put("info", info);
            return sendPOSTRequest(url, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String executeHttpPost(String url, String type, String userid, byte[] faceinfo) {//录入人脸库
        Log.i(TAG, "executeHttpPost()."+this.base64encode(faceinfo).length());
        try {
            String base64_faceinfo =this.base64encode(faceinfo);//将人脸特征信息转换成base64字符串
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", type);
            params.put("userid", userid);
            params.put("faceinfo", base64_faceinfo);
            return sendPOSTRequest(url, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String executeHttpPost(String url, String type,String _allid) {//签到
        Log.i(TAG, "executeHttpPost()."+_allid);
        try {
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", type);
            params.put("_allid", _allid);
            return sendPOSTRequest(url, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 处理发送数据请求
    private String sendPOSTRequest(String url, Map<String, String> params, String encoding) throws Exception {
        Log.i(TAG, "sendPOSTRequest().");
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, encoding);
        HttpPost post = new HttpPost(url);
        post.setEntity(entity);
        DefaultHttpClient client = new DefaultHttpClient();
        // 请求超时
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        // 读取超时
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        try {
            HttpResponse response = client.execute(post);
            // 判断是否成功收取信息
            if (response.getStatusLine().getStatusCode() == 200) {
                return getInfo(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 未成功收取信息，返回空指针
        return null;
    }

    // 收取数据
    private String getInfo(HttpResponse response) throws Exception {
        Log.i(TAG, "getInfo().");
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        // 将输入流转化为byte型
        byte[] data = new HttpHelper().read(is);
        // 转化为字符串
        return new String(data, "UTF-8");
    }

    public byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inStream.close();
        return outputStream.toByteArray();
    }
}
