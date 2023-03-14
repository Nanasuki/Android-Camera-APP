package com.clothes.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.R;
import com.clothes.login.utils.ActivityCollectorUtils;
import com.louisgeek.louiscustomcamerademo.CameraActivity;

public class AltActivity extends AppCompatActivity  {
    private Button bt_call;
    private Button bt_call2;
    ActivityCollectorUtils ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alt);
        bt_call = (Button) findViewById(R.id.butt_message);
        bt_call2 = (Button) findViewById(R.id.butt_measure);
        bt_call.setOnClickListener(L1_button_listener);
        bt_call2.setOnClickListener(L2_button_listener);

        ac.addActivity(this);
    }

        //设点事件第一种

    Button.OnClickListener L1_button_listener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AltActivity.this, UsermessageActivity.class);
            startActivity(intent);
        }
    };
    Button.OnClickListener L2_button_listener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AltActivity.this, CameraActivity.class);
            startActivity(intent);
        }
    };

}