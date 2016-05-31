package com.ionesmile.variousdemo.activity.failure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import com.ionesmile.variousdemo.R;

@SuppressLint("SetJavaScriptEnabled")
public class WebSelectPicActivity extends Activity {

	private String[] paramJS;
	EditText tv_url;
	public WebView webView;
	private Handler myHandler;
	private static final int REQUEST_CODE_UPLOAD = 300;
	public static final int REQUEST_CODE_MULTI_UPLOAD = 3001;
	public static final int REQUEST_CODE_MEDIA_UPLOAD = 3002;
	private JSInterfaceCamera jsInterfaceCamera;
	public int currentUploadNums = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_select_pic);
		myHandler = new Handler();
		/*webView = (WebView) this.findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		tv_url = (EditText) findViewById(R.id.tv_url);
		Button btn = (Button) findViewById(R.id.btn_ok);*/
		tv_url.setText("http://10.1.2.123/webview/");
		/*btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				webView.loadUrl(tv_url.getText().toString());
			}
		});*/

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
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
		jsInterfaceCamera = new JSInterfaceCamera(this, myHandler);
		// 添加js与本地代码通讯接口
		webView.addJavascriptInterface(jsInterfaceCamera,
				"QM_APP_WEBVIEW_ENGINE_camera");
	}

	public void refresh(final Object... param) {
		int flag = ((Integer) param[0]).intValue();// 获取第一个参数
		switch (flag) {
		case 10001:
			break;
		case REQUEST_CODE_UPLOAD:
			webView.post(new Runnable() {
				@Override
				public void run() {
					webView.loadUrl("javascript:"
							+ paramJS[1]
							+ "(\""
							+ (paramJS.length >= 2 ? param[1].toString()
									: "Can't get the callback, confirm your parameters correctly")
							+ "\")");
				}
			});
			break;
		}
	}

	private Bitmap bmp;

	protected void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			upload_carmare_photo(data);
		}
	}


	public String[] getParamJS() {
		return paramJS;
	}

	public void setParamJS(String[] paramJS) {
		this.paramJS = paramJS;
	}

	public void upload_carmare_photo(final Intent data) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				super.run();
				// 选择图片
				Uri uri = data.getData();
				ContentResolver cr = getContentResolver();
				try {
					if (bmp != null)// 如果不释放的话，不断取图片，将会内存不够
						bmp.recycle();
					bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("the bmp toString: " + bmp);
				File fileDir = new File(
						Environment.getExternalStorageDirectory() + "/Myimage");
				if (!fileDir.exists()) {
					fileDir.mkdirs();// 创建文件夹
				}
				File file = new File(Environment.getExternalStorageDirectory()
						+ "/Myimage/", String.valueOf(System
						.currentTimeMillis()) + ".png");
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(file);
					bmp.compress(Bitmap.CompressFormat.JPEG, 50, fos);
					fos.flush();
					fos.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};
		thread.start();
	}

	class JSInterfaceCamera {
		public final int SELECT_PICTURE = 10000;
		public final int SELECT_CAMER = 10002;

		private int PHOTO_REQUEST_CODE = 5;
		// private int ALBUM_REQUEST_CODE = 6;
		public WebSelectPicActivity context;
		public Handler myHandler;

		public JSInterfaceCamera(WebSelectPicActivity a, Handler h) {
			this.context = a;
			this.myHandler = h;
		}

		@JavascriptInterface
		public void request_albums(final String params) {
			myHandler.post(new Runnable() {
				@Override
				public void run() {
					// 获取传过来的参数
					context.setParamJS(params.split(","));
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("image/*");
					context.startActivityForResult(
							Intent.createChooser(intent, "选择图片"),
							SELECT_PICTURE);
				}
			});
		}

		@JavascriptInterface
		public void invoke_camera(final String params) {
			myHandler.post(new Runnable() {
				@Override
				public void run() {
					// 获取传过来的参数
					context.setParamJS(params.split(","));
					String state = Environment.getExternalStorageState();
					if (state.equals(Environment.MEDIA_MOUNTED)) {
						Intent getImageByCamera = new Intent(
								"android.media.action.IMAGE_CAPTURE");
						File fileDir = new File(Environment
								.getExternalStorageDirectory() + "/Myimage");
						if (!fileDir.exists()) {
							fileDir.mkdirs();// 创建文件夹
						}
						File file = new File(Environment
								.getExternalStorageDirectory() + "/Myimage/",
								String.valueOf(System.currentTimeMillis())
										+ ".jpg");
						Uri imageUri = Uri.fromFile(file);
						getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
								imageUri);
						context.startActivityForResult(getImageByCamera,
								SELECT_CAMER);
					} else {
						Toast.makeText(context.getApplicationContext(),
								"请确认已经插入SD卡", Toast.LENGTH_LONG).show();
					}
				}
			});
		}
	}
}
