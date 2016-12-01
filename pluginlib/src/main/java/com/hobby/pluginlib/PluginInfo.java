package com.hobby.pluginlib;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hobby.pluginlib.loader.PluginDexLoader;
import com.hobby.pluginlib.loader.PluginResources;
import com.hobby.pluginlib.ui.BasePluginActivity;
import com.hobby.pluginlib.ui.BasePluginFragment;

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

    /**
     * 同LayoutInflater.inflate(id, parent, attachToRoot)
     * <p>
     * 不会处理依赖关系，请确保id对应的layout在当前包内
     *
     * @return
     * @throws Resources.NotFoundException
     */
    public View inflate(Context context, int id, ViewGroup parent,
                        boolean attachToRoot) {
        if (!(context instanceof BasePluginActivity)) {
            throw new RuntimeException(
                    "unable to inflate without BasePluginActivity context");
        }
        BasePluginActivity ma = (BasePluginActivity) context;
        PluginInfo old = ma.getOverrideResources();
        ma.setOverrideResources(this);
        try {
            View v = LayoutInflater.from(context).inflate(id, null,
                    attachToRoot);
            return v;
        } finally {
            ma.setOverrideResources(old);
        }
    }
}
