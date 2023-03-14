package com.clothes.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.R;
import com.clothes.httprequest.NewsService;
import com.clothes.login.utils.ActivityCollectorUtils;
import com.clothes.login.utils.SharePreferenceUtil;
import com.louisgeek.louiscustomcamerademo.CameraActivity;

public class UsermessageActivity extends AppCompatActivity {
    private TextView textView[];
    String []information = {"用户名","身高","体重","出生日期","性别","胸围","腰围"};
    private Button butt;
    private Button exit;

    Intent intent1;
    ActivityCollectorUtils ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermessage);
        butt = (Button) findViewById(R.id.button_change);
        butt.setOnClickListener(L2_button_listener);
        exit = (Button) findViewById(R.id.button_exit);
        exit.setOnClickListener(L1_button_listener);
        textView = new TextView[7];
        textView[0] = (TextView) findViewById(R.id.textView_username);
        textView[1] = (TextView) findViewById(R.id.textView_height);
        textView[2] = (TextView) findViewById(R.id.textView_weight);
        textView[3] = (TextView) findViewById(R.id.textView_burndate);
        textView[4] = (TextView) findViewById(R.id.textView_gender);
        textView[5] = (TextView) findViewById(R.id.textView_bust);
        textView[6] = (TextView) findViewById(R.id.textView_waistline);
        SharePreferenceUtil.getParam(getApplicationContext(), "username");
        try {
            getdisplay();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ac.addActivity(this);
        intent1 = getIntent();
    }
    Button.OnClickListener L2_button_listener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(UsermessageActivity.this, UsermessageActivity.class);
            if (intent1.getStringExtra("alt").equals("0"))
                intent = new Intent(UsermessageActivity.this, SizeActivity.class);
            else if (intent1.getStringExtra("alt").equals("1"))
                intent = new Intent(UsermessageActivity.this, CameraActivity.class);
            startActivity(intent);
            finish();
        }
    };

    Button.OnClickListener L1_button_listener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UsermessageActivity.this, LoginActivity.class);
            startActivity(intent);
            ac.finishAllActivity();
        }
    };
    private void getdisplay() throws Exception {
            // TODO: 请求服务器查询用户信息并显示
            String tel = (String) SharePreferenceUtil.getParam(this,"username");
            Run rr = new Run(tel);
            new Thread(rr).start();
    }
    class Run implements Runnable {
        private String tel;
        Run(String tel){
            this.tel = tel;
        }
        public void run(){
            try {
                String str = NewsService.display(tel,intent1.getStringExtra("alt"));
                Log.i("-----++++++", str);
                String []result = str.split("#");
                for(int i=0;i<result.length;i++){
                    if (result[i]!=null){
                    textView[i].setText(information[i]+": \n"+result[i]);
                    }
                    Log.i("-----++++++", result[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
