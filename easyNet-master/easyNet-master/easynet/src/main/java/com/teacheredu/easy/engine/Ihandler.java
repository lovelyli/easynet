package com.teacheredu.easy.engine;


import com.teacheredu.easy.enexception.ParseException;

import java.io.InputStream;

/**
 * Created by kiera1 on 15/9/18.
 * 数据处理
 */
public interface Ihandler {
    Object parseResponse(InputStream inputStream) throws ParseException;
}
