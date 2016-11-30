package com.hobby.pluginlib;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.text.TextUtils;

import com.hobby.pluginlib.base.IntentConst;
import com.hobby.pluginlib.inflater.PluginInflaterFactory;
import com.hobby.pluginlib.ui.BaseActivity;

/**
 * Created by Chenyichang on 2016/11/29.
 */

public class PluginHostActivity extends BaseActivity {

    private String localPath;
    private String fragmentCls;

    private boolean isInstall;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new PluginInflaterFactory());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        localPath = getIntent().getStringExtra(IntentConst.INTENT_KEY_APK_PATH);
        fragmentCls = getIntent().getStringExtra(IntentConst.INTENT_KEY_FRAGMENT);

        if (!TextUtils.isEmpty(localPath)) {
            installPlugin(localPath);
            installFragment(fragmentCls);
        }
    }

    protected PluginInfo getPluginInfo() {
        if (TextUtils.isEmpty(localPath)) {
            return null;
        }
        return PluginHelper.getPlugin(localPath);
    }

    /**
     * 装载插件的运行环境,这个函数需要在Activity中运行，不能移动到Application中去
     */
    private void installPlugin(final String localPath) {
        PluginHelper.getInstance(this).install(localPath);
        isInstall = true;
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

    protected void installFragment(String fragClass) {
        try {
            if (isFinishing()) {
                return;
            }
            ClassLoader classLoader = getClassLoader();
            Fragment fragment = (Fragment) classLoader.loadClass(fragClass).newInstance();
            Bundle bundle = getIntent().getExtras();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, fragment).commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
