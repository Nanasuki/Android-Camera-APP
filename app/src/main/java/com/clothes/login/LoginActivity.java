package com.clothes.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.R;
import com.clothes.httprequest.NewsService;
import com.clothes.login.global.AppConstants;
import com.clothes.login.utils.ActivityCollectorUtils;
import com.clothes.login.utils.RegexUtils;
import com.clothes.login.utils.SharePreferenceUtil;
import com.clothes.login.views.CleanEditText;
import com.clothes.store.FileService;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static android.view.View.OnClickListener;

/**
 * @desc 登录界面
 * Created by devilwwj on 16/1/24.
 */
public class LoginActivity extends Activity implements OnClickListener {

    private static final String TAG = "loginActivity";
    private static final int REQUEST_CODE_TO_REGISTER = 0x001;

    ActivityCollectorUtils ac;

    // 界面控件
    private CleanEditText accountEdit;
    private CleanEditText passwordEdit;

    // 整个平台的Controller，负责管理整个SDK的配置、操作等处理
    private UMSocialService mController = UMServiceFactory
            .getUMSocialService(AppConstants.DESCRIPTOR);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        accountEdit.setText((String)SharePreferenceUtil.getParam(this,"username"));
        passwordEdit.setText((String)SharePreferenceUtil.getParam(this,"userpwd"));

        ac.addActivity(this);
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        accountEdit = (CleanEditText) this.findViewById(R.id.et_email_phone);
        accountEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        accountEdit.setTransformationMethod(HideReturnsTransformationMethod
                .getInstance());
        passwordEdit = (CleanEditText) this.findViewById(R.id.et_password);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_GO);
        passwordEdit.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {
                    try {
                        clickLogin();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }

    /**
     * 登录模块-与服务器数据库连接的实现
     */
    private void clickLogin() throws Exception {
        Intent intent2 = new Intent(this, AltActivityNew.class);
        String account = accountEdit.getText().toString();
        String password = passwordEdit.getText().toString();
       if(checkInput(account,password)){
            // TODO: 请求服务器登录账号
           if(check(account, password).equals("success")) {
               FileService f = new FileService(this);

               SharePreferenceUtil.setParam(getApplicationContext(),"username",account);
               SharePreferenceUtil.setParam(getApplicationContext(), "userpwd", password);
               Log.i("-----++++++", "ssssssssssss");
//               Log.i("-----++++++", b2);String b2 =  (String)
//               f.saveToRom(account);
//               intent2.putExtra("tel", account);
                 startActivity(intent2);
               Log.i("-----++++++", "kkkkkkkkkkkkkk");
           }else{
               Toast.makeText(this, R.string.tip_account2_regex_not_right,
                       Toast.LENGTH_LONG).show();
           }
        }
    }
    private String check(String account, String password) throws Exception {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        TaskCallable task = new TaskCallable(account,password);
        Future<String> future = threadPool.submit(task);
        String ss = future.get();
            if (ss.equals("zhengque")) {
                return "success";
            } else {
                return "fail";
            }
    }

    /**
     * 检查输入
     *
     * @param account
     * @param password
     * @return
     */
    public boolean checkInput(String account, String password) {
        // 账号为空时提示
        if (account == null || account.trim().equals("")) {
            Toast.makeText(this, R.string.tip_account_empty, Toast.LENGTH_LONG)
                    .show();
        } else {
            // 账号不匹配手机号格式（11位数字且以1开头）
            if (!RegexUtils.checkMobile(account)) {
                Toast.makeText(this, R.string.tip_account_regex_not_right,
                        Toast.LENGTH_LONG).show();
            } else if (password == null || password.trim().equals("")) {
                Toast.makeText(this, R.string.tip_password_can_not_be_empty,
                        Toast.LENGTH_LONG).show();
            } else {
                return true;
            }
        }


        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_cancel:
                finish();
                break;
            case R.id.btn_login:
                try {
                    clickLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.tv_create_account:
                enterRegister();
                break;
            default:
                break;
        }
    }


//    /**
//     * 跳转到忘记密码
//     */
//    private void enterForgetPwd() {
//        Intent intent = new Intent(this, ForgetPasswordActivity.class);
//        startActivity(intent);
//    }

    /**
     * 跳转到注册页面
     */
    private void enterRegister() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivityForResult(intent, REQUEST_CODE_TO_REGISTER);
    }
    class TaskCallable implements Callable<String> {

        private String tel;
        private String password;
        TaskCallable(String tel,String password){
            this.tel = tel;
            this.password = password;
        }
        public String call() throws Exception {
            String ss =  NewsService.verify(tel,password);
            return  ss;
        }
        }
    }
