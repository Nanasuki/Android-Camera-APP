package com.clothes.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.R;
import com.clothes.httprequest.NewsService;
import com.clothes.login.utils.ActivityCollectorUtils;
import com.clothes.login.utils.RegexUtils;
import com.clothes.login.utils.ToastUtils;
import com.clothes.login.utils.VerifyCodeManager;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * @desc 注册界面
 * 功能描述：一般会使用手机登录，通过获取手机验证码，跟服务器交互完成注册
 * Created by devilwwj on 16/1/24.
 */
public class SignUpActivity extends Activity implements OnClickListener{
    private static final String TAG = "SignupActivity";
    // 界面控件
    private EditText phoneEdit;
    private EditText passwordEdit;
    private EditText hightEdit;
    private EditText weightEdit;
    private EditText burndateEdit;
    private EditText genderEdit;
    private EditText verifyEdit;
    private VerifyCodeManager codeManager;

    ActivityCollectorUtils ac;
    Run rr;
    private Button bt_verify;
    EventHandler eventHandler;
    String phone_number;
    private boolean flag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MobSDK.init(this, "2bc7c74d06449","57bfa4f18951389c71a89b57cf9ad8cd");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_new);
        initViews();

        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                new Handler(Looper.getMainLooper(), new Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        int event = msg.arg1;
                        int result = msg.arg2;
                        Object data = msg.obj;
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                // TODO 处理成功得到验证码的结果
                                ToastUtils.showShort(SignUpActivity.this,"验证码发送成功");
                                // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                            } else {
                                // TODO 处理错误的结果
                                ToastUtils.showShort(SignUpActivity.this,"验证码发送失败");
                                ((Throwable) data).printStackTrace();
                            }
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                // TODO 处理验证码验证通过的结果
                                new Thread(rr).start();
                                ToastUtils.showShort(SignUpActivity.this,"验证码验证成功");
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                // TODO 处理错误的结果
                                flag = false;
                                verifyEdit.requestFocus();
                                ToastUtils.showShort(SignUpActivity.this,"验证码验证失败");
                                ((Throwable) data).printStackTrace();
                            }
                        }
                        // TODO 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                        return false;
                    }
                }).sendMessage(msg);
            }
        };

        SMSSDK.registerEventHandler(eventHandler);

        ac.addActivity(this);
    }


    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    /**
     * 通用findViewById,减少重复的类型转换
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            Log.e(TAG, "Could not cast View to concrete class.", ex);
            throw ex;
        }
    }


    private void initViews() {

        phoneEdit = getView(R.id.act_home_et_phone);
        phoneEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);// 下一步
        passwordEdit = getView(R.id.act_reg_et_pwd);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        burndateEdit = getView(R.id.act_reg_et_burndate);
        burndateEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        genderEdit = getView(R.id.act_reg_et_gender);
        genderEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        hightEdit = getView(R.id.act_reg_et_height);
        hightEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        verifyEdit = getView(R.id.act_home_et_sms_code);
        verifyEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        weightEdit = getView(R.id.act_reg_et_weight);
        weightEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        weightEdit.setImeOptions(EditorInfo.IME_ACTION_GO);
        weightEdit.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // 点击虚拟键盘的done
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {
                    commit();
                }
                return false;
            }
        });
    }



    private void commit() {
        String phone = phoneEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        String hight = hightEdit.getText().toString().trim();
        String weight = weightEdit.getText().toString().trim();
        String burndate = burndateEdit.getText().toString().trim();
        String gender = genderEdit.getText().toString().trim();

        if(checkInput(phone,password,hight,weight)) {
            Run rr = new Run(phone, password,hight,weight,burndate,gender);
            new Thread(rr).start();
        }
    }

    private boolean checkInput(String phone, String password,String hight,String weight) {
        if (TextUtils.isEmpty(phone)) { // 电话号码为空
            phoneEdit.requestFocus();
            ToastUtils.showShort(this, R.string.tip_phone_can_not_be_empty);
        } else {
            if (!RegexUtils.checkMobile(phone)) { // 电话号码格式有误
                phoneEdit.requestFocus();
                ToastUtils.showShort(this, R.string.tip_phone_regex_not_right);
            } else if (password.length() < 6 || password.length() > 32
                    || TextUtils.isEmpty(password)) { // 密码格式
                passwordEdit.requestFocus();
                ToastUtils.showShort(this,
                        R.string.tip_please_input_6_32_password);
            } else if(TextUtils.isEmpty(hight)){
                hightEdit.requestFocus();
                ToastUtils.showShort(this, R.string.tip_hight_empty);

            }else if(TextUtils.isEmpty(weight)){
                weightEdit.requestFocus();
                ToastUtils.showShort(this, R.string.tip_weight_empty);
            } else{
               return true;
            }
        }

        return false;
    }


    @Override
    public void onClick(View v) {
        String phone = phoneEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        String hight = hightEdit.getText().toString().trim();
        String weight = weightEdit.getText().toString().trim();
        String verify = verifyEdit.getText().toString().trim();
        String burndate = burndateEdit.getText().toString().trim();
        String gender = genderEdit.getText().toString().trim();
        Log.i("000000000000000",phone);
        switch (v.getId()) {
            case R.id.frag_register_tv_getcode:
                // TODO 请求接口发送验证码
                Log.i("000000000000000","000000000000000000000000");
                if (checkInput(phone,password,hight,weight))
                    SMSSDK.getVerificationCode("86",phone);
                verifyEdit.requestFocus();
                break;
            case R.id.act_reg_tv_reg:
                // TODO 注册账号
                if (checkInput(phone,password,hight,weight)){
                    rr = new Run(phone, password,hight,weight,burndate,gender);
                    SMSSDK.submitVerificationCode("86",phone,verify);
                }
                break;
            default:
                break;
        }
    }
    class Run2 implements  Runnable{
        String phone;
        String verify;

        Run2(String phone,String verify){
            this.phone = phone;
            this.verify = verify;
        }
        public void run(){
            try {
                SMSSDK.submitVerificationCode("86",phone,verify);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class Run implements Runnable {

        private String iphone;
        private String password;
        private String hight;
        private  String weight;
        private String burndate;
        private String gender;
        Run(String iphone,String password,String hight,String weight,String burndate,String gender){
            this.iphone = iphone;
            this.password = password;
            this.hight = hight;
            this.weight = weight;
            this.burndate = burndate;
            this.gender = gender;
        }
        public void run(){
            try {
                NewsService.save2(iphone,password,hight,weight,burndate,gender);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
