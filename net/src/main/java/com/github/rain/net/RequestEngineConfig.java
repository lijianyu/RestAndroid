package com.github.rain.net;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.toolbox.DiskBasedCache;
import com.github.rain.net.converters.DefaultResponseConverter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.CookieJar;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-04-06 17:11
 */
public class RequestEngineConfig {
    /**
     * http header Content-Type.
     */
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    /**
     * http header Content-Range.
     */
    public static final String HEADER_CONTENT_RANGE = "Content-Range";
    /**
     * http header Content-Encoding.
     */
    public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
    /**
     * http header Content-Length.
     */
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";
    /**
     * http header Range.
     */
    public static final String HEADER_RANGE = "Range";
    /**
     * http header Content-Disposition.
     */
    public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
    /**
     * http header Accept-Encoding.
     */
    public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    /**
     * http header User-Agent.
     */
    public static final String HEADER_USER_AGENT = "User-Agent";
    /**
     * encoding gzip.
     */
    public static final String ENCODING_GZIP = "gzip";
    /**
     * mimetype application/octet-stream.
     */
    public final static String APPLICATION_OCTET_STREAM = "application/octet-stream";
    /**
     * mimetype application/json.
     */
    public final static String APPLICATION_JSON = "application/json";
    /**
     * mimetype application/x-www-form-urlencoded.
     */
    public final static String APPLICATION_X_WWW_FORM_URL_ENCODED = "application/x-www-form-urlencoded";

    /**
     * Default on-disk cache directory.
     */
    private static final String DEFAULT_CACHE_DIR = "volley";

    /**
     * default http timeout 10000.
     */
    public static final int DEFAULT_HTTP_TIMEOUT = 10 * 1000;
    public static final int DEFAULT_HTTP_RETRIES = 1;

    public static final String DEFAULT_PARAMS_ENCODING = "UTF-8";


    private final int timeOut;
    private final int retries;
    private final CookieJar cookieJar;
    private final Map<String, String> headers;
    private final String endPoint;
    private final Cache cache;
    private final IConverter converter;

    public RequestEngineConfig(Context context) {
        this(new Builder(context));
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    private RequestEngineConfig(RequestEngineConfig.Builder builder) {

        this.timeOut = builder.timeOut;
        this.retries = builder.retries;
        this.cookieJar = builder.cookieJar;
        this.headers = builder.headers;
        this.endPoint = builder.endPoint;
        this.cache = builder.cache;
        this.converter = builder.converter;

    }

    public int getTimeOut() {
        return timeOut;
    }

    public int getRetries() {
        return retries;
    }

    public CookieJar getCookieJar() {
        return cookieJar;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public Cache getCache() {
        return cache;
    }

    public IConverter getConverter() {
        return converter;
    }

    public static class Builder {
        int timeOut = DEFAULT_HTTP_TIMEOUT;
        int retries = DEFAULT_HTTP_RETRIES;
        CookieJar cookieJar;
        Map<String, String> headers = new HashMap<>();
        private String endPoint;
        private Cache cache;
        private IConverter converter = new DefaultResponseConverter();

        public Builder(Context context) {
            cache = new DiskBasedCache(new File(context.getCacheDir(), DEFAULT_CACHE_DIR));
        }

        Builder(RequestEngineConfig config) {
            this.timeOut = config.timeOut;
            this.retries = config.retries;
            this.cookieJar = config.cookieJar;
            this.headers = config.headers;
            this.endPoint = config.endPoint;
            this.cache = config.cache;
            this.converter = config.converter;
        }

        public Builder setTimeOut(int timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        public Builder setRetries(int retries) {
            this.retries = retries;
            return this;
        }

        public Builder addHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder addHeaders(Map<String, String> headers) {
            if (headers != null) {
                Set<String> keySet = headers.keySet();
                for (String key : keySet) {
                    this.headers.put(key, headers.get(key));
                }
            }
            return this;
        }

        public Builder setCookieStore(CookieJar cookieJar) {
            this.cookieJar = cookieJar;
            return this;
        }

        public Builder setEndPoint(String entPoint) {
            this.endPoint = entPoint;
            return this;
        }

        public Builder setCache(Cache cache) {
            this.cache = cache;
            return this;
        }

        public Builder setConvert(IConverter converter) {
            this.converter = converter;
            return this;
        }

        public RequestEngineConfig build() {
            return new RequestEngineConfig(this);
        }

    }
}
