package com.clothes.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.R;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    List images = new ArrayList();
        images.add("http://image14.m1905.cn/uploadfile/2018/0907/thumb_1_1380_460_20180907013518839623.jpg");
        images.add("http://image14.m1905.cn/uploadfile/2018/0906/thumb_1_1380_460_20180906040153529630.jpg");
        images.add("http://image13.m1905.cn/uploadfile/2018/0907/thumb_1_1380_460_20180907114844929630.jpg");


    Banner banner = (Banner) findViewById(R.id.banner);
    //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
    //设置图片集合
        banner.setImages(images);
    //banner设置方法全部调用完毕时最后调用
        banner.start();

    //增加点击事件
        banner.setOnBannerListener(new OnBannerListener() {
        @Override
        public void OnBannerClick(int position) {
            Toast.makeText(TestActivity.this, "position"+position, Toast.LENGTH_SHORT).show();
        }
    });
}
}