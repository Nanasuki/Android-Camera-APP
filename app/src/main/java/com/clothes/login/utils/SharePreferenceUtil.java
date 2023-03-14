package com.clothes.login.utils;

/**
 * Created by www86 on 2019/7/17.
 */

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用来进行共享参数的存取操作
 */
public final class SharePreferenceUtil {
    private SharePreferenceUtil(){}
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_date";


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param context 上下文对象
     * @param key key
     * @param object 需要存储的数据
     */
    public static void setParam(Context context , String key, Object object){

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences("Login", Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, (String)object);
        if("String".equals(type)){
            editor.putString(key, (String)object);
        }
        else if("Integer".equals(type)){
            editor.putInt(key, (Integer)object);
        }
        else if("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)object);
        }
        else if("Float".equals(type)){
            editor.putFloat(key, (Float)object);
        }
        else if("Long".equals(type)){
            editor.putLong(key, (Long)object);
        }

        editor.apply();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param context 上下文对象
     * @param key key
     * @return
     */
    public static Object getParam(Context context , String key){
        SharedPreferences sp = context.getSharedPreferences("Login", Context.MODE_WORLD_READABLE);
            return sp.getString(key,"");
    }
}