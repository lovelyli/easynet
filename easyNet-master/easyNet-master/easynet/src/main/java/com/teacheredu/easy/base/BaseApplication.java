package com.teacheredu.easy.base;

import android.app.Application;

import com.teacheredu.easy.ENGlobalParams;
import com.teacheredu.easy.utils.LogUtils;

/**
 * Created by kiera1 on 15/9/22.
 */
public abstract class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ENGlobalParams.applicationContext = this;
        //TODO Log输出级别 上线需要修改
        ENGlobalParams.Debuggable = LogUtils.LEVEL_ERROR;
    }
}
