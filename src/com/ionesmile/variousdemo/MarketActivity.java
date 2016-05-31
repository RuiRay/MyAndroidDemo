package com.ionesmile.variousdemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ionesmile.variousdemo.utils.MarketUtil;
import com.ionesmile.variousdemo.utils.MyBaseAdapter;

public class MarketActivity extends Activity {

	//private final String packageName = "com.snaillove.app.snailbulb.general";	// getPackageName()
	private final String packageName = "com.tencent.mobileqq";	// getPackageName()
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_market);
	}

	private String[] validPackageNames = new String[]{
			// "com.android.vending",	// GooglePlay
			"com.baidu.appsearch",		// 百度助手
			"com.qihoo.appstore",		// 360手机助手	
			"com.ijinshan.ShouJiKongService", 		// 金山手机助手
			"com.tencent.android.qqdownloader"};	// 应用宝 
	
	// 按钮的单击事件，点击后系统会弹出所有支持改意图的应用供选择
	public void gotoMarket(View v) {
	     Intent paramIntent = MarketUtil.getIntent(packageName);
	     List<ResolveInfo> resolveList = MarketUtil.getResolveInfos(getApplicationContext(), paramIntent, validPackageNames);
	     if (resolveList == null || resolveList.size() == 0) {
				// TODO open WebView
				Toast.makeText(this, "empty market app", Toast.LENGTH_SHORT).show();
			} else {
				startActivity(paramIntent);
			}
	}
	
	// 按钮的单击事件，点击后弹出对话框显示自定义的部分应用
	public void gotoMarketCustom(View v) {
		Intent paramIntent = MarketUtil.getIntent(packageName);
		List<ResolveInfo> resolveList = MarketUtil.getResolveInfos(getApplicationContext(), paramIntent, validPackageNames);
		if (resolveList == null || resolveList.size() == 0) {
			// TODO open WebView
			Toast.makeText(this, "empty market app", Toast.LENGTH_SHORT).show();
		} else {
			if (resolveList.size() == 1) {
				paramIntent.setPackage(resolveList.get(0).activityInfo.packageName);
				startActivity(paramIntent);
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("请选择应用");
				ListView lvMarket = new ListView(this);
				builder.setView(lvMarket);
				List<MarketBean> marketBeans = parseMarkBean(resolveList);
				MarketAppAdapter marketAppAdapter = new MarketAppAdapter(this, marketBeans);
				lvMarket.setAdapter(marketAppAdapter);
				lvMarket.setOnItemClickListener(new OnItemClickListener() {
					
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						MarketBean bean = (MarketBean) parent.getItemAtPosition(position);
						Intent intent = new Intent();
						intent.setAction("android.intent.action.VIEW");
						intent.setData(Uri.parse("market://details?id=" + packageName));
						intent.setPackage(bean.getmPackageName());
						startActivity(intent);
					}
				});
				AlertDialog dialog = builder.create();
				dialog.setCanceledOnTouchOutside(true);	// 设置点击对话框以外部分取消对话框
				dialog.show();
			}
		}
	}

	private List<MarketBean> parseMarkBean(List<ResolveInfo> resolveList) {
		List<MarketBean> marketBeans = new ArrayList<MarketBean>();
	     PackageManager pManager = getPackageManager();
	     for (ResolveInfo item : resolveList) {
	    	 MarketBean bean = new MarketBean();
	    	 bean.setmIcon(item.loadIcon(pManager));
	    	 bean.setmLabel(item.loadLabel(pManager).toString());
	    	 bean.setmPackageName(item.activityInfo.packageName);
	    	 marketBeans.add(bean);
	     }
		return marketBeans;
	}
	
	class MarketAppAdapter extends MyBaseAdapter<MarketBean>{

		public MarketAppAdapter(Context context, List<MarketBean> data) {
			super(context, data);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_show_title, null);
				holder = new ViewHolder();
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.tvLable = (TextView) convertView.findViewById(R.id.tv_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			MarketBean bean = getItem(position);
			holder.ivIcon.setImageDrawable(bean.getmIcon());
			holder.tvLable.setText(bean.getmLabel());
			return convertView;
		}
	}
	
	class ViewHolder{
		public TextView tvLable;
		public ImageView ivIcon;
	}
	
	class MarketBean {
		private String mLabel;
		private String mPackageName;
		private Drawable mIcon;
		public String getmLabel() {
			return mLabel;
		}
		public void setmLabel(String mLabel) {
			this.mLabel = mLabel;
		}
		public String getmPackageName() {
			return mPackageName;
		}
		public void setmPackageName(String mPackageName) {
			this.mPackageName = mPackageName;
		}
		public Drawable getmIcon() {
			return mIcon;
		}
		public void setmIcon(Drawable mIcon) {
			this.mIcon = mIcon;
		}
	}
	
}
