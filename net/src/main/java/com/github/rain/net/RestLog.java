package com.github.rain.net;

import android.util.Log;

import com.mdsd.sdk.net.BuildConfig;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-04-12 15:18
 */
public class RestLog {
    private StringBuilder stringBuilder = new StringBuilder();
    private static final String MARK = ">>>>>>>> ";

    public RestLog Line(String stringformat, Object... args) {
        stringBuilder.append("\n");
        stringBuilder.append(MARK);
        stringBuilder.append(String.format(stringformat, args));

        return this;
    }


    public void d() {

        if (BuildConfig.DEBUG) {
            Log.d("RestLog", stringBuilder.toString());
        }
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}
