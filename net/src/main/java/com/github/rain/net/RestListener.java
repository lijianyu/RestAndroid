package com.github.rain.net;

import com.android.volley.VolleyError;
import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * the request callback
 *
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-04-06 13:26
 */
public abstract class RestListener<T> {

    public Type type;

    public RestListener() {
        type = getSuperclassTypeParameter(getClass());

    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }


    /**
     * before request execute
     */
    public void onPreExecute() {
    }

    /**
     * when request finish
     */
    public void onFinish() {
    }

    /**
     * when request success
     */
    public abstract void onSuccess(Result<T> result);

    /**
     * when request error
     */
    public void onError(VolleyError error) {
    }

    /**
     * when request cancle
     */
    public void onCancle() {

    }

}
