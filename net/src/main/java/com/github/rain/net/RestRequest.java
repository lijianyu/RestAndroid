package com.github.rain.net;

import android.net.Uri;

import com.github.rain.net.callback.Callback;
import com.github.rain.net.callback.CallbackWraper;
import com.github.rain.net.requestbody.RequestBodyFactory;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http.HttpMethod;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-05-20 14:11
 */
public class RestRequest {

    private Call mCall;
    private Request mRequest;


    private RestRequest(Builder builder) {
        long readTimeout = builder.readTimeout;
        long writeTimeout = builder.writeTimeout;
        long connTimeout = builder.connTimeout;

        // build request

        String callUrl = "";
        if (builder.url != null) {
            callUrl = builder.url.toString();
        } else {
            callUrl += RestAndroidHttp.getInstance().endPoint();

            if (builder.urlPath != null && !builder.urlPath.isEmpty()) {
                callUrl += builder.urlPath;
            }
        }

        // build url params
        Uri.Builder uriBuilder = Uri.parse(callUrl).buildUpon();
        Set<String> keySet = builder.urlParams.keySet();
        for (String s : keySet) {
            uriBuilder.appendQueryParameter(s, builder.urlParams.get(s));
        }


        mRequest = new Request.Builder()
                .url(uriBuilder.toString())
                .headers(builder.headers.build())
                .method(builder.method, builder.body)
                .tag(builder.tag)
                .build();

        // build call;
        OkHttpClient.Builder cloneBuilder = RestAndroidHttp.getInstance().okHttpClient().newBuilder();

        if (readTimeout > 0)
            cloneBuilder.readTimeout(readTimeout, TimeUnit.SECONDS).build();

        if (writeTimeout > 0)
            cloneBuilder.writeTimeout(writeTimeout, TimeUnit.SECONDS).build();

        if (connTimeout > 0)
            cloneBuilder.connectTimeout(connTimeout, TimeUnit.SECONDS).build();

        mCall = cloneBuilder.build().newCall(mRequest);
    }


    public HttpUrl url() {
        return mRequest.url();
    }

    public String method() {
        return mRequest.method();
    }

    public Headers headers() {
        return mRequest.headers();
    }

    public String header(String name) {
        return mRequest.headers().get(name);
    }

    public List<String> headers(String name) {
        return mRequest.headers().values(name);
    }

    public RequestBody body() {
        return mRequest.body();
    }

    public Object tag() {
        return mRequest.tag();
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public CacheControl cacheControl() {
        CacheControl result = mRequest.cacheControl();

        if (result != null)
            return result;

        mRequest = mRequest.newBuilder().cacheControl(CacheControl.parse(mRequest.headers())).build();
        return mRequest.cacheControl();
    }

    public boolean isHttps() {
        return mRequest.url().isHttps();
    }

    public String toString() {
        return "Request{method=" + mRequest.method()
                + ", url=" + mRequest.url()
                + ", tag=" + (mRequest.tag() != this ? mRequest.tag() : null) + '}';
    }


    public Response execute() throws IOException {
        return mCall.execute();
    }

    public void enqueue(Callback callback) {
        mCall.enqueue(new CallbackWraper(callback));
    }

    public void cancel() {
        mCall.cancel();
    }

    public boolean isExecuted() {
        return mCall.isExecuted();
    }

    public boolean isCanceled() {
        return mCall.isCanceled();
    }

    public static class Builder {

        private long readTimeout;
        private long writeTimeout;
        private long connTimeout;
        /**
         * request urlParams.
         */
        private final Map<String, String> urlParams = new LinkedHashMap<>();
        private String urlPath;

        // copy from okhttp
        private HttpUrl url;
        private String method;
        private okhttp3.Headers.Builder headers;
        private RequestBody body;
        private Object tag;

        public Builder() {
            this.method = "GET";
            this.headers = new okhttp3.Headers.Builder();
        }


        public Builder(RestRequest restRequest) {
            this.url = restRequest.mRequest.url();
            this.method = restRequest.mRequest.method();
            this.body = restRequest.mRequest.body();
            this.tag = restRequest.mRequest.tag();
            this.headers = restRequest.mRequest.headers().newBuilder();
        }

        public Builder readTimeOut(long readTimeOut) {
            this.readTimeout = readTimeOut;
            return this;
        }

        public Builder writeTimeOut(long writeTimeOut) {
            this.writeTimeout = writeTimeOut;
            return this;
        }

        public Builder connTimeOut(long connTimeOut) {
            this.connTimeout = connTimeOut;

            return this;
        }

        /**
         * add request String url params.
         *
         * @param key   url param's key
         * @param value url param's value
         *
         * @return
         */
        public Builder addUrlParams(String key, String value) {
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
         * @return
         */
        public Builder addUrlParams(String key, Object value) {
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
         * @return
         */
        public Builder addUrlParams(String key, int value) {
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
         * @return
         */
        public Builder addUrlParams(String key, long value) {
            if (key != null) {
                this.urlParams.put(key, String.valueOf(value));
            }

            return this;
        }

        /**
         * set the path that flow the end point
         *
         * @param urlPath
         *
         * @return
         */
        public Builder urlPath(String urlPath) {
            this.urlPath = urlPath;
            return this;
        }


        // copy from okhttp
        public Builder url(HttpUrl url) {
            if (url == null) {
                throw new IllegalArgumentException("url == null");
            } else {
                this.url = url;
                return this;
            }
        }

        public Builder url(String url) {
            if (url == null) {
                throw new IllegalArgumentException("url == null");
            } else {
                if (url.regionMatches(true, 0, "ws:", 0, 3)) {
                    url = "http:" + url.substring(3);
                } else if (url.regionMatches(true, 0, "wss:", 0, 4)) {
                    url = "https:" + url.substring(4);
                }

                HttpUrl parsed = HttpUrl.parse(url);
                if (parsed == null) {
                    throw new IllegalArgumentException("unexpected url: " + url);
                } else {
                    return this.url(parsed);
                }
            }
        }

        public Builder url(URL url) {
            if (url == null) {
                throw new IllegalArgumentException("url == null");
            } else {
                HttpUrl parsed = HttpUrl.get(url);
                if (parsed == null) {
                    throw new IllegalArgumentException("unexpected url: " + url);
                } else {
                    return this.url(parsed);
                }
            }
        }

        public Builder header(String name, String value) {
            this.headers.set(name, value);
            return this;
        }

        public Builder addHeader(String name, String value) {
            this.headers.add(name, value);
            return this;
        }

        public Builder removeHeader(String name) {
            this.headers.removeAll(name);
            return this;
        }

        public Builder headers(Headers headers) {
            this.headers = headers.newBuilder();
            return this;
        }

        public Builder cacheControl(CacheControl cacheControl) {
            String value = cacheControl.toString();
            return value.isEmpty() ? this.removeHeader("Cache-Control") : this.header("Cache-Control", value);
        }

        public Builder get() {
            return this.method("GET", null);
        }

        public Builder head() {
            return this.method("HEAD", null);
        }

        public Builder post(RequestBodyFactory bodyFactory) {
            return this.method("POST", bodyFactory);
        }

        public Builder delete(RequestBodyFactory bodyFactory) {
            return this.method("DELETE", bodyFactory);
        }

        public Builder delete() {
            return this.delete(
                    new RequestBodyFactory() {
                        @Override
                        public RequestBody buildRequestBody() {
                            return RequestBody.create(null, new byte[0]);
                        }
                    }
            );
        }

        public Builder put(RequestBodyFactory bodyFactory) {
            return this.method("PUT", bodyFactory);
        }

        public Builder patch(RequestBodyFactory bodyFactory) {
            return this.method("PATCH", bodyFactory);
        }

        public Builder method(String method, RequestBodyFactory bodyFactory) {
            body = bodyFactory.buildRequestBody();
            if (method != null && method.length() != 0) {
                if (body != null && !HttpMethod.permitsRequestBody(method)) {
                    throw new IllegalArgumentException("method " + method + " must not have a request body.");
                } else if (body == null && HttpMethod.requiresRequestBody(method)) {
                    throw new IllegalArgumentException("method " + method + " must have a request body.");
                } else {
                    this.method = method;
                    return this;
                }
            } else {
                throw new IllegalArgumentException("method == null || method.length() == 0");
            }
        }

        public Builder tag(Object tag) {
            this.tag = tag;
            return this;
        }

        public RestRequest build() {
            return new RestRequest(this);
        }

    }
}
