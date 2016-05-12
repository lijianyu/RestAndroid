package com.github.rain.net;

import java.util.Map;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-04-06 15:32
 */
public interface RequestParams {


    /**
     * @return {@link com.android.volley.Request.Method}
     */
    int method();

    String url();

    Map<String, String> headers();

    Map<String,String> params();

    RequestEngine engine();


}
