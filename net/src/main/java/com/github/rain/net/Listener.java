package com.github.rain.net;

import com.android.volley.VolleyError;

/**
 * 网络访问的回调
 *
 * @param <T>
 */
public abstract class Listener<T> {



    /**
     * 当访问开始执行之前
     */
    public void onPreExecute() {
    }

    /**
     * 当请求执行结束后
     */
    public void onFinish() {
    }

    /**
     * 当访问成功后执行
     */
    public abstract void onSuccess(T result);

    /**
     * 请求出错后执行
     */
    public void onError(VolleyError error) {
    }

    /**
     * 当前请求取消后执行
     */
    public void onCancel() {
    }


}
