package com.teacheredu.easy.net;

import android.os.AsyncTask;

import com.teacheredu.easy.ENGlobalParams;
import com.teacheredu.easy.enenum.MethodType;
import com.teacheredu.easy.enexception.NetException;
import com.teacheredu.easy.enexception.error.ErrorHandler;
import com.teacheredu.easy.engine.Ihandler;
import com.teacheredu.easy.engine.NetCallback;
import com.teacheredu.easy.engine.impl.DFIhandler;
import com.teacheredu.easy.param.EasyNetParam;

import java.util.Map;

/**
 * Created by kiera1 on 15/9/18.
 */
public class EasyNetAsyncTask extends AsyncTask<EasyNetParam, Integer, Object> {

    private MethodType type;
    private NetCallback callback;
    private int connTimeout = 15 * 1000;
    private int requestCode;

    /**
     * 默认post请求
     * @param requestCode  请求码
     * @param callback     执行结束后的回调
     */
    public EasyNetAsyncTask(int requestCode, NetCallback callback) {
        this(MethodType.POST, requestCode, callback);
    }

    public EasyNetAsyncTask(MethodType type, int requestCode, NetCallback callback) {
        this.callback = callback;
        this.type = type;
        this.requestCode = requestCode;
    }

    /**
     * @param params 参数包装类
     * @return
     */
    @Override
    protected Object doInBackground(EasyNetParam... params) {
        try {
            if (ENGlobalParams.applicationContext == null) {
                throw new IllegalArgumentException("参数异常 请在Application中初始化ENGlobalParams.applicationContext");
            }
            if (!NetworkUtil.checkNetwork(ENGlobalParams.applicationContext)) {
                throw new NetException();
            }
            Object result = null;
            Map<String, String> data = params[0].getData();
            String url = params[0].getUrl();
            Ihandler ihandler = params[0].getIhandler();
            Object obj = params[0].getObject();
            if (MethodType.GET.equals(type)) {
                if(obj != null){
                    result = NetworkUtil.getAndParse(url, data, obj.getClass(), connTimeout);
                }else if(ihandler != null){
                    result = NetworkUtil.getAndParse(url, data, ihandler, connTimeout);
                }else {
                    result = NetworkUtil.getAndParse(url, data, new DFIhandler(), connTimeout);
                }
            } else if (MethodType.POST.equals(type)) {
                if(obj != null){
                    result = NetworkUtil.postAndParse(url, data, obj.getClass(), connTimeout);
                }else if(ihandler != null){
                    result = NetworkUtil.postAndParse(url, data, ihandler, connTimeout);
                }else {
                    result = NetworkUtil.postAndParse(url, data, new DFIhandler(), connTimeout);
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result == null || result instanceof Exception) {
            ErrorHandler.handle(ENGlobalParams.applicationContext, result);
            callback.failed(requestCode);
        } else {
            callback.success(result, requestCode);
        }
    }

}
