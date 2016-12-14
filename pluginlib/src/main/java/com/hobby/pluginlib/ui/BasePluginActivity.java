package com.hobby.pluginlib.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.hobby.pluginlib.PluginHelper;
import com.hobby.pluginlib.PluginInfo;
import com.hobby.pluginlib.utils.IntentConst;
import com.hobby.pluginlib.utils.ReflectUtils;

/**
 * Created by Chenyichang on 2016/11/29.
 */

public abstract class BasePluginActivity extends AppCompatActivity {

    private PluginInfo pluginInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void startFragment(String fragment, String apkPath, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(IntentConst.ACTION_HOST_ACTIVITY);
        intent.putExtra(IntentConst.INTENT_KEY_APK_PATH, apkPath);
        intent.putExtra(IntentConst.INTENT_KEY_FRAGMENT, fragment);
        intent.putExtra(IntentConst.INTENT_KEY_BUNDLE, bundle);
        startActivity(intent);
    }

    protected void startMultiFragment(String[] fragments, String[] apkPaths, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(IntentConst.ACTION_MULTI_TAB_HOST_ACTIVITY);
        intent.putExtra(IntentConst.INTENT_KEY_APK_PATHS, apkPaths);
        intent.putExtra(IntentConst.INTENT_KEY_FRAGMENTS, fragments);
        intent.putExtra(IntentConst.INTENT_KEY_BUNDLE, bundle);
        startActivity(intent);
    }

    @Override
    public AssetManager getAssets() {
        return pluginInfo != null ? pluginInfo.assetManager : super.getAssets();
    }

    @Override
    public Resources getResources() {
        return pluginInfo != null ? pluginInfo.resources : super.getResources();
    }

    @Override
    public Resources.Theme getTheme() {
        return pluginInfo != null ? pluginInfo.theme : super.getTheme();
    }

    @Override
    public ClassLoader getClassLoader() {
        return pluginInfo != null ? pluginInfo.classLoader :super.getClassLoader();
    }

    public ClassLoader getClassLoader(String path) {
        PluginInfo pluginInfo = PluginHelper.getPlugin(path);
        if (pluginInfo != null) {
            return pluginInfo.classLoader;
        }
        return super.getClassLoader();
    }

    public Fragment getFragment(String localPath, String fragmentCls) throws Exception {
        ClassLoader classLoader = getClassLoader(localPath);
        Class<?> cls = classLoader.loadClass(fragmentCls);
        Fragment fragment = (Fragment) cls.newInstance();
        ReflectUtils.invokeMethod(cls, fragment, "setPluginPath", new Class[]{String.class}, localPath);
        Bundle bundle = getIntent().getExtras();
        fragment.setArguments(bundle);
        return fragment;
    }

    public PluginInfo getOverrideResources() {
        return pluginInfo;
    }

    public void setOverrideResources(PluginInfo pluginInfo) {
        if (pluginInfo != null) {
            this.pluginInfo = pluginInfo;
        }
    }
}
