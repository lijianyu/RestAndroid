package com.github.rain.rest;

import android.app.Application;

import com.github.rain.net.RestAndroidHttp;
import com.github.rain.net.log.LoggerInterceptor;

import okhttp3.OkHttpClient;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-05-20 10:27
 */
public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        RestAndroidHttp.getInstance().builder(new RestAndroidHttp.Builder()
                .okBuilder(new OkHttpClient.Builder()
                        .addInterceptor(new LoggerInterceptor("", true)))
                .endPoint("https://api.github.com/"));

    }
}
