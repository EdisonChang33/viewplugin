package com.hobby.pluginlib.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * Created by Chenyichang on 2016/11/29.
 */

public class ApkUtils {

    //https://code.google.com/p/android/issues/detail?id=9151
    public static Drawable getApplicationIcon(Context context, String apkFilepath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = getPackageInfo(context, apkFilepath);
        if (pkgInfo == null) {
            return null;
        }

        ApplicationInfo appInfo = pkgInfo.applicationInfo;
        appInfo.sourceDir = apkFilepath;
        appInfo.publicSourceDir = apkFilepath;

        return pm.getApplicationIcon(appInfo);
    }

    public static CharSequence getApplicationLabel(Context context, String apkFilepath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = getPackageInfo(context, apkFilepath);
        if (pkgInfo == null) {
            return null;
        }

        ApplicationInfo appInfo = pkgInfo.applicationInfo;
        appInfo.sourceDir = apkFilepath;
        appInfo.publicSourceDir = apkFilepath;

        return pm.getApplicationLabel(appInfo);
    }

    public static PackageInfo getPackageInfo(Context context, String apkFilepath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageArchiveInfo(apkFilepath,
                    PackageManager.GET_ACTIVITIES |
                            PackageManager.GET_RECEIVERS |
                            PackageManager.GET_SERVICES |
                            PackageManager.GET_PROVIDERS |
                            PackageManager.GET_META_DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pkgInfo;
    }


    public static String getRootFragment(Context context, String apkFilepath) {
        PackageInfo info = getPackageInfo(context, apkFilepath);
        ApplicationInfo appInfo = info.applicationInfo;
        String root = appInfo.metaData.getString("root_fragment");
        return root;
    }
}
