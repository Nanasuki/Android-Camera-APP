package com.clothes.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.R;
import com.clothes.httprequest.NewsService;
import com.clothes.login.global.AppConstants;
import com.clothes.login.utils.ActivityCollectorUtils;
import com.clothes.login.utils.SharePreferenceUtil;
import com.clothes.login.views.CleanEditText;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

import static android.view.View.OnClickListener;

/**
 * @desc 登录界面
 * Created by devilwwj on 16/1/24.
 */
public class SizeActivity extends Activity implements OnClickListener {

    private static final String TAG = "sizeActivity";
    private static final int REQUEST_CODE_TO_REGISTER = 0x001;

    ActivityCollectorUtils ac;

    // 界面控件
    private CleanEditText bustEdit;
    private CleanEditText waistlineEdit;

    // 整个平台的Controller，负责管理整个SDK的配置、操作等处理
    private UMSocialService mController = UMServiceFactory
            .getUMSocialService(AppConstants.DESCRIPTOR);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inwrite);
        initViews();

        ac.addActivity(this);
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        bustEdit = (CleanEditText) this.findViewById(R.id.et_bust);
        bustEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        bustEdit.setTransformationMethod(HideReturnsTransformationMethod
                .getInstance());
        waistlineEdit = (CleanEditText) this.findViewById(R.id.et_waistline);
        waistlineEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        waistlineEdit.setImeOptions(EditorInfo.IME_ACTION_GO);
        waistlineEdit.setTransformationMethod(HideReturnsTransformationMethod
                .getInstance());
        waistlineEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {
                    try {
                        clicksize();
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
    private void clicksize() throws Exception {
           //bust:胸围  waistline:腰围
        String bust = bustEdit.getText().toString();
        String waistline = waistlineEdit.getText().toString();
       if(checkInput(bust,waistline)){
            // TODO: 请求服务器改变尺寸
           String tel = (String) SharePreferenceUtil.getParam(this,"username");
           Run rr = new Run(tel,bust, waistline);
           new Thread(rr).start();
               Toast.makeText(this, R.string.size_insert,
                       Toast.LENGTH_LONG).show();
           }
        }

    /**
     * 检查输入
     *
     * @param bust
     * @param waistline
     * @return
     */
    public boolean checkInput(String bust, String waistline) {
        // 胸围为空时提示
        if (bust == null || bust.trim().equals("")) {
            Toast.makeText(this, R.string.tip_bust_empty, Toast.LENGTH_LONG).show();
        } else {
            // 腰围为空时提示

            if (waistline == null || waistline.trim().equals("")) {
                Toast.makeText(this, R.string.tip_waistline_empty,
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
            case R.id.btn_change_size:
                try {
                    clicksize();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }


    class Run implements Runnable {
        private String tel;
        private String bust;
        private String waistline;
        Run(String tel,String bust,String waistline){
            this.tel = tel;
            this.bust = bust;
            this.waistline = waistline;
        }
        public void run(){
            try {
                NewsService.changesize(tel,bust,waistline);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    }
