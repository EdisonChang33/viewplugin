package com.hobby.pluginlib;

import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * Created by Chenyichang on 2016/11/29.
 */

public class PluginInfo {

    public ClassLoader classLoader;
    public Resources resources;
    public AssetManager assetManager;
    public Resources.Theme theme;
    public String localPath;

    public PluginInfo(String localPath, ClassLoader classLoader, Resources resources, AssetManager assetManager, Resources.Theme theme) {
        this.classLoader = classLoader;
        this.resources = resources;
        this.assetManager = assetManager;
        this.theme = theme;
        this.localPath = localPath;
    }
}
