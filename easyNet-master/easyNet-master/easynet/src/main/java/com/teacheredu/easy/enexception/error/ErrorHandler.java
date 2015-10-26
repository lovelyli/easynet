package com.teacheredu.easy.enexception.error;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by kiera1 on 15/9/18.
 */
public class ErrorHandler {
    public static void handle(Context mContext,Object result){
        if(result==null){
            Toast.makeText(mContext,"数据异常",Toast.LENGTH_LONG).show();
        }else if(result instanceof Exception){
            Toast.makeText(mContext,((Exception) result).getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
