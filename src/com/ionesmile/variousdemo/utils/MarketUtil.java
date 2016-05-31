package com.ionesmile.variousdemo.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

public class MarketUtil {

	/**
	 * create market Intent
	 * @param packageName	need to open app
	 * @return
	 */
	public static Intent getIntent(String packageName) {
		StringBuilder localStringBuilder = new StringBuilder().append("market://details?id=");
		localStringBuilder.append(packageName);
		Uri localUri = Uri.parse(localStringBuilder.toString());
		return new Intent("android.intent.action.VIEW", localUri);
	}

	/**
	 * queryIntentActivties by  paramIntent
	 * @param context
	 * @param paramIntent
	 * @param validPackageNames    is null return intent Activity, else return accord Activity
	 * @return
	 */
	public static List<ResolveInfo> getResolveInfos(Context context, Intent paramIntent, String[] validPackageNames) {
		List<ResolveInfo> localList = context.getPackageManager().queryIntentActivities(paramIntent, PackageManager.GET_INTENT_FILTERS);
		// when validPackageNames is null, return all intent activity.
		if (validPackageNames != null) {
			// else return accord Activity
			List<ResolveInfo> newList = new ArrayList<ResolveInfo>(validPackageNames.length);
			for(ResolveInfo item : localList){
				String itemName = item.activityInfo.packageName;
				Log.i("myTag", "packageName = " + itemName + "    Label = " + item.loadLabel(context.getPackageManager()).toString());
				if (itemName == null) continue;
				for (int i = 0; i < validPackageNames.length; i++) {
					if (itemName.equals(validPackageNames[i])) {
						newList.add(item);
						break;
					}
				}
			}
			return newList;
		}
		return localList;
	}
}
