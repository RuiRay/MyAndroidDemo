package com.ionesmile.variousdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class WebViewActivity extends Activity {

	private static final String TAG = WebViewActivity.class.getName();

	private WebView mWebView;
	private Button btnShowInfo;
	private JSKit js;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		// 初始化控件
		mWebView = (WebView) findViewById(R.id.wv_test);
		btnShowInfo = (Button) findViewById(R.id.btn_showmsg);
		// 实例化js对象
		js = new JSKit(this);
		// 设置参数
		mWebView.getSettings().setBuiltInZoomControls(true);
		// 内容的渲染需要webviewChromClient去实现，设置webviewChromClient基类，解决js中alert不弹出的问题和其他内容渲染问题
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				Log.i("myTag", "---url = " + url + "       message = "
						+ message);
				AlertDialog.Builder builder = new AlertDialog.Builder(view
						.getContext());
				builder.setTitle("提示").setMessage(message)
						.setPositiveButton("确定", null);
				// 不需要绑定按键事件
				// 屏蔽keycode等于84之类的按键
				builder.setOnKeyListener(new OnKeyListener() {

					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						Log.v("onJsAlert", "keyCode==" + keyCode + "event="
								+ event);
						return true;
					}
				});
				// 禁止响应按back键的事件
				builder.setCancelable(false);
				AlertDialog dialog = builder.create();
				dialog.show();
				result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
				return true;
			}
		});
		// 如果希望点击链接继续在当前browser中响应，而不是新开Android的系统browser中响应该链接，必须覆盖webview的WebViewClient对象
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		mWebView.getSettings().setJavaScriptEnabled(true);
		// 把js绑定到全局的myjs上，myjs的作用域是全局的，初始化后可随处使用
		mWebView.addJavascriptInterface(js, "myjs");

		mWebView.loadUrl("file:///android_asset/jstest.html");

		btnShowInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						// 调用 HTML 中的javaScript 函数
						mWebView.loadUrl("javascript:showMsg()");
					}
				});
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onBackPressed() {
		// 这里处理逻辑代码，该方法仅适用于2.0或更高版本的sdk 
		super.onBackPressed();
	}

	public class JSKit {
		private Context mContext;

		public JSKit(Context context) {
			this.mContext = context;
		}

		@JavascriptInterface
		// sdk17版本以上加上注解
		public void showMsg(String msg) {
			Log.i(TAG, "JSKit showMsg msg = " + msg);
			Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
		}

		@JavascriptInterface
		public void openCamera() {
			Intent intent = new Intent();
			intent.setAction("android.media.action.IMAGE_CAPTURE");
			intent.addCategory("android.intent.category.DEFAULT");
			mContext.startActivity(intent);
		}

		@JavascriptInterface
		public void selectAlbums() {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			((Activity) mContext).startActivityForResult(
					Intent.createChooser(intent, "选择图片"), 0);
		}
	}
}
