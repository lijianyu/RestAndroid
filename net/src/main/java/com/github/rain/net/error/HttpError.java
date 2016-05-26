package com.github.rain.net.error;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-05-23 17:21
 */
public class HttpError extends CallError {

    private String message;

    public HttpError(int statusCode) {
        message = String.format("http error:%s", statusCode);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
