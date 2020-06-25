package com.arcsoft.arcfacedemo.common;

public class Constants {

    public static final String APP_ID = "3BMLJYEVoAzGfumwnQKSoEiMdnsKaxWvX2D6rPpsRRei";
    public static final String SDK_KEY = "Gs7V2ncVUTH6HChdvseBq9zPA3YEwKmV7nqGQqHKw9ER";

    //请求服务器执行方法
    public static final int selectinfo = 101;//查询
    public static final int updateinfo = 102;//更新
    public static final int insertinfo = 103;//插入
    //public static final int deleteinfo = 103;
    //服务返回信息
    public static final int success = 666;//成功
    public static final int fail = 111;//失败
    /**
     * IR预览数据相对于RGB预览数据的横向偏移量，注意：是预览数据，一般的摄像头的预览数据都是 width > height
     */
    public static final int HORIZONTAL_OFFSET = 0;
    /**
     * IR预览数据相对于RGB预览数据的纵向偏移量，注意：是预览数据，一般的摄像头的预览数据都是 width > height
     */
    public static final int VERTICAL_OFFSET = 0;
}

