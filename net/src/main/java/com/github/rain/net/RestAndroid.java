/*
 * RestVolley      2015-12-16
 * Copyright (c) 2015 hujiang Co.Ltd. All right reserved(http://www.hujiang.com).
 * 
 */

package com.github.rain.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-04-06 17:11
 */
public class RestAndroid {

    /**
     * default request engine tag.
     */
    public static final String TAG_REQUEST_ENGINE_DEFAULT = "restvolley_default_request_engine_Tag";

    private static RestAndroid REST_VOLLEY;

    private Map<String, RequestEngine> sRequestEngineMap = new HashMap<String, RequestEngine>();

    public static RestAndroid getInstance() {

        if (REST_VOLLEY == null) {
            synchronized (RestAndroid.class) {
                if (REST_VOLLEY == null)
                    REST_VOLLEY = new RestAndroid();
            }
        }

        return REST_VOLLEY;
    }

    /**
     * create a default http engine with tag that contains OkHttpClient and RequestQueue
     *
     * @param context
     */
    public void init(Context context) {
        setDefaultRequestEngineConfig(new RequestEngineConfig(context));
    }

    /**
     * set engine conifg with tag
     *
     * @param engineTag
     * @param requestEngineConfig
     */
    public void setRequestEngineConfig(String engineTag, RequestEngineConfig requestEngineConfig) {
        RequestEngine requestEngine = sRequestEngineMap.get(engineTag);
        if (requestEngine == null)
            newRequestEngine(engineTag, requestEngineConfig);
        else
            requestEngine.setRequestEngineConfig(requestEngineConfig);
    }

    /**
     * set default engine config
     *
     * @param requestEngineConfig
     */
    public void setDefaultRequestEngineConfig(RequestEngineConfig requestEngineConfig) {
        setRequestEngineConfig(TAG_REQUEST_ENGINE_DEFAULT, requestEngineConfig);
    }

    private RestAndroid() {
    }

    /**
     * create a new http engine with tag that contains OkHttpClient and RequestQueue.
     * <br>
     * <br>
     * if the http engine with the special tag exists, return the existing http engine, otherwise create a new http engine and return.
     *
     * @param engineTag           http engine Tag related to the http engine.
     * @param requestEngineConfig
     *
     * @return HttpEngine.
     */
    public RequestEngine newRequestEngine(String engineTag, RequestEngineConfig requestEngineConfig) {
        RequestEngine requestEngine = sRequestEngineMap.get(engineTag);
        if (requestEngine == null) {
            requestEngine = new RequestEngine(requestEngineConfig);
            sRequestEngineMap.put(engineTag, requestEngine);
        }

        return requestEngine;
    }


    /**
     * create a new http engine with tag that contains OkHttpClient and RequestQueue.
     * <br>
     * <br>
     * if the http engine with the special tag exists, return the existing http engine, otherwise create a new http engine and return.
     *
     * @param engineTag
     * @param okHttpClient
     * @param requestEngineConfig
     *
     * @return
     */
    public RequestEngine newRequestEngine(String engineTag, OkHttpClient okHttpClient, RequestEngineConfig requestEngineConfig) {
        RequestEngine requestEngine = sRequestEngineMap.get(engineTag);
        if (requestEngine == null) {
            requestEngine = new RequestEngine(okHttpClient, requestEngineConfig);
            sRequestEngineMap.put(engineTag, requestEngine);
        }

        return requestEngine;
    }


    /**
     * return default request engine.
     *
     * @return RequestEngine
     */
    public RequestEngine getDefaultRequestEngine() {
        return sRequestEngineMap.get(TAG_REQUEST_ENGINE_DEFAULT);
    }

    /**
     * cancel all request with the tag that int the specified {@link RequestEngine}
     *
     * @param requestEngine {@link RequestEngine}
     * @param tag           request tag.
     */
    public void cancelAll(RequestEngine requestEngine, Object tag) {
        if (requestEngine != null) {
            requestEngine.cancelAll(tag);
        }
    }

    /**
     * cancel all request in the specified {@link RequestEngine}.
     *
     * @param requestEngine {@link RequestEngine}
     */
    public static void cancelAll(RequestEngine requestEngine) {
        if (requestEngine != null) {
            requestEngine.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
    }

    /**
     * stop the request int the specified {@link RequestEngine}.
     *
     * @param requestEngine {@link RequestEngine}
     */
    public static void stop(RequestEngine requestEngine) {
        if (requestEngine != null) {
            requestEngine.stop();
        }
    }
}