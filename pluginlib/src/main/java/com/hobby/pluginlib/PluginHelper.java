package com.hobby.pluginlib;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.hobby.pluginlib.utils.Config;

import java.lang.reflect.Method;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

/**
 * Created by Chenyichang on 2016/11/29.
 */

public class PluginHelper {

    private static PluginHelper sInstance;

    private PluginHelper(Context context) {
        mContext = context.getApplicationContext();
    }

    public static PluginHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PluginHelper.class) {
                if (sInstance == null) {
                    sInstance = new PluginHelper(context);
                }
            }
        }
        return sInstance;
    }

    private Context mContext;
    public final static HashMap<String, PluginInfo> mPluginHolder = new HashMap<>();

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

        pluginInfo = new PluginInfo(apkPath, dexClassLoader, resources, assetManager, theme);
        mPluginHolder.put(apkPath, pluginInfo);
        return pluginInfo;
    }

    public static PluginInfo getPlugin(String apkPath) {
        if (!TextUtils.isEmpty(apkPath)) {
            return mPluginHolder.get(apkPath);
        }
        return null;
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

}
