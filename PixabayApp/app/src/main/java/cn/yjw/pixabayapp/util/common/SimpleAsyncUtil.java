package cn.yjw.pixabayapp.util.common;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yinjiawei
 * @date 2020/12/24
 */
public class SimpleAsyncUtil {
    private static final String TAG = "SimpleAsyncUtil";

    /**
     * 获取线程池对象
     *
     * @return ThreadPoolExecutor
     */
    public static ThreadPoolExecutor getExecutor() {
        return InnerCreator.EXECUTOR;
    }

    /**
     * 提交任务
     *
     * @param task 需要线程池执行的任务
     */
    public static void submitTask(@NonNull Runnable task) {
        try {
            Log.d(TAG, "getExecutor: " + InnerCreator.EXECUTOR.toString());
            getExecutor().submit(task);
        } catch (RejectedExecutionException ex) {
            Log.e(TAG, "submitTask: task has been rejected", ex);
        }
    }

    /**
     * 关闭线程池
     */
    public static void destroy() {
        getExecutor().shutdown();
        Log.d(TAG, "getExecutor: " + InnerCreator.EXECUTOR.toString());
    }

    /**
     * @return asyncResult
     */
    public static Object getAsyncResult() {
        return getAsyncLocal().get();
    }

    /**
     * 保存异步结果
     *
     * @param result asyncResult
     */
    public static void setAsyncResult(Object result) {
        getAsyncLocal().set(result);
    }

    /**
     * 显示移除本线程所保存的值
     */
    public static void removeAsyncResult() {
        getAsyncLocal().remove();
    }

    /**
     * 获取threadLocal
     *
     * @return ThreadLocal
     */
    private static ThreadLocal<Object> getAsyncLocal() {
        return InnerCreator.ASYNC_RESULT;
    }

    private static class InnerCreator {
        /**
         * 创建线程池
         */
        private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
                4,
                16,
                60L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(8));
        /**
         * 创建ThreadLocal来记录每个线程的异步结果
         */
        private static final ThreadLocal<Object> ASYNC_RESULT = new ThreadLocal<>();
    }

    private SimpleAsyncUtil() {
    }
}
