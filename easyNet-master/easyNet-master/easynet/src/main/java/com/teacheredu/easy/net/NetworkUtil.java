package com.teacheredu.easy.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.teacheredu.easy.ENConstanValue;
import com.teacheredu.easy.enexception.NetException;
import com.teacheredu.easy.enexception.ParseException;
import com.teacheredu.easy.enexception.ServerConnException;
import com.teacheredu.easy.enexception.ServerException;
import com.teacheredu.easy.engine.Ihandler;
import com.teacheredu.easy.utils.JSONUtils;
import com.teacheredu.easy.utils.LogUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by kiera1 on 15/9/18.
 */
public class NetworkUtil {
    // 相应状态  // 1代表正常，0代表其它错误 ，2代表服务端相应错误，3代表解析错误
    public static int responseStatus;
    public static HttpClient httpClient;

    static {
        if (httpClient == null)
            httpClient = createHttpClient();
    }

    private static HttpClient createHttpClient() {
        HttpParams params = new BasicHttpParams();
        params.setParameter("Charset", ENConstanValue.UTF_8);
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
        HttpProtocolParams.setUseExpectContinue(params, true);
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
        return new DefaultHttpClient(conMgr, params);
    }

    /**
     * 处理网络连接超时
     *
     * @param cutTime 超时时间
     * @return
     */
    public static HttpParams getConnectionTimeOutParams(int cutTime) {
        HttpParams httpParams = new BasicHttpParams();
        httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, cutTime);
        HttpConnectionParams.setSoTimeout(httpParams, cutTime);
        return httpParams;
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * GET请求
     *
     * @param context     上下文
     * @param url         地址
     * @param data        参数
     * @param ihandler    处理器
     * @param connTimeout 超时时长
     * @return
     * @throws ServerConnException
     * @throws NetException
     * @throws ParseException
     */
    public static Object getAndParse( String url, Map<String, String> data, Ihandler ihandler, int connTimeout) throws ServerConnException, NetException, ParseException {
        responseStatus = 0;
        if (data != null) {
            url = buildGetMethod(url, data).build().toString();
        }
        HttpPost httpPost = null;
        try {
            httpPost = new HttpPost(url);
            httpPost.setParams(getConnectionTimeOutParams(connTimeout));
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new ServerConnException(url);
        }
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                responseStatus = 1;
            } else {
                responseStatus = 2;
                httpPost.abort();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage() != null)
                responseStatus = 2;
            throw new NetException(url);
        }
        // 响应正常
        if (responseStatus != 2) {
            try {
                HttpEntity httpEntity = response.getEntity();
                InputStream ins = httpEntity.getContent();
                Object resultMessage = ihandler.parseResponse(ins);
                return resultMessage;
            } catch (Exception e) {
                e.printStackTrace();
                responseStatus = 3;
                throw new ParseException(url);
            }
        }
        return null;
    }

    public static Object getAndParse( String url, Map<String, String> data, Class clazz, int connTimeout) throws ServerConnException, NetException, ParseException {
        responseStatus = 0;
        if (data != null) {
            url = buildGetMethod(url, data).build().toString();
        }
        HttpPost httpPost = null;
        try {
            httpPost = new HttpPost(url);
            httpPost.setParams(getConnectionTimeOutParams(connTimeout));
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new ServerConnException(url);
        }
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                responseStatus = 1;
            } else {
                responseStatus = 2;
                httpPost.abort();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage() != null)
                responseStatus = 2;
            throw new NetException(url);
        }
        // 响应正常
        if (responseStatus != 2) {
            try {
                HttpEntity httpEntity = response.getEntity();
                InputStream ins = httpEntity.getContent();
                String responseStr = streamToString(ins);
                LogUtils.v("返回的结果:" + responseStr);
                return JSONUtils.readValue(responseStr, clazz);
            } catch (Exception e) {
                e.printStackTrace();
                responseStatus = 3;
                throw new ParseException(url);
            }
        }
        return null;
    }

    /**
     * POST请求
     *
     * @param context     上下文
     * @param url         请求地址
     * @param data        参数
     * @param ihandler    处理器
     * @param connTimeout 链接超时时长
     * @return
     * @throws ServerConnException
     * @throws ParseException
     * @throws NetException
     */
    public static Object postAndParse( String url, Map<String, String> data, Ihandler ihandler, int connTimeout) throws ServerConnException, ParseException, NetException {
        ArrayList<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
        if (data != null) {
            for (Map.Entry<String, String> m : data.entrySet()) {
                postData.add(new BasicNameValuePair(m.getKey(), m.getValue()));
                LogUtils.v("参数:" + m.getKey() + " : " + m.getValue());
            }
        }
        HttpPost httpPost;
        try {
            httpPost = new HttpPost(url);
            httpPost.setParams(getConnectionTimeOutParams(connTimeout));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerConnException(url);
        }
        HttpResponse response = null;
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postData, "GBK");
            httpPost.setEntity(entity);

            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 在这儿获取响应的实体数据进行解析
                responseStatus = 1;
            } else {
                //
                responseStatus = 2;
                LogUtils.e("您的请求地址" + url + "返回码不是200，而是" + response.getStatusLine().getStatusCode());
                httpPost.abort();
                throw new ServerException(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseStatus = 2;
            LogUtils.e("报错的");
            throw new NetException(url);
        }

        // 响应正常
        if (responseStatus != 2) {
            InputStream ins = null;
            try {
                // 把这块自定义个方法
                HttpEntity httpEntity = response.getEntity();
                ins = httpEntity.getContent();
                Object resultMessage = ihandler.parseResponse(ins);
                return resultMessage;
            } catch (Exception e) {
                responseStatus = 3;
                throw new ParseException(url);
            } finally {
                try {
                    ins.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }

    /**
     * POST请求
     * @param context     上下文
     * @param url         请求地址
     * @param data        参数
     * @param connTimeout 链接超时时长
     * @return
     * @throws ServerConnException
     * @throws ParseException
     * @throws NetException
     */
    public static Object postAndParse(String url, Map<String, String> data, Class clazz, int connTimeout) throws ServerConnException, ParseException, NetException {
        ArrayList<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
        if (data != null) {
            for (Map.Entry<String, String> m : data.entrySet()) {
                postData.add(new BasicNameValuePair(m.getKey(), m.getValue()));
                LogUtils.v("参数:" + m.getKey() + " : " + m.getValue());
            }
        }
        HttpPost httpPost;
        try {
            httpPost = new HttpPost(url);
            httpPost.setParams(getConnectionTimeOutParams(connTimeout));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerConnException(url);
        }
        HttpResponse response = null;
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postData, "GBK");
            httpPost.setEntity(entity);

            response = httpClient.execute(httpPost);
            System.out.println(response);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 在这儿获取响应的实体数据进行解析
                responseStatus = 1;
            } else {
                //
                responseStatus = 2;
                LogUtils.i("您的请求地址" + url + "返回码不是200，而是" + response.getStatusLine().getStatusCode());
                httpPost.abort();
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseStatus = 2;
            throw new NetException(url);
        }

        // 响应正常
        if (responseStatus != 2) {
            InputStream ins = null;
            try {
                // 把这块自定义个方法
                HttpEntity httpEntity = response.getEntity();
                ins = httpEntity.getContent();
                //Object resultMessage = ihandler.parseResponse(ins);
                String responseStr = streamToString(ins);
                LogUtils.i("返回的结果:" + responseStr);
                return JSONUtils.readValue(responseStr, clazz);
            } catch (Exception e) {
                responseStatus = 3;
                throw new ParseException(url);
            } finally {
                try {
                    ins.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }

    private static Uri.Builder buildGetMethod(String url, Map<String, String> data) {
        final Uri.Builder builder = new Uri.Builder();
        builder.encodedPath(url);
        if (data != null) {
            for (Map.Entry<String, String> m : data.entrySet()) {
                builder.appendQueryParameter(m.getKey(), m.getValue());
                LogUtils.v("参数:" + m.getKey() + " : " + m.getValue());
            }
        }
        return builder;
    }

    private static String streamToString(InputStream inputStream) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer sb = new StringBuffer();
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            sb.append(str);
        }
        return sb.toString();
    }
}
