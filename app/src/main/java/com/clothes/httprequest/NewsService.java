package com.clothes.httprequest;

/**
 * Created by Yuki on 2019/6/29.
 */


import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewsService {
    /**

     * 保存数据，传递参数给web服务器端

     * @param

     * @return
     */
    private final int Running = 1;
    private final int Ending = 2;

    public static boolean save(String b,String s2,String s3) throws Exception {
        //存储照片
        String path = "http://172.24.227.25:8080//yiju/andriod/store";
        Map<String,String> params = new HashMap<String,String>();
        params.put("title",b);
        params.put("tel",s2);
        params.put("order",s3);
        return sendHttpClientPOSTRequest(path,params,"UTF-8");//httpclient已经集成在android中
    }
    public static boolean save2(String tel,String password,String height,String weight,String burndate,String gender) throws Exception {
        //注册账号
        String path = "http://172.24.227.25:8080//yiju/andriod/register";
        Map<String,String> params = new HashMap<String,String>();
        params.put("tel",tel);
        params.put("password",password);
        params.put("height",height);
        params.put("weight",weight);
        params.put("burndate",burndate);
        params.put("gender",gender);
        Log.i("+++------------++++",tel+"00"+password+"00"+height+"00"+weight+"00"+burndate+"00"+gender);
        return sendHttpClientPOSTRequest(path,params,"UTF-8");//httpclient已经集成在android中
    }
    public static String verify(String tel,String password) throws Exception {
        //验证登陆
        String path = "http://172.24.227.25:8080/yiju/andriod/verify";
        Map<String,String> params = new HashMap<String,String>();
        params.put("tel",tel);
        params.put("password",password);
        //httpClient请求方式，如果单纯传递参数的话建议使用GET或者POST请求方式

        return sendHttpClientPOSTRequest2(path,params,"UTF-8");//httpclient已经集成在android中

    }
    public static boolean changesize(String t,String b,String s2) throws Exception {

        //改变尺寸

        String path = "http://172.24.227.25:8080/yiju/andriod/changesize";
        Map<String,String> params = new HashMap<String,String>();
        params.put("tel",t);
        params.put("bust",b);
        params.put("waistline",s2);
        return sendHttpClientPOSTRequest(path,params,"UTF-8");//httpclient已经集成在android中

    }
    public static String display(String t,String alt) throws Exception {
        //查询信息
        String path = "http://172.24.227.25:8080/yiju/andriod/display";
        Map<String,String> params = new HashMap<String,String>();
        params.put("tel",t);
        params.put("alt",alt);
        return sendHttpClientPOSTRequest2(path,params,"UTF-8");//httpclient已经集成在android中
    }



    /*Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Running){
                Intent newintent =new Intent("com.clothes.login.AltActivity");
            }
        }
    };*/



    public static boolean aichangesize(String t) throws Exception {
        String path = "http://172.24.227.25:8080/yiju/andriod/AImeasure";
        Log.i("00000000000000","aaaaaaaaaaaaaaaaaaaaaiiiiiiiiiiii");
        Map<String,String> params = new HashMap<String,String>();
        params.put("tel",t);
        //Run rr = new Run(path,params);
        //new Thread(rr).start();

        try {
            sendHttpClientPOSTRequest(path,params,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sendHttpClientPOSTRequest(path,params,"UTF-8");//httpclient已经集成在android中

    }

    /**

     * 通过HttpClient发送post请求

     * @param path

     * @param params

     * @param encoding

     * @return

     * @throws Exception

     */

    private static boolean sendHttpClientPOSTRequest(String path, Map<String, String> params, String encoding) throws Exception {

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();//存放请求参数

        for(Map.Entry<String, String> entry:params.entrySet()){

            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));

        }

        //防止客户端传递过去的参数发生乱码，需要对此重新编码成UTF-8

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs,encoding);

        HttpPost httpPost = new HttpPost(path);

        httpPost.setEntity(entity);

        DefaultHttpClient client = new DefaultHttpClient();

        HttpResponse response = client.execute(httpPost);

        if(response.getStatusLine().getStatusCode() == 200){

            return true;

        }

        return false;

    }

    private static String sendHttpClientPOSTRequest2(String path, Map<String, String> params, String encoding) throws Exception {

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();//存放请求参数

        for(Map.Entry<String, String> entry:params.entrySet()){

            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));

        }

        //防止客户端传递过去的参数发生乱码，需要对此重新编码成UTF-8

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs,encoding);

        HttpPost httpPost = new HttpPost(path);

        httpPost.setEntity(entity);

        DefaultHttpClient client = new DefaultHttpClient();

        HttpResponse response = client.execute(httpPost);
          HttpEntity mHttpEntity;                          //此处为接收服务器端传过来的string数据以便于判断是否要进行操作
          InputStream inputStream;
          mHttpEntity = response.getEntity();
          inputStream = mHttpEntity.getContent();
          BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
          String result = bufferedReader.readLine();
         Log.i("------------------", result);
        return result;

    }

    /**
     * 新线程，Runable实现，用于图片传输
     */
    static class Run implements Runnable {
        String path;
        Map<String,String> params;
        Run(String path, Map<String,String> params){
            this.path = path;
            this.params = params;
        }
        public void run(){
            try {
                sendHttpClientPOSTRequest(path,params,"UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}