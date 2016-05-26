package com.github.rain.net.callback;


import com.github.rain.net.error.CallError;

import okhttp3.Response;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-05-20 15:59
 */
public interface Callback<T> {

    void onPreExecute();

    void onProgress(float progress, float length);

    void onFinish();

    void onError(CallError error);

    void onResponse(T response);

    void onCancel();

    T parseNetworkResponse(Response response) throws Exception;


}
