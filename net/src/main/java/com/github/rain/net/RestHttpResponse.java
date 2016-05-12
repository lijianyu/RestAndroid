/*
 * ResponseResult      2015-12-10
 * Copyright (c) 2015 hujiang Co.Ltd. All right reserved(http://www.hujiang.com).
 * 
 */

package com.github.rain.net;

import java.util.Map;

/**
 *
 */
public class RestHttpResponse {
    /**
     * http status code.
     */
    public int statusCode;
    /**
     * response data.
     */
    public String data;
    /**
     * response headers.
     */
    public Map<String, String> headers;
    /**
     * response not modified.
     */
    public boolean notModified;
    /**
     * response message.
     */
    public String message;

    /**
     * constructor.
     *
     * @param statusCode    http status code.
     * @param data          response data.
     * @param headers       response headers.
     * @param notModified   response not modified.
     * @param message       response message.
     */
    public RestHttpResponse(int statusCode, String data, Map<String, String> headers, boolean notModified, String message) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
        this.notModified = notModified;
        this.message = message;
    }
}