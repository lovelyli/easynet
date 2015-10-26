package com.teacheredu.easy.engine;

/**
 * Created by kiera1 on 15/9/18.
 */
public interface NetCallback{

    //成功回调
    void success(Object result, int requestCode);
    //失败回调
    void failed(int requestCode);
}
