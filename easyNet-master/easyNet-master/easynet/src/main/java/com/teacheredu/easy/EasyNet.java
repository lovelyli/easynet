package com.teacheredu.easy;

import android.content.Context;

import com.teacheredu.easy.enenum.MethodType;
import com.teacheredu.easy.engine.NetCallback;
import com.teacheredu.easy.engine.impl.DFIhandler;
import com.teacheredu.easy.net.EasyNetAsyncTask;
import com.teacheredu.easy.param.EasyNetParam;

import java.util.Map;

/**
 * Created by kiera1 on 15/9/18.
 */
public class EasyNet {

    private NetCallback callback;
    private EasyNetParam pm;
    private int requestCode;
    private MethodType type;

    public EasyNet() {
        super();
    }

    public EasyNet(String url,int requestCode, Map<String, String> data, NetCallback callback){
        this(MethodType.POST, url , requestCode, data, callback);
    }
    public EasyNet(MethodType type, String url,int requestCode, Map<String, String> data, NetCallback callback){
        this(type, requestCode, new EasyNetParam(url, data, new DFIhandler()), callback);
    }

    public EasyNet(int requestCode, EasyNetParam pm,  NetCallback callback) {
        this(MethodType.POST, requestCode, pm, callback);
    }
    public EasyNet(MethodType type, int requestCode, EasyNetParam pm,  NetCallback callback) {
        this.requestCode = requestCode;
        this.callback = callback;
        this.pm = pm;
        this.type = type;
    }


    public EasyNet setCallback(NetCallback callback) {
        this.callback = callback;
        return this;
    }

    public EasyNet setPm(EasyNetParam pm) {
        this.pm = pm;
        return this;
    }

    public EasyNet setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public EasyNet setType(MethodType type) {
        this.type = type;
        return this;
    }

    public void execute() {
        if (callback == null || pm == null )
            throw new IllegalArgumentException("参数异常:尚未初始化 NetCallback 或者 EasyNetParam");
        if(type == null)
            type = MethodType.POST;
        new EasyNetAsyncTask(type, requestCode, callback).execute(pm);
    }
}
