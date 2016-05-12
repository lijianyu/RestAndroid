/*
 * HttpEngine      2016-01-05
 * Copyright (c) 2016 hujiang Co.Ltd. All right reserved(http://www.hujiang.com).
 * 
 */

package com.github.rain.net;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * contains {@link RequestQueue} of the {@link com.android.volley.toolbox.Volley} and the {@link OkHttpClient} that requesting data from the network.
 *
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-04-06 17:11
 */
public class RequestEngine {
    /**
     * {@link RequestQueue} of the {@link com.android.volley.toolbox.Volley}.
     */
    protected RequestQueue requestQueue;

    public RequestEngineConfig requestEngineConfig;

    private OkHttpClient okHttpClient;

    RequestEngine(RequestEngineConfig requestEngineConfig) {
        this(new OkHttpClient(), requestEngineConfig);
    }

    RequestEngine(OkHttpClient okHttpClient, final RequestEngineConfig requestEngineConfig) {
        this.okHttpClient = okHttpClient;

        if (this.okHttpClient == null)
            this.okHttpClient = new OkHttpClient();

        setRequestEngineConfig(requestEngineConfig);
    }

    public void setRequestEngineConfig(final RequestEngineConfig requestEngineConfig) {
        this.requestEngineConfig = requestEngineConfig;
        this.okHttpClient = this.okHttpClient.newBuilder()
                .cookieJar(this.requestEngineConfig.getCookieJar() == null ?
                        new CookieJar() {
                            @Override
                            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {

                            }

                            @Override
                            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                                return new ArrayList<>();
                            }
                        } : this.requestEngineConfig.getCookieJar())
                .build();

        if (requestQueue != null)
            requestQueue.stop();
        requestQueue = null;

        requestQueue = Volley.newRequestQueue(new OkHttpStack(this.okHttpClient),
                this.requestEngineConfig.getCache());
        requestQueue.start();
    }

    /**
     * cancel all the request associated wht the tag from the {@link com.android.volley.toolbox.Volley} {@link RequestQueue}.
     *
     * @param tag request tag.
     */
    public void cancelAll(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

    /**
     * cancel all the request in the {@link RequestQueue} of the {@link com.android.volley.toolbox.Volley}.
     */
    public void cancelAll() {
        if (requestQueue != null) {
            requestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
    }

    /**
     * {@link RequestQueue} stop work.
     */
    public void stop() {
        if (requestQueue != null) {
            requestQueue.stop();
        }
    }
}