package com.github.rain.net;

import com.android.volley.VolleyError;

import java.lang.reflect.Type;

/**
 * implement to achieve own transform
 *
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-04-12 16:21
 */
public interface IConverter {

    /**
     * convert return string to result
     *
     * @param type
     * @param data
     *
     * @return
     *
     * @throws VolleyError
     */
    Result convertToResult(Type type, String data) throws VolleyError;

    /**
     * @param error
     *
     * @return
     */
    VolleyError convertError(VolleyError error);
}
