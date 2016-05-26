package com.github.rain.net.callback;


import com.github.rain.net.RestAndroidHttp;
import com.github.rain.net.error.CallError;
import com.github.rain.net.error.HttpError;

import org.apache.http.HttpStatus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-05-20 16:21
 */
public class CallbackWraper implements okhttp3.Callback {

    private Callback mCallback;

    public CallbackWraper(Callback callback) {
        mCallback = callback;
        mCallback.onPreExecute();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (call.isCanceled()) {
            sendCancel();
            return;
        }

        sendError(new CallError(e));

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {

            // build error
            if (response.code() != HttpStatus.SC_OK) {
                sendError(new HttpError(response.code()));
                return;
            }

            Object data = mCallback.parseNetworkResponse(response);
            sendSuccess(data);
        } catch (Exception e) {
            sendError(new CallError(e));
        }


    }

    private void sendError(final CallError error) {
        RestAndroidHttp.getInstance().delivery(new Runnable() {
            @Override
            public void run() {

                mCallback.onError(error);
                mCallback.onFinish();
            }
        });

    }

    private void sendSuccess(final Object data) {
        RestAndroidHttp.getInstance().delivery(new Runnable() {
            @Override
            public void run() {
                mCallback.onResponse(data);
                mCallback.onFinish();
            }
        });

    }

    private void sendCancel() {
        RestAndroidHttp.getInstance().delivery(new Runnable() {
            @Override
            public void run() {
                mCallback.onCancel();
                mCallback.onFinish();
            }
        });

    }


}
