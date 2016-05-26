package com.github.rain.net.requestbody;

import okhttp3.RequestBody;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-05-20 15:02
 */
public interface RequestBodyFactory {
    RequestBody buildRequestBody();
}
