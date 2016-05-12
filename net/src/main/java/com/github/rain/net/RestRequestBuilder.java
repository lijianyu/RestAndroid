package com.github.rain.net;

import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;

import org.apache.http.protocol.HTTP;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-04-06 14:07
 */
public class RestRequestBuilder implements RequestParams {
    /**
     * http request engine {@link RequestEngine}.
     */
    private RequestEngine requestEngine;

    /**
     * request body content type.
     */
    private String contentType = RequestEngineConfig.APPLICATION_JSON;
    /**
     * request body char set.
     */
    private String charset = HTTP.UTF_8;
    /**
     * method.
     */
    private int method;
    /**
     * request url.
     */
    private String url;

    /**
     * sequence id.
     */
    private int sequence;
    /**
     * should cache or not.
     */
    private boolean shouldCache = false;
    /**
     * {@link RetryPolicy}.
     */
    private DefaultRetryPolicy retryPolicy;
    /**
     * {@link com.android.volley.Cache.Entry}.
     */
    private Cache.Entry cacheEntry;
    /**
     * request tag.
     */
    private Object tag;
    /**
     * request marker.
     */
    private String marker;
    /**
     * request priority {@link com.android.volley.Request.Priority}, default is {@link com.android.volley.Request.Priority#NORMAL}.
     */
    private Request.Priority priority = Request.Priority.NORMAL;

    /**
     * request headers.
     */
    private final Map<String, String> headers = new HashMap<>();
    /**
     * request  params.
     */
    private final Map<String, String> params = new HashMap<>();
    /**
     * request urlParams.
     */
    private final Map<String, String> urlParams = new HashMap<>();

    private String urlPath;


    public RestRequestBuilder() {
        requestEngine = RestAndroid.getInstance().getDefaultRequestEngine();
        headers().putAll(requestEngine.requestEngineConfig.getHeaders());

        retryPolicy = new DefaultRetryPolicy();
        retryPolicy.setCurrentTimeoutMs(requestEngine.requestEngineConfig.getTimeOut());
        retryPolicy.setCurrentRetryCount(requestEngine.requestEngineConfig.getRetries());
    }


    /**
     * add request header.
     *
     * @param key   header key
     * @param value header value
     *
     * @return {@link RestRequest}
     */
    public RestRequestBuilder addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    /**
     * add more request headers.
     *
     * @param headers more headers in map
     *
     * @return {@link RestRequest}
     */
    public RestRequestBuilder addHeaders(Map<String, String> headers) {
        if (headers != null) {
            Set<String> keySet = headers.keySet();
            for (String key : keySet) {
                this.headers.put(key, headers.get(key));
            }
        }
        return this;
    }


    /**
     * add request String  params.
     *
     * @param key   param's key
     * @param value param's value
     *
     * @return {@link RestRequest}
     */
    public RestRequestBuilder addParams(String key, String value) {
        if (key != null && value != null) {
            this.params.put(key, value);
        }

        return this;
    }

    /**
     * add request Object  params.
     *
     * @param key   param's key
     * @param value param's value
     *
     * @return {@link RestRequest}
     */
    public RestRequestBuilder addParams(String key, Object value) {
        if (key != null && value != null) {
            this.params.put(key, value.toString());
        }

        return this;
    }

    /**
     * add request int  params.
     *
     * @param key   param's key
     * @param value param's value
     *
     * @return {@link RestRequest}
     */
    public RestRequestBuilder addParams(String key, int value) {
        if (key != null) {
            this.params.put(key, String.valueOf(value));
        }

        return this;
    }

    /**
     * add request long  params.
     *
     * @param key   param's key
     * @param value param's value
     *
     * @return {@link RestRequest}
     */
    public RestRequestBuilder addParams(String key, long value) {
        if (key != null) {
            this.params.put(key, String.valueOf(value));
        }

        return this;
    }


    /**
     * add request String url params.
     *
     * @param key   url param's key
     * @param value url param's value
     *
     * @return {@link RestRequest}
     */
    public RestRequestBuilder addUrlParams(String key, String value) {
        if (key != null && value != null) {
            this.urlParams.put(key, value);
        }

        return this;
    }

    /**
     * add request Object url params.
     *
     * @param key   param's key
     * @param value param's value
     *
     * @return {@link RestRequest}
     */
    public RestRequestBuilder addUrlParams(String key, Object value) {
        if (key != null && value != null) {
            this.urlParams.put(key, value.toString());
        }

        return this;
    }

    /**
     * add request int url params.
     *
     * @param key   param's key
     * @param value param's value
     *
     * @return {@link RestRequest}
     */
    public RestRequestBuilder addUrlParams(String key, int value) {
        if (key != null) {
            this.urlParams.put(key, String.valueOf(value));
        }

        return this;
    }

    /**
     * add request long url params.
     *
     * @param key   param's key
     * @param value param's value
     *
     * @return {@link RestRequest}
     */
    public RestRequestBuilder addUrlParams(String key, long value) {
        if (key != null) {
            this.urlParams.put(key, String.valueOf(value));
        }

        return this;
    }


    /**
     * set connect timeout, read timeout, write timeout with the same time in mills.
     *
     * @param timeMillis time in mills.
     *
     * @return {@link RestRequest}
     */
    public RestRequestBuilder timeout(int timeMillis) {
        retryPolicy.setCurrentTimeoutMs(timeMillis);
        return this;
    }

    /**
     * set connect retries
     *
     * @param retries
     *
     * @return
     */
    public RestRequestBuilder retries(int retries) {

        retryPolicy.setCurrentRetryCount(retries);
        return this;
    }

    /**
     * set request tag that you can cancel the request with it, eg: {@link RestAndroid#cancelAll(RequestEngine, Object)}.
     *
     * @param tag request tag
     *
     * @return {@link RestRequest}
     *
     * @see com.android.volley.Request#setTag(Object)
     */
    public RestRequestBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }


    /**
     * set url.
     *
     * @param url request url
     *
     * @return {@link RestRequest}
     */
    public RestRequestBuilder url(String url) {
        this.url = url;

        return this;
    }

    /**
     * build a request
     *
     * @param method {@link com.android.volley.Request.Method}
     *
     * @return
     */
    public RestRequestBuilder method(int method) {
        this.method = method;
        return this;
    }

    /**
     * build a post request
     *
     * @return
     */
    public RestRequestBuilder post() {
        this.method = Request.Method.POST;
        return this;
    }

    /**
     * build a get request
     *
     * @return
     */
    public RestRequestBuilder get() {
        this.method = Request.Method.GET;
        return this;
    }

    /**
     * build a put request
     *
     * @return
     */
    public RestRequestBuilder put() {
        this.method = Request.Method.PUT;
        return this;
    }

    /**
     * build a delete request
     *
     * @return
     */
    public RestRequestBuilder delete() {
        this.method = Request.Method.DELETE;
        return this;
    }

    /**
     * when set url, the path will fail.
     *
     * @param path
     *
     * @return
     */
    public RestRequestBuilder urlPath(String path) {
        this.urlPath = path;
        return this;
    }

    /**
     * @param requestEngine
     *
     * @return
     */
    public RestRequestBuilder requestEngine(RequestEngine requestEngine) {
        this.requestEngine = requestEngine;
        return this;
    }

    public RestRequest build() {

        RestRequest request = new RestRequest(this, requestEngine.requestEngineConfig.getConverter());

        request.setTag(tag);
        request.setRetryPolicy(retryPolicy);

        return request;
    }

    private static String buildUrl(Map<String, String> urlParams, String url) {
        Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
        Set<String> keySet = urlParams.keySet();
        for (String s : keySet) {
            uriBuilder.appendQueryParameter(s, urlParams.get(s));
        }

        return uriBuilder.toString();
    }

    @Override
    public int method() {
        return this.method;
    }

    @Override
    public String url() {
        if (!TextUtils.isEmpty(this.url))
            return this.url;

        if (TextUtils.isEmpty(requestEngine.requestEngineConfig.getEndPoint())) {
            throw new NullPointerException("endPoint or url should not be null");
        }

        return buildUrl(urlParams, requestEngine.requestEngineConfig.getEndPoint() + urlPath);

    }

    @Override
    public Map<String, String> headers() {
        return this.headers;
    }

    @Override
    public RequestEngine engine() {
        return this.requestEngine;
    }

    @Override
    public Map<String, String> params() {
        return this.params;
    }
}
