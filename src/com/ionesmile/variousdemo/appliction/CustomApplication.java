package com.ionesmile.variousdemo.appliction;

import android.app.Application;

public class CustomApplication extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();

        //在这里为应用设置异常处理程序，然后我们的程序才能捕获未处理的异常
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

}
