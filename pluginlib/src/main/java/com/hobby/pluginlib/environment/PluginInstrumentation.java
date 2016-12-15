package com.hobby.pluginlib.environment;

import android.app.Activity;
import android.app.Fragment;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Window;

import com.hobby.pluginlib.Exception.PluginNotFoundException;
import com.hobby.pluginlib.PluginHelper;
import com.hobby.pluginlib.consts.PluginComponent;
import com.hobby.pluginlib.delegate.DelegateInstrumentation;
import com.hobby.pluginlib.dexloader.PluginDexLoader;
import com.hobby.pluginlib.reflect.Reflect;
import com.hobby.pluginlib.reflect.ReflectException;
import com.hobby.pluginlib.consts.Config;
import com.hobby.pluginlib.widget.LayoutInflaterWrapper;

import java.lang.reflect.Field;

/**
 * @author Lody
 * @version 1.0
 */
public class PluginInstrumentation extends DelegateInstrumentation {

    /**
     * 当前正在运行的插件
     */
    private PluginInfo currentPlugin;

    /**
     * @param mBase 真正的Instrumentation
     */
    public PluginInstrumentation(Instrumentation mBase) {
        super(mBase);
    }

    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        CreateActivityData activityData = (CreateActivityData) intent.getSerializableExtra(Config.FLAG_ACTIVITY_FROM_PLUGIN);
        //如果activityData存在,那么说明将要创建的是插件Activity
        if (activityData != null && PluginHelper.getInstance().getPlugins().size() > 0) {
            //这里找不到插件信息就会抛异常的,不用担心空指针
            PluginInfo plugInfo;
            try {
                Log.d(getClass().getSimpleName(), "+++ Start Plugin Activity => " + activityData.pluginPkg + " / " + activityData.activityName);
                plugInfo = PluginHelper.getInstance().tryGetPluginInfo(activityData.pluginPkg);
                plugInfo.ensureApplicationCreated();
            } catch (PluginNotFoundException e) {
                throw new IllegalAccessException("Cannot get plugin Info : " + activityData.pluginPkg);
            }
            if (activityData.activityName != null) {
                className = activityData.activityName;
                cl = plugInfo.getClassLoader();
            }
        }
        return super.newActivity(cl, className, intent);
    }


    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        lookupActivityInPlugin(activity);
        if (currentPlugin != null) {


            //初始化插件Activity
            Context baseContext = activity.getBaseContext();
            PluginContext pluginContext = new PluginContext(baseContext, currentPlugin);

            try {
                try {
                    //在许多设备上，Activity自身hold资源
                    Reflect.on(activity).set("mResources", pluginContext.getResources());
                    Reflect.on(activity).set("mTheme", pluginContext.getTheme());

                } catch (Throwable ignored) {
                }

                Field field = ContextWrapper.class.getDeclaredField("mBase");
                field.setAccessible(true);
                field.set(activity, pluginContext);
                try {
                    Reflect.on(activity).set("mApplication", currentPlugin.getApplication());
                } catch (ReflectException e) {
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

            ActivityInfo activityInfo = currentPlugin.findActivityByClassName(activity.getClass().getName());
            if (activityInfo != null) {
                //根据AndroidManifest.xml中的参数设置Theme
                int resTheme = activityInfo.getThemeResource();
                if (resTheme != 0) {
                    boolean hasNotSetTheme = true;
                    try {
                        Field mTheme = ContextThemeWrapper.class
                                .getDeclaredField("mTheme");
                        mTheme.setAccessible(true);
                        hasNotSetTheme = mTheme.get(activity) == null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (hasNotSetTheme) {
                        changeActivityInfo(activityInfo, activity);
                        activity.setTheme(resTheme);

                    }
                }
            }

            // 如果是三星手机，则使用包装的LayoutInflater替换原LayoutInflater
            // 这款手机在解析内置的布局文件时有各种错误
            if (android.os.Build.MODEL.startsWith("GT")) {
                Window window = activity.getWindow();
                Reflect windowRef = Reflect.on(window);
                try {
                    LayoutInflater originInflater = window.getLayoutInflater();
                    if (!(originInflater instanceof LayoutInflaterWrapper)) {
                        windowRef.set("mLayoutInflater", new LayoutInflaterWrapper(originInflater));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }


        super.callActivityOnCreate(activity, icicle);
    }

    @Override
    public void callActivityOnResume(Activity activity) {
        lookupActivityInPlugin(activity);
        super.callActivityOnResume(activity);
    }

    private static void changeActivityInfo(ActivityInfo activityInfo, Activity activity) {
        Field field_mActivityInfo;
        try {
            field_mActivityInfo = Activity.class.getDeclaredField("mActivityInfo");
            field_mActivityInfo.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        try {
            field_mActivityInfo.set(activity, activityInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void callActivityOnDestroy(Activity activity) {
        super.callActivityOnDestroy(activity);
    }

    /**
     * 检查跳转目标是不是来自插件
     *
     * @param activity Activity
     */
    private void lookupActivityInPlugin(Activity activity) {
        ClassLoader classLoader = activity.getClass().getClassLoader();
        if (classLoader instanceof PluginDexLoader) {
            currentPlugin = PluginHelper.getInstance().findPluginByDexLoader((PluginDexLoader) classLoader);
        } else {
            currentPlugin = null;
        }
    }

    private void replaceIntentTargetIfNeed(Context from, Intent intent) {
        ComponentName componentName = intent.getComponent();
        if (componentName != null) {
            String pkgName = componentName.getPackageName();
            String activityName = componentName.getClassName();

            String pluginPkg = PluginComponent.getPluginPkgName(activityName);
            if (!TextUtils.isEmpty(pluginPkg)) {
                currentPlugin = PluginHelper.getInstance().findPluginByPackageName(pluginPkg);
                CreateActivityData createActivityData = new CreateActivityData(activityName, currentPlugin.getPackageName());
                ActivityInfo activityInfo = currentPlugin.findActivityByClassName(activityName);
                if (activityInfo != null) {
                    intent.setClass(from, PluginHelper.getInstance().getActivitySelector().selectDynamicActivity(activityInfo));
                    intent.putExtra(Config.FLAG_ACTIVITY_FROM_PLUGIN, createActivityData);
                    intent.setExtrasClassLoader(currentPlugin.getClassLoader());
                }
            }
        }
    }

    @Override
    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Fragment fragment, Intent intent, int requestCode) {
        replaceIntentTargetIfNeed(who, intent);
        return super.execStartActivity(who, contextThread, token, fragment, intent, requestCode);
    }


    @Override
    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Fragment fragment, Intent intent, int requestCode, Bundle options) {
        replaceIntentTargetIfNeed(who, intent);
        return super.execStartActivity(who, contextThread, token, fragment, intent, requestCode, options);
    }

    @Override
    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target, Intent intent, int requestCode) {
        replaceIntentTargetIfNeed(who, intent);
        return super.execStartActivity(who, contextThread, token, target, intent, requestCode);
    }

    @Override
    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target, Intent intent, int requestCode, Bundle options) {
        replaceIntentTargetIfNeed(who, intent);
        return super.execStartActivity(who, contextThread, token, target, intent, requestCode, options);
    }
}
