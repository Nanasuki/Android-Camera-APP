package com.clothes.store;

/**
 * Created by www86 on 2019/7/3.
 */

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class FileService {


    private Context context;

    public FileService(Context context){
        this.context = context;
    }
    /**
     * 把用户名和密码保存到手机ROM
     * @param username 要保存的用户名
     * @return
     */
    public boolean saveToRom(String username) throws Exception{
        //以私有的方式打开一个文件
        File file = new File(Environment.getExternalStorageDirectory().toString()+File.separator +username+".txt");
        if (!file.exists()) {
            File dir = new File(file.getParent());
            dir.mkdirs();
            file.createNewFile();

        }
        FileOutputStream fos = new FileOutputStream (file);
        String result = username;
        fos.write(result.getBytes());
        fos.flush();
        fos.close();
        return true;
    }
    public static Map<String,String> getUserInfo(String name) throws Exception{
        File file = new File(Environment.getExternalStorageDirectory().toString()+File.separator + name +".txt");
        FileInputStream fis = new FileInputStream(file);
        //以上的两句代码也可以通过以下的代码实现：
        //FileInputStream fis = context.openFileInput(filename);
        byte[] data = StreamTools.getBytes(fis);
        String result = new String(data);
        Map<String,String> map = new HashMap<String,String>();
        map.put("username", result);
        return map;
    }
}