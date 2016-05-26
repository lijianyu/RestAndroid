package com.github.rain.rest;

import com.github.rain.net.callback.DataCallback;

import org.json.JSONObject;

import okhttp3.Response;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-05-26 11:37
 */
public class ResultCallback<T> extends DataCallback<T> {

    @Override
    public T parseNetworkResponse(Response response) throws Exception {

        JSONObject jsonObject = new JSONObject(response.body().toString());


        return super.parseNetworkResponse(response);
    }
}
