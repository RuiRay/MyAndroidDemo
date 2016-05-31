package com.ionesmile.variousdemo.activity.failure;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.ionesmile.variousdemo.R;

public class WebCaptureActivity extends Activity {

	private final static String TAG = "CameraWebviewActivity";
        
    private Button bt;
    private WebView wv;
    public String fileFullName;//照相后的照片的全整路径
    private boolean fromTakePhoto; //是否是从摄像界面返回的webview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_capture);
        initViews();
    }
    
    private void initViews() {
        bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                        System.out.println("----------------");
                        takePhoto( Math.random()*1000+1 + ".jpg");
                }
        });
        
        wv = (WebView) findViewById(R.id.wv);
        WebSettings setting = wv.getSettings();
        setting.setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                }
                
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return super.shouldOverrideUrlLoading(view, url);
                }
                
                @Override
                public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                }
                
        });
        
        wv.setWebChromeClient(new WebChromeClient(){
                @Override//实现js中的alert弹窗在Activity中显示
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
           Log.d(TAG, message);
            result.confirm();
            return true;
        }
        });
        
        wv.loadUrl("file:///android_asset/index.html");
        final Handler mHandler = new Handler();
        //webview增加javascript接口，监听html页面中的js点击事件
        wv.addJavascriptInterface(new Object(){
                public String clickOnAndroid() {//将被js调用
            mHandler.post(new Runnable() {
                public void run() {
                        fromTakePhoto  = true;
                        //调用 启用摄像头的自定义方法
                    takePhoto("testimg" + Math.random()*1000+1 + ".jpg");
                    System.out.println("========fileFullName: " + fileFullName);
                    
                }
            });
            return fileFullName;
        }
        }, "demo");
    }
    
    /*
     * 调用摄像头的方法
     */
    public void takePhoto(String filename) {
        System.out.println("----start to take photo2 ----");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_MEDIA_TITLE, "TakePhoto");
        
        //判断是否有SD卡
        String sdDir = null;
        boolean isSDcardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if(isSDcardExist) {
                sdDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
                sdDir = Environment.getRootDirectory().getAbsolutePath();
        }
        //确定相片保存路径
        String targetDir = sdDir + "/" + "webview_camera";
        File file = new File(targetDir);
        if (!file.exists()) {
                file.mkdirs();
        }
        fileFullName = targetDir + "/" + filename;
        System.out.println("----taking photo fileFullName: " + fileFullName);
        //初始化并调用摄像头
        intent.putExtra(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(fileFullName)));
        startActivityForResult(intent, 1);
    }
    
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     * 重写些方法，判断是否从摄像Activity返回的webview activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("----requestCode: " + requestCode + "; resultCode " + resultCode + "; fileFullName: " + fileFullName);
        if (fromTakePhoto && requestCode ==1 && resultCode ==-1) {
                wv.loadUrl("javascript:wave2('" + fileFullName + "')");
        } else {
                wv.loadUrl("javascript:wave2('Please take your photo')");
        }
        fromTakePhoto = false;
        super.onActivityResult(requestCode, resultCode, data);
    }

}
