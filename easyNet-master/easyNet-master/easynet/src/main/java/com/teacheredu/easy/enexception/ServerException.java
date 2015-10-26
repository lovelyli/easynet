package com.teacheredu.easy.enexception;


import com.teacheredu.easy.utils.LogUtils;

public class ServerException extends Exception{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

    public ServerException() {
        super("服务异常");
    }
    public ServerException(String url) {
        super("服务异常");
        LogUtils.e("服务器异常URL = " + url);
    }
}
