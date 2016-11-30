package com.hobby.pluginlib;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.hobby.pluginlib.base.IntentConst;
import com.hobby.pluginlib.inflater.PluginInflaterFactory;
import com.hobby.pluginlib.ui.BaseActivity;

/**
 * Created by Chenyichang on 2016/11/29.
 */

public class PluginHostMultiTabActivity extends BaseActivity implements IPluginTricker {

    private String localPath[];
    private String fragmentCls[];

    private boolean isInstall;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new PluginInflaterFactory());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        viewPager = (ViewPager) findViewById(R.id.content);

        localPath = getIntent().getStringArrayExtra(IntentConst.INTENT_KEY_APK_PATHS);
        fragmentCls = getIntent().getStringArrayExtra(IntentConst.INTENT_KEY_FRAGMENTS);

        if (localPath != null && localPath.length > 0) {
            installPlugin();
            initViewPager();
        }
    }

    protected PluginInfo getPluginInfo() {
        String path = getPluginPath(0);
        return getPluginInfo(path);
    }

    protected PluginInfo getPluginInfo(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return PluginHelper.getPlugin(path);
    }

    /**
     * 装载插件的运行环境,这个函数需要在Activity中运行，不能移动到Application中去
     */
    private void installPlugin() {
        for (String path : localPath) {
            PluginHelper.getInstance(this).install(path);
        }
        isInstall = true;
    }

    protected String getPluginPath(int pos) {
        return localPath[pos];
    }

    @Override
    public AssetManager getAssets() {
        PluginInfo info = getPluginInfo();
        if (isInstall && info != null) {
            return info.assetManager;
        } else {
            return super.getAssets();
        }
    }

    @Override
    public Resources getResources() {
        PluginInfo info = getPluginInfo();
        if (isInstall && info != null) {
            return info.resources;
        } else {
            return super.getResources();
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        PluginInfo info = getPluginInfo();
        if (isInstall && info != null) {
            return info.classLoader;
        } else {
            return super.getClassLoader();
        }
    }

    @Override
    public Resources.Theme getTheme() {
        PluginInfo info = getPluginInfo();
        if (isInstall && info != null) {
            return info.theme;
        } else {
            return super.getTheme();
        }
    }

    private void initViewPager() {
        FragmentPagerAdapter adapter = new HostPageAdapter(getSupportFragmentManager(), fragmentCls, this);
        viewPager.setAdapter(adapter);
    }

    @Override
    public ClassLoader getClassLoader(int pos) {
        PluginInfo info = getPluginInfo(getPluginPath(pos));
        if (isInstall && info != null) {
            return info.classLoader;
        } else {
            return super.getClassLoader();
        }
    }

    @Override
    public PluginInfo getPluginInfo(int pos) {
        return getPluginInfo(getPluginPath(pos));
    }
}
