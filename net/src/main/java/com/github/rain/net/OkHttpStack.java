/*
 * OkhttpStack      2015-11-18
 * Copyright (c) 2015 hujiang Co.Ltd. All right reserved(http://www.hujiang.com).
 * 
 */

package com.github.rain.net;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HurlStack;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * using the okhttp as the transport layer.
 *
 * @author simon
 * @version 1.0.0
 * @since 2015-11-18
 */
public class OkHttpStack extends HurlStack {

    private final OkHttpClient mOkHttpClient;
    private final static String TAG = "OkHttpStack";

    /**
     * constructor.
     */
    public OkHttpStack() {
        this(new OkHttpClient());
    }

    /**
     * constructor.
     *
     * @param okHttpClient {@link OkHttpClient}
     */
    public OkHttpStack(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            throw new NullPointerException("Client must not be null.");
        }

        mOkHttpClient = okHttpClient;
    }

    /**
     * {@link OkHttpClient}.
     *
     * @return {@link OkHttpClient}
     */
    public OkHttpClient getClient() {
        return mOkHttpClient;
    }

    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders)
            throws IOException, AuthFailureError {

        int timeoutMs = request.getTimeoutMs();
        OkHttpClient client = mOkHttpClient.newBuilder()
                .connectTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .readTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .writeTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .build();

        okhttp3.Request.Builder okHttpRequestBuilder =
                new okhttp3.Request.Builder();
        okHttpRequestBuilder.url(request.getUrl());

        Map<String, String> headers = request.getHeaders();

        for (final String name : headers.keySet()) {
            okHttpRequestBuilder.addHeader(name, headers.get(name));
        }

        for (final String name : additionalHeaders.keySet()) {
            okHttpRequestBuilder.addHeader(name, additionalHeaders.get(name));
        }


        setConnectionParametersForRequest(okHttpRequestBuilder, request);

        okhttp3.Request okHttpRequest = okHttpRequestBuilder.build();

        new RestLog().Line("performRequest")
                .Line("method:%s", okHttpRequest.method() )
                .Line("url:%s", okHttpRequest.url().toString())
                .Line("headers:%s", okHttpRequest.headers().toString())
                .d();

        Call okHttpCall = client.newCall(okHttpRequest);
        Response okHttpResponse = okHttpCall.execute();

        StatusLine responseStatus = new BasicStatusLine
                (
                        parseProtocol(okHttpResponse.protocol()),
                        okHttpResponse.code(),
                        okHttpResponse.message()
                );

        BasicHttpResponse response = new BasicHttpResponse(responseStatus);
        response.setEntity(entityFromOkHttpResponse(okHttpResponse));

        Headers responseHeaders = okHttpResponse.headers();

        for (int i = 0, len = responseHeaders.size(); i < len; i++) {
            final String name = responseHeaders.name(i), value = responseHeaders.value(i);

            if (name != null) {
                response.addHeader(new BasicHeader(name, value));
            }
        }

        return response;
    }

    private static HttpEntity entityFromOkHttpResponse(Response r) throws IOException {
        BasicHttpEntity entity = new BasicHttpEntity();
        ResponseBody body = r.body();

        entity.setContent(body.byteStream());
        entity.setContentLength(body.contentLength());
        entity.setContentEncoding(r.header("Content-Encoding"));

        if (body.contentType() != null) {
            entity.setContentType(body.contentType().type());
        }
        return entity;
    }

    @SuppressWarnings("deprecation")
    private static void setConnectionParametersForRequest
            (okhttp3.Request.Builder builder, Request<?> request)
            throws IOException, AuthFailureError {
        switch (request.getMethod()) {
            case Request.Method.DEPRECATED_GET_OR_POST:
                // Ensure backwards compatibility.
                // Volley assumes a request with a null body is a GET.
                byte[] postBody = request.getPostBody();

                if (postBody != null) {
                    builder.post(RequestBody.create
                            (MediaType.parse(request.getPostBodyContentType()), postBody));
                }
                break;

            case Request.Method.GET:
                builder.get();
                break;

            case Request.Method.DELETE:
                builder.delete();
                break;

            case Request.Method.POST:
                builder.post(createRequestBody(request));
                break;

            //            case Request.Method.PUT:
            //                builder.put(createRequestBody(request));
            //                break;
            //
            //            case Request.Method.HEAD:
            //                builder.head();
            //                break;
            //
            //            case Request.Method.OPTIONS:
            //                builder.method("OPTIONS", null);
            //                break;
            //
            //            case Request.Method.TRACE:
            //                builder.method("TRACE", null);
            //                break;
            //
            //            case Request.Method.PATCH:
            //                builder.patch(createRequestBody(request));
            //                break;

            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }

    private static ProtocolVersion parseProtocol(final Protocol p) {
        switch (p) {
            case HTTP_1_0:
                return new ProtocolVersion("HTTP", 1, 0);
            case HTTP_1_1:
                return new ProtocolVersion("HTTP", 1, 1);
            case SPDY_3:
                return new ProtocolVersion("SPDY", 3, 1);
            case HTTP_2:
                return new ProtocolVersion("HTTP", 2, 0);
        }

        throw new IllegalAccessError("Unkwown protocol");
    }

    private static RequestBody createRequestBody(Request r) throws AuthFailureError {
        final byte[] body = r.getBody();
        if (body == null) return null;

        return RequestBody.create(MediaType.parse(r.getBodyContentType()), body);
    }
}