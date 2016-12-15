package com.hobby.pluginlib.environment;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * Created by Chenyichang on 2016/12/5.
 */

public class PluginActivity extends Activity{

    private PluginInfo plugInfo;
    private Context hostContext;

    public PluginActivity(Context hostContext, PluginInfo plugInfo) {
        super();
        if (plugInfo == null) {
            throw new IllegalStateException("Create a plugin context, but not given host context!");
        }
        this.plugInfo = plugInfo;
    }

    @Override
    public Resources getResources() {
        return plugInfo.getResources();
    }

    @Override
    public AssetManager getAssets() {
        return plugInfo.getAssets();
    }

    @Override
    public ClassLoader getClassLoader() {
        return plugInfo.getClassLoader();
    }

    @Override
    public Resources.Theme getTheme() {
        return plugInfo != null ? plugInfo.getTheme() : super.getTheme();
    }
}
