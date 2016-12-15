package com.hobby.pluginlib;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.hobby.pluginlib.Exception.PluginNotFoundException;
import com.hobby.pluginlib.consts.PluginComponent;
import com.hobby.pluginlib.delegate.DelegateActivityThread;
import com.hobby.pluginlib.environment.PluginContext;
import com.hobby.pluginlib.dexloader.DynamicJarLoader;
import com.hobby.pluginlib.environment.PluginInfo;
import com.hobby.pluginlib.environment.PluginInstrumentation;
import com.hobby.pluginlib.selector.DefaultActivitySelector;
import com.hobby.pluginlib.selector.DynamicActivitySelector;
import com.hobby.pluginlib.consts.Config;
import com.hobby.pluginlib.utils.PluginManifestUtil;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

/**
 * Created by Chenyichang on 2016/11/29.
 */

public class PluginHelper {

    private static PluginHelper sInstance;

    private PluginHelper(Context context) {
        mContext = context;
        PluginComponent.init();
        DelegateActivityThread delegateActivityThread = DelegateActivityThread.getSingleton();
        Instrumentation originInstrumentation = delegateActivityThread.getInstrumentation();
        if (!(originInstrumentation instanceof PluginInstrumentation)) {
            PluginInstrumentation pluginInstrumentation = new PluginInstrumentation(originInstrumentation);
            delegateActivityThread.setInstrumentation(pluginInstrumentation);
        }
    }

    public static PluginHelper getInstance() {
        return sInstance;
    }

    public static void init(Context context) {
        if (sInstance != null) {
            return;
        }
        sInstance = new PluginHelper(context);
    }

    private Context mContext;
    public final Map<String, PluginInfo> mPluginHolder = new HashMap<>();
    public final Map<DexClassLoader, PluginInfo> mDexPluginHolder = new HashMap<>();
    public final Map<String, PluginInfo> mPkgPluginHolder = new HashMap<>();

    public PluginInfo install(String apkPath) {
        PluginInfo pluginInfo = mPluginHolder.get(apkPath);
        if (pluginInfo != null) {
            return pluginInfo;
        }

        DexClassLoader dexClassLoader = createDexClassLoader(apkPath);
        AssetManager assetManager = createAssetManager(apkPath);
        Resources resources = createResources(assetManager);
        Resources.Theme theme = resources.newTheme();
        theme.applyStyle(R.style.AppTheme, false);

        pluginInfo = new PluginInfo(/*apkPath, dexClassLoader, resources, assetManager, theme*/);
        pluginInfo.setFilePath(apkPath);
        pluginInfo.setAssetManager(assetManager);
        pluginInfo.setClassLoader(dexClassLoader);
        pluginInfo.setResources(resources);
        pluginInfo.setTheme(theme);

        try {
            PluginManifestUtil.setManifestInfo(mContext, apkPath, pluginInfo);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ApplicationInfo appInfo = pluginInfo.getPackageInfo().applicationInfo;
        Application app = makeApplication(pluginInfo, appInfo);
        attachBaseContext(pluginInfo, app);
        pluginInfo.setApplication(app);

        mPluginHolder.put(apkPath, pluginInfo);
        mDexPluginHolder.put(dexClassLoader, pluginInfo);
        mPkgPluginHolder.put(pluginInfo.getPackageName(), pluginInfo);
        return pluginInfo;
    }


    public static PluginInfo getPlugin(String apkPath) {
        if (!TextUtils.isEmpty(apkPath) && PluginHelper.getInstance() != null) {
            return PluginHelper.getInstance().mPluginHolder.get(apkPath);
        }
        return null;
    }

    /**
     * 构造插件的Application
     *
     * @param pluginInfo 插件信息
     * @param appInfo    插件ApplicationInfo
     * @return 插件App
     */
    private Application makeApplication(PluginInfo pluginInfo, ApplicationInfo appInfo) {
        String appClassName = appInfo.className;
        if (appClassName == null) {
            //Default Application
            appClassName = Application.class.getName();
        }
        try {
            return (Application) pluginInfo.getClassLoader().loadClass(appClassName).newInstance();
        } catch (Throwable e) {
            throw new RuntimeException("Unable to create Application for "
                    + pluginInfo.getPackageName() + ": "
                    + e.getMessage());
        }
    }

    private void attachBaseContext(PluginInfo info, Application app) {
        try {
            Field mBase = ContextWrapper.class.getDeclaredField("mBase");
            mBase.setAccessible(true);
            mBase.set(app, new PluginContext(mContext.getApplicationContext(), info));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建插件classloader
     *
     * @param dexPath
     * @return
     */
    private DexClassLoader createDexClassLoader(String dexPath) {
        DexClassLoader loader = DynamicJarLoader.INSTANCE.getDexClassLoader(dexPath,
                mContext.getDir(Config.SDK_JAR_DIR, Context.MODE_PRIVATE).getAbsolutePath(),
                null,
                mContext.getClassLoader());

        return loader;
    }

    /**
     * 创建AssetManager对象
     *
     * @param dexPath
     * @return
     */
    private AssetManager createAssetManager(String dexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            return assetManager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建Resource对象
     *
     * @param assetManager
     * @return
     */
    private Resources createResources(AssetManager assetManager) {
        Resources superRes = mContext.getResources();
        Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        return resources;
    }

    private Class<?> loadPluginClass(ClassLoader classLoader, String className) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className, true, classLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public File getPluginLibPath(PluginInfo info) {
        return null;
    }

    public Map<String, PluginInfo> getPlugins() {
        return mPluginHolder;
    }

    public PluginInfo tryGetPluginInfo(String pluginPkg) throws PluginNotFoundException {

        PluginInfo plug = findPluginByPackageName(pluginPkg);
        if (plug == null) {
            throw new PluginNotFoundException("plug not found by:"
                    + pluginPkg);
        }
        return plug;
    }

    /**
     * 指定插件包名取得插件信息
     *
     * @param packageName 插件包名
     * @return 插件信息
     */
    public PluginInfo findPluginByPackageName(String packageName) {
        return mPkgPluginHolder.get(packageName);
    }

    public PluginInfo findPluginByDexLoader(DexClassLoader dexClassLoader) {
        return mDexPluginHolder.get(dexClassLoader);
    }

    public DynamicActivitySelector getActivitySelector() {
        return DefaultActivitySelector.getDefault();
    }
}
