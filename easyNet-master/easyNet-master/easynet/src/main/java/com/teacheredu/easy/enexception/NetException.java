package com.teacheredu.easy.enexception;

import com.teacheredu.easy.utils.LogUtils;

/**
 * Created by kiera1 on 15/9/18.
 */
public class NetException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public NetException() {
        super("网络异常");
    }
    public NetException(String url) {
        super("网络异常");
        LogUtils.e("出错的URL = " + url);
    }
}
