package com.github.rain.net.error;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-05-20 16:02
 */
public class CallError extends Exception {

    public CallError() {
    }


    public CallError(String exceptionMessage) {
        super(exceptionMessage);
    }

    public CallError(String exceptionMessage, Throwable reason) {
        super(exceptionMessage, reason);
    }

    public CallError(Throwable cause) {
        super(cause);
    }


}
