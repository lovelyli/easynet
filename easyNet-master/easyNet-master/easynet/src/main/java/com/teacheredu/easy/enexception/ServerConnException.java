package com.teacheredu.easy.enexception;

import com.teacheredu.easy.utils.LogUtils;

/**
 * Created by kiera1 on 15/9/18.
 */
public class ServerConnException extends Exception{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ServerConnException() {
        super("服务器错误");
    }

    public ServerConnException(String url) {
        super("服务器错误");
        LogUtils.e( "服务器错误URL = " + url);
    }
}
