package com.github.rain.net.converters;

import android.text.TextUtils;

import com.github.rain.net.GsonUtils;
import com.github.rain.net.IConverter;

import java.lang.reflect.Type;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-04-27 10:44
 */
public abstract class BaseResponseConverter implements IConverter {

    protected Object parseString(Type clazz, String result) {
        if (TextUtils.isEmpty(result) || "null".equalsIgnoreCase(result)
                || "[]".equals(result) || "{}".equals(result))
            return null;

        if (clazz == String.class) {
            return result;
        } else if (clazz == byte[].class && !TextUtils.isEmpty(result)) {
            return result.getBytes();
        } else {
            return GsonUtils.fromJsonString(result, clazz);
        }
    }
}
