package com.github.rain.net;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * trans RestHttpResponse and call HttpListener
 *
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-04-06 13:52
 */
public class RestResponseListener implements Response.Listener<RestHttpResponse>, Response.ErrorListener {

    private final RestListener mRestListener;
    private final HttpListenerCallBack mHttpListenerCallBack;
    private final IConverter mIConverter;

    public RestResponseListener(RestListener restListener, IConverter iConverter) {
        this.mRestListener = restListener;
        this.mIConverter = iConverter;
        this.mHttpListenerCallBack = new HttpListenerCallBack(mRestListener);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        mHttpListenerCallBack.error(mIConverter.convertError(error));
    }

    @Override
    public void onResponse(RestHttpResponse response) {

        Result result = null;
        try {
            result = mIConverter.convertToResult(mRestListener.type, response.data);
        } catch (VolleyError error) {
            mHttpListenerCallBack.error(error);
            return;
        }


        if (result != null)
            result.setHeaders(response.headers);
        else
            result = new Result();

        result.setOriginal(response.data);
        mHttpListenerCallBack.success(result);
    }

    protected void onCancle() {

    }


    private static class HttpListenerCallBack {
        private final RestListener mRestListener;

        private HttpListenerCallBack(RestListener restListener) {
            this.mRestListener = restListener;
        }

        private final void success(Result result) {
            mRestListener.onSuccess(result);
            mRestListener.onFinish();
        }

        private final void error(VolleyError error) {
            mRestListener.onError(error);
            mRestListener.onFinish();
        }

        private final void cancle() {
            mRestListener.onCancle();
            mRestListener.onFinish();
        }

    }

}
