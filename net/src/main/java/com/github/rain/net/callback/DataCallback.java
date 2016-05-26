package com.github.rain.net.callback;

import android.text.TextUtils;

import com.github.rain.net.GsonUtils;
import com.github.rain.net.error.CallError;
import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-05-20 16:33
 */
public abstract class DataCallback<T> implements Callback<T> {

    private Type type;

    public DataCallback() {
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

    public Type getType() {
        return type;
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onProgress(float progress, float length) {

    }

    @Override
    public void onResponse(T response) {

    }

    @Override
    public T parseNetworkResponse(Response response) throws Exception {
        return (T) parseResponse(type, response.body().string());
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(CallError error) {

    }


    @Override
    public void onCancel() {

    }


    protected Object parseResponse(Type clazz, String result) {
        if (TextUtils.isEmpty(result) || "null".equalsIgnoreCase(result)
                || "[]".equals(result) || "{}".equals(result))
            return null;

        if (clazz.equals(String.class)
                || clazz.equals(Number.class)
                || clazz.equals(Integer.class)
                || clazz.equals(Double.class)
                || clazz.equals(Long.class)
                || clazz.equals(Float.class)
                || clazz.equals(Short.class)) {
            return result;
        } else if (clazz == byte[].class && !TextUtils.isEmpty(result)) {
            return result.getBytes();
        } else {
            return GsonUtils.fromJsonString(result, clazz);
        }
    }
}
