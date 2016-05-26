package com.github.rain.net.requestbody;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-05-23 14:59
 */
public class StringRequestBodyFactory implements RequestBodyFactory {

    private static MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8");

    private String mContent;
    private MediaType mMediaType = MEDIA_TYPE_PLAIN;


    public StringRequestBodyFactory(String content) {
        this(content, MEDIA_TYPE_PLAIN);
    }

    public StringRequestBodyFactory(String content, MediaType mediaType) {
        mContent = content;
        mMediaType = mediaType;
    }

    @Override
    public RequestBody buildRequestBody() {
        return RequestBody.create(mMediaType, mContent);
    }
}
