package com.github.rain.net.requestbody;

import com.github.rain.net.GsonUtils;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-05-23 14:26
 */
public class JsonRequestBodyFactory implements RequestBodyFactory {


    private static final MediaType MEDIA_TYPE_PLAIN = MediaType.parse("application/json; charset=utf-8");


    private JsonObject mJsonObject = new JsonObject();
    private Object mObject;

    public JsonRequestBodyFactory() {
    }

    public JsonRequestBodyFactory(Object object) {
        mObject = object;
    }

    /**
     * add request String  params.
     *
     * @param key   param's key
     * @param value param's value
     *
     * @return
     */
    public JsonRequestBodyFactory addParam(String key, String value) {
        if (key != null && value != null) {
            mJsonObject.addProperty(key, value);
        }

        return this;
    }

    /**
     * add request Object  params.
     *
     * @param key   param's key
     * @param value param's value
     *
     * @return
     */
    public JsonRequestBodyFactory addParam(String key, Object value) {
        addParam(key, value == null ? "" : value.toString());

        return this;
    }

    /**
     * add request int  params.
     *
     * @param key   param's key
     * @param value param's value
     *
     * @return
     */
    public JsonRequestBodyFactory addParam(String key, int value) {
        addParam(key, String.valueOf(value));
        return this;
    }

    /**
     * add request long  params.
     *
     * @param key   param's key
     * @param value param's value
     *
     * @return
     */
    public JsonRequestBodyFactory addParam(String key, long value) {
        addParam(key, String.valueOf(value));

        return this;
    }

    @Override
    public RequestBody buildRequestBody() {
        return RequestBody.create(MEDIA_TYPE_PLAIN,
                mObject == null ?
                        mJsonObject.toString() : GsonUtils.toJsonString(mObject));
    }
}
