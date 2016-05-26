package com.github.rain.net;

import android.os.Handler;
import android.os.Looper;

import okhttp3.OkHttpClient;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-05-20 16:51
 */
public class RestAndroidHttp {

    private static RestAndroidHttp mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Builder mBuilder;


    private RestAndroidHttp(Builder builder) {
        mBuilder = builder;

        OkHttpClient.Builder okBuilder = builder.mOkBuilder;

        if (okBuilder == null) {
            mOkHttpClient = new OkHttpClient.Builder().build();
        } else {
            mOkHttpClient = okBuilder.build();
        }

        mDelivery = new Handler(Looper.getMainLooper());

    }


    public static RestAndroidHttp getInstance() {
        if (mInstance == null) {
            synchronized (RestAndroidHttp.class) {
                if (mInstance == null) {
                    mInstance = new RestAndroidHttp(new Builder());
                }
            }
        }
        return mInstance;
    }

    public RestAndroidHttp builder(Builder builder) {
        if (builder == null)
            return this;

        mBuilder = builder;
        mOkHttpClient = mBuilder.mOkBuilder.build();

        return this;
    }

    public Builder builder() {
        return mBuilder;
    }


    /**
     * get http call client
     *
     * @return
     */
    public OkHttpClient okHttpClient() {
        return mOkHttpClient;
    }

    /**
     * delivery runnable to mainthread
     *
     * @param runnable
     */
    public void delivery(Runnable runnable) {
        mDelivery.post(runnable);
    }

    public String endPoint() {
        return mBuilder.mEndPoint;
    }

    public static class Builder {
        private OkHttpClient.Builder mOkBuilder;
        private String mEndPoint;

        public Builder() {
            mOkBuilder = new OkHttpClient.Builder();
            mEndPoint = "";
        }

        public Builder(OkHttpClient client) {
            mOkBuilder = client.newBuilder();
            mEndPoint = "";
        }

        public Builder okBuilder(OkHttpClient.Builder builder) {
            mOkBuilder = builder;
            return this;
        }

        /**
         * set a http url endPoint, such as "http://192.168.1.1/"
         *
         * @param endPoint
         *
         * @return
         */
        public Builder endPoint(String endPoint) {
            this.mEndPoint = endPoint;
            return this;
        }
    }

}
