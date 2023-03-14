package com.clothes.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.R;
import com.clothes.httprequest.NewsService;
import com.clothes.login.utils.MarqueeTextView;
import com.clothes.login.utils.Num;
import com.clothes.login.utils.SharePreferenceUtil;
import com.louisgeek.louiscustomcamerademo.PreviewActivity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AltActivityNew extends AppCompatActivity implements AdapterView.OnItemClickListener{
    MarqueeTextView PMD ;
    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    // 图片封装为一个数组
    private int[] icon = {R.drawable.camera, R.drawable.user, R.drawable.cloudcomputer,
            R.drawable.alternative, R.drawable.alternative, R.drawable.alternative};
    private String[] iconName = { "智能量体", "个人信息",
            "AI测量信息", "其他1", "其他2", "其他3" };

    String s;
    //图片文件路径（拍照用）
    String mFilePath;
    FileInputStream mFis;

    private Handler handler;

    private ProgressDialog pd;

    private final int TAKE_PHOTO_FINISH = 1002;
    private final int TAKE_PHOTO_ING = 1003;
    private final int TAKE_PHOTO_ENDING = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alt_new);
        gview = (GridView) findViewById(R.id.gview);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        //新建适配器
        String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.item, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);

        //设置监听器
        gview.setOnItemClickListener(this);
        final Intent intent2=new Intent(this,UsermessageActivity.class);

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
                Toast.makeText(AltActivityNew.this, "position"+position, Toast.LENGTH_SHORT).show();
            }
        });

        PMD = (MarqueeTextView)findViewById(R.id.pmd);
        PMD.setText("这    是    一    个    测    试    的    通    知    ，    无    法    点    击    ,    可    以    替    换    或    者    从    网    上    更    新");

        //图片计数器

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == TAKE_PHOTO_FINISH) {
                    // byte[] bitmapByte= (byte[]) msg.obj;
                    if (msg.obj == null) {
                        return;
                    }
                    Intent intent2 = getIntent();
                    String ss = intent2.getStringExtra("tel");
                    String filePath = msg.obj.toString();
                    String mm = Integer.toString(Num.num);
                    String b2 =  (String) SharePreferenceUtil.getParam(getApplicationContext(),"username");

                    //Intent intent = new Intent(AltActivityNew.this, PreviewActivity.class);
                    //intent.putExtra("filePath", filePath);
                    //intent.putExtra("tel", ss);
                    //Log.i("00000000000000",mm);
                    //intent.putExtra("num",mm);
                    //AltActivityNew.this.startActivity(intent);
                    PreviewActivity.LetSave(filePath,mm,b2);
                    if (Num.num == 8){
                        try{
                            NewsService.aichangesize(ss);
                            System.out.println("使用智能测量");
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("出错了");
                        }
                        //AltActivityNew.this.finish();

                    }
                }
                else if (msg.what == TAKE_PHOTO_ENDING){

                    pd.dismiss();
                    intent2.putExtra("alt","1");
                    startActivity(intent2);

                }
            }
        };
    }
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position,
        long id) {
            switch (position) {
                case 0:
                    //Intent intent=new Intent(this, CameraActivity.class);
                    //startActivity(intent);
                    Num.num = 1;
                    mFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "picture.jpg";
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri uri = Uri.fromFile(new File(mFilePath));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, 1);
                    break;
                case 1:
                    Intent intent1=new Intent(this,UsermessageActivity.class);
                    s = "0";
                    intent1.putExtra("alt",s);
                    startActivity(intent1);
                    break;
                case 2:
                    Intent intent2=new Intent(this,UsermessageActivity.class);
                    s = "1";
                    Intent intentt = getIntent();
                        pd = ProgressDialog.show(AltActivityNew.this, "同步", "正在同步…");
                        new Thread() {
                            public void run() {
                                //在这里执行长耗时方法
                                try{
                                NewsService.aichangesize("13061249062");
                                System.out.println("使用智能测量");
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println("出错了");
                                }
                                //执行完毕后给handler发送一个消息
                                //handler.sendEmptyMessage(TAKE_PHOTO_ENDING);
                                Message message = handler.obtainMessage();
                                message.what = TAKE_PHOTO_ENDING;
                                message.obj = "1";
                                message.sendToTarget();
                            }
                        }.start();

                    /*intent2.putExtra("alt",s);
                    startActivity(intent2);*/
                    break;
                default:
                    break;
            }
        }

    public List<Map<String, Object>> getData(){
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }

        return data_list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FLAG_CHOOCE_PICTURE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String imgPath = getUrl(uri);
            Log.d("", "CameraSurfaceView imgPath : " + imgPath);
        }*/
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            try {
                mFis = new FileInputStream(mFilePath);
                Bitmap bmp = BitmapFactory.decodeStream(mFis);
                //ImageView img2.setImageBitmap(bitmap);
                //textView2.setText("width--->" + bitmap.getWidth() + "\n" + "height--->" + bitmap.getHeight() + "\n" + "byte--->" + bitmap.getByteCount() / 1024)
                String filePath = mFilePath;
                Message message = handler.obtainMessage();
                if(Num.num < 8){
                    message.what = TAKE_PHOTO_FINISH;
                    //Num.num++;
                    message.obj = filePath;
                    message.sendToTarget();


                    mFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "picture.jpg";
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri uri = Uri.fromFile(new File(mFilePath));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, 1);


                }
                else{
                    message.what = TAKE_PHOTO_FINISH;
                    message.obj = filePath;
                    message.sendToTarget();
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (mFis != null)
                        mFis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
