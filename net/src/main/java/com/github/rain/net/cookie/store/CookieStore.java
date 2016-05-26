package com.github.rain.net.cookie.store;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-05-23 11:19
 */
public interface CookieStore {
    public void add(HttpUrl uri, List<Cookie> cookie);

    public List<Cookie> get(HttpUrl uri);

    public List<Cookie> getCookies();

    public boolean remove(HttpUrl uri, Cookie cookie);

    public boolean removeAll();
}
