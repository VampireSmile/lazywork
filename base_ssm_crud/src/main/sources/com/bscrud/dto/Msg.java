package com.bscrud.dto;

import com.bscrud.enums.StatusEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Msg
 * @Description 通用的返回类
 * @Author YinJiaWei
 * @Date 2020/6/18 11:53
 * @Version 1.0
 **/
public class Msg {
    //200成功，100失败
    private int code;
    //提示信息
    private String msg;
    //用户要返回给浏览器的数据
    private Map<String, Object> extend = new HashMap<>();

    public Msg() {
    }

    public Msg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Msg(int code, String msg, Map<String, Object> extend) {
        this(code, msg);
        this.extend = extend;
    }

    //成功信息
    public static Msg successInfo() {
        return new Msg(StatusEnum.SUCCESS.getCode(), "处理成功！");
    }

    //失败信息
    public static Msg failInfo() {
        return new Msg(StatusEnum.FAIL.getCode(), "处理失败！");
    }

    //带上要返回的数据
    public Msg withData(String key, Object value) {
        this.extend.put(key, value);
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, Object> extend) {
        this.extend = extend;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", extend=" + extend +
                '}';
    }
}
