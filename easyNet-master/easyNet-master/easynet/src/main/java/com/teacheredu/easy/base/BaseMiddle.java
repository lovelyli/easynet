package com.teacheredu.easy.base;

import com.teacheredu.easy.enenum.MethodType;
import com.teacheredu.easy.engine.NetCallback;
import com.teacheredu.easy.net.EasyNetAsyncTask;
import com.teacheredu.easy.param.EasyNetParam;

/**
 * Created by kiera1 on 15/9/22.
 */
public abstract class BaseMiddle implements NetCallback {

    protected BaseActivity activity;
    public BaseMiddle(){
    }
    public BaseMiddle(BaseActivity activity ){
        this.activity = activity;
    }

    public void send(int requestCode, EasyNetParam pm , NetCallback callback){
        new EasyNetAsyncTask(requestCode,callback).execute(pm);
    }

    public void send(int requestCode, EasyNetParam pm){
        new EasyNetAsyncTask(requestCode,this).execute(pm);
    }

    public void send(int requestCode, MethodType type, EasyNetParam pm , NetCallback callback){
        new EasyNetAsyncTask(type, requestCode, callback).execute(pm);
    }

    public void send(int requestCode, MethodType type, EasyNetParam pm ){
        new EasyNetAsyncTask(type, requestCode, this).execute(pm);
    }

    @Override
    public void success(Object result, int requestCode) {
        //子类覆盖实现
    }

    @Override
    public void failed(int requestCode) {
        //子类覆盖实现
    }
}
