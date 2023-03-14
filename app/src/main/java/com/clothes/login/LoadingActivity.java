package com.clothes.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.R;
import com.clothes.login.utils.ActivityCollectorUtils;

public class LoadingActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private ImageView image2;

    ActivityCollectorUtils ac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
        setContentView(R.layout.activity_loading);
        image2 = (ImageView)findViewById(R.id.frontpage);
        int num=(int)(Math.random()*2);
        switch (num){
            case 1:
                image2.setBackground(getResources().getDrawable(R.drawable.one));//变形
                image2.setBackgroundResource(R.drawable.one);//变形
                image2.setBackgroundDrawable(getResources().getDrawable(R.drawable.one));
                break;

            default:
                image2.setBackground(getResources().getDrawable(R.drawable.three));//变形
                image2.setBackgroundResource(R.drawable.three);//变形
                image2.setBackgroundDrawable(getResources().getDrawable(R.drawable.three));
                break;

        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);//延时

        ac.addActivity(this);
    }

}
