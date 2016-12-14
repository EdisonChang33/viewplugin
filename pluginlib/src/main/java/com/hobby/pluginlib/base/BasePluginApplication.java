package com.hobby.pluginlib.base;

import android.app.Application;
import android.content.Context;

import com.hobby.pluginlib.PluginHelper;

/**
 * Created by Chenyichang on 2016/11/30.
 */

public class BasePluginApplication extends Application{

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PluginHelper.init(this);
    }
}
