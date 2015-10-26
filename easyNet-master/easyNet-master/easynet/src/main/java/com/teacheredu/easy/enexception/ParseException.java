package com.teacheredu.easy.enexception;

import com.teacheredu.easy.utils.LogUtils;

/**
 * Created by kiera1 on 15/9/18.
 * 解析异常
 */
public class ParseException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = -299713641613973101L;

    public ParseException(){
        super("抱歉，没有相应的数据!");
    }

    public ParseException(String url){
        super("抱歉，没有相应的数据!");
        LogUtils.e( "没有数据URL = " + url);
    }
}
