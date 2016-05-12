package com.github.rain.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-04-05 16:39
 */
public class RestRequest extends Request<RestHttpResponse> {

    protected RequestParams mRequestParams;

    private RestResponseListener mRestResponseListener;

    private IConverter mConverter;

    protected RestRequest(RequestParams params, IConverter converter) {
        super(params.method(), params.url(), null);
        this.mRequestParams = params;
        this.mConverter = converter;
    }


    @Override
    protected Response<RestHttpResponse> parseNetworkResponse(NetworkResponse response) {


        String str = convertNetworkResponseData2String(response);
        RestHttpResponse result = new RestHttpResponse(response.statusCode,
                str,
                response.headers,
                response.notModified, "");


        new RestLog().Line("response success")
                .Line("url:%s", getUrl())
                .Line("result:%s", str)
                .d();

        return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {

        new RestLog().Line("response error")
                .Line("url:%s", getUrl())
                .Line("error:%s", volleyError.networkResponse != null ?
                        "error code:" + volleyError.networkResponse.statusCode :
                        (volleyError.getMessage() == null ? "unknown error" : volleyError.getMessage()))
                .d();

        return super.parseNetworkError(volleyError);
    }

    @Override
    protected void deliverResponse(RestHttpResponse response) {

        if (mRestResponseListener != null)
            mRestResponseListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        if (mRestResponseListener != null)
            mRestResponseListener.onErrorResponse(error);
    }

    @Override
    public void cancel() {
        super.cancel();
        if (mRestResponseListener != null)
            mRestResponseListener.onCancle();
    }

    public final void excute(final RestListener<?> listener) {
        if (listener == null) {
            throw new NullPointerException("listener should not be null");
        }

        mRestResponseListener = new RestResponseListener(listener, mConverter);

        //start callback
        listener.onPreExecute();
        new RestLog().Line("PreExecute")
                .Line("url:%s", mRequestParams.url())
                .Line("headers:%s", mRequestParams.headers().toString())
                .Line("param:%s", mRequestParams.params().toString())
                .d();
        mRequestParams.engine().requestQueue.add(this);
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mRequestParams.headers();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mRequestParams.params();
    }

    private String convertNetworkResponseData2String(NetworkResponse response) {
        if (response == null || response.data == null) {
            return "";
        }
        String str;
        try {
            str = new String(response.data, HttpHeaderParser.parseCharset(response.headers, getParamsEncoding()));
        } catch (Exception e) {
            str = new String(response.data);
        }

        return str;
    }


}
