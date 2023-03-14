package com.louisgeek.louiscustomcamerademo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.clothes.httprequest.NewsService;
import com.clothes.login.utils.Num;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PreviewActivity{




    public static void LetSave(String filePath,String mm,String b2) {
        Num.num++;
        Bitmap lvBitmap =  getBitmapByUrl(filePath);
        String b = Bitmap2StrByBase64(lvBitmap);
        Map<String,String> map = new HashMap<String,String>();
        Log.i("+6666666666666666666",mm);
        //String b2 =  (String) SharePreferenceUtil.getParam(getApplicationContext(),"username");
        Log.i("此为获取到的信息++++++", b2);
        Run rr = new Run(b,b2,mm);
        new Thread(rr).start();
    }


    /**
     * 根据图片路径获取本地图片的Bitmap
     *
     * @param url
     * @return
     */

    public static Bitmap getBitmapByUrl(String url) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = new FileInputStream(url);
            bitmap = BitmapFactory.decodeStream(fis);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            bitmap = null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                fis = null;
            }
        }

        return bitmap;
    }


    /**
     * 处理bitmap图片
     * bitmap 转Base64字符串
     */
    //
    public static String Bitmap2StrByBase64(Bitmap bit){
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 50, bos);//参数100表示不压缩
        byte[] bytes=bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 新线程，Runable实现，用于图片传输
     */
    static class Run implements Runnable {

        private String ss;
        private String s2;
        private String s3;
        Run(String ss,String s2,String s3){
            this.ss = ss;
            this.s2 = s2;
            this.s3 = s3;
        }
        public void run(){
            try {
                NewsService.save(ss,s2,s3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}










