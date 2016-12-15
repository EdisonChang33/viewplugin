package com.hobby.pluginlib.environment;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hobby.pluginlib.R;
import com.hobby.pluginlib.base.BasePluginActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chenyichang on 2016/11/29.
 */

public class PluginInfo {

    private String filePath;
    private PackageInfo packageInfo;
    private Map<String, ResolveInfo> activities;
    private ResolveInfo mainActivity;
    private List<ResolveInfo> services;
    private List<ResolveInfo> receivers;
    private List<ResolveInfo> providers;

    private transient ClassLoader classLoader;
    private transient Application application;
    private transient AssetManager assetManager;
    private transient Resources resources;
    private transient boolean isApplicationOnCreated;
    private Resources.Theme theme;

    public String getPackageName() {
        return packageInfo.packageName;
    }

    public ActivityInfo findActivityByClassNameFromPkg(String actName) {
        if (actName.startsWith(".")) {
            actName = getPackageName() + actName;
        }
        if (packageInfo.activities == null) {
            return null;
        }
        for (ActivityInfo act : packageInfo.activities) {
            if (act.name.equals(actName)) {
                return act;
            }
        }
        return null;
    }

    public ActivityInfo findActivityByClassName(String actName) {
        if (packageInfo.activities == null) {
            return null;
        }
        if (actName.startsWith(".")) {
            actName = getPackageName() + actName;
        }
        ResolveInfo act = activities.get(actName);
        if (act == null) {
            return null;
        }
        return act.activityInfo;
    }

    public ActivityInfo findActivityByAction(String action) {
        if (activities == null || activities.isEmpty()) {
            return null;
        }

        for (ResolveInfo act : activities.values()) {
            if (act.filter != null && act.filter.hasAction(action)) {
                return act.activityInfo;
            }
        }
        return null;
    }

    public ActivityInfo findReceiverByClassName(String className) {
        if (packageInfo.receivers == null) {
            return null;
        }
        for (ActivityInfo receiver : packageInfo.receivers) {
            if (receiver.name.equals(className)) {
                return receiver;
            }
        }
        return null;

    }

    public ServiceInfo findServiceByClassName(String className) {
        if (packageInfo.services == null) {
            return null;
        }
        for (ServiceInfo service : packageInfo.services) {
            if (service.name.equals(className)) {
                return service;
            }
        }
        return null;

    }

    public ServiceInfo findServiceByAction(String action) {
        if (services == null || services.isEmpty()) {
            return null;
        }
        for (ResolveInfo ser : services) {
            if (ser.filter != null && ser.filter.hasAction(action)) {
                return ser.serviceInfo;
            }
        }
        return null;
    }

    public void addActivity(ResolveInfo activity) {
        if (activities == null) {
            activities = new HashMap<String, ResolveInfo>(15);
        }
        fixActivityInfo(activity.activityInfo);
        activities.put(activity.activityInfo.name, activity);
        if (mainActivity == null && activity.filter != null
                && activity.filter.hasAction("android.intent.action.MAIN")
                && activity.filter.hasCategory("android.intent.category.LAUNCHER")
                ) {
            mainActivity = activity;
        }
    }

    private void fixActivityInfo(ActivityInfo activityInfo) {
        if (activityInfo != null) {
            if (activityInfo.name.startsWith(".")) {
                activityInfo.name = getPackageName() + activityInfo.name;
            }
        }
    }

    public void addReceiver(ResolveInfo receiver) {
        if (receivers == null) {
            receivers = new ArrayList<ResolveInfo>();
        }
        receivers.add(receiver);
    }

    public void addService(ResolveInfo service) {
        if (services == null) {
            services = new ArrayList<ResolveInfo>();
        }
        services.add(service);
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
        activities = new HashMap<String, ResolveInfo>(packageInfo.activities.length);
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public AssetManager getAssets() {
        return assetManager;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public void setTheme(int themeId) {
        Resources.Theme theme = resources.newTheme();
        theme.applyStyle(themeId, true);
        setTheme(theme);
    }

    public void setTheme(Resources.Theme theme) {
        this.theme = theme;
    }

    public Resources.Theme getTheme() {
        return theme;
    }

    public void resetHostTheme() {
        Resources.Theme theme = resources.newTheme();
        theme.applyStyle(R.style.AppTheme, false);
        setTheme(theme);
    }

    public Collection<ResolveInfo> getActivities() {
        if (activities == null) {
            return null;
        }
        return activities.values();
    }

    public List<ResolveInfo> getServices() {
        return services;
    }

    public void setServices(List<ResolveInfo> services) {
        this.services = services;
    }

    public List<ResolveInfo> getProviders() {
        return providers;
    }

    public void setProviders(List<ResolveInfo> providers) {
        this.providers = providers;
    }

    public ResolveInfo getMainActivity() {
        return mainActivity;
    }

    public List<ResolveInfo> getReceivers() {
        return receivers;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PluginInfo other = (PluginInfo) obj;
        if (filePath == null) {
            if (other.filePath != null)
                return false;
        } else if (!filePath.equals(other.filePath))
            return false;
        return true;
    }

    public boolean isApplicationCreated() {
        //Fix at 2016/3/16
        return application != null || isApplicationOnCreated;
    }

    public void ensureApplicationCreated() {
        if (isApplicationCreated()) {
            synchronized (this) {
                try {
                    application.onCreate();
                    if (receivers != null && receivers.size() > 0) {
                        for (ResolveInfo resolveInfo : receivers) {
                            if (resolveInfo.activityInfo != null) {
                                try {
                                    BroadcastReceiver broadcastReceiver = (BroadcastReceiver) classLoader.loadClass(resolveInfo.activityInfo.name).newInstance();
                                    application.registerReceiver(broadcastReceiver, resolveInfo.filter);
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (Throwable ignored) {
                }
                isApplicationOnCreated = true;
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() + "[ filePath=" + filePath + ", pkg=" + getPackageName()
                + " ]";
    }

}
