package com.hobby.pluginlib.consts;

/**
 * Created by edisonChang on 2016/9/14.
 */
public class Config {

    public static final String SDK_APK_NEWS_PLUGIN = "newsplugin-debug.apk";
    public static final String SDK_APK_PLUGIN1 = "plugin1-debug.apk";
    public static final String SDK_APK_PLUGIN2 = "plugin2-debug.apk";
    public static final String SDK_APK_PLUGIN3 = "plugin3-debug.apk";

    public static final String SDK_JAR_DIR = "dex";

    public static final String[] SDK_APK_PLUGINS = {
            SDK_APK_PLUGIN1,
            SDK_APK_PLUGIN2,
            SDK_APK_PLUGIN3
    };


    /**
     * Activity来自插件的标志
     */
    public static final String FLAG_ACTIVITY_FROM_PLUGIN = "flag_act_fp";


    //////////////////////////////////////////////////////////////////////////////
    //////////////////////////  破壳系统 常量 //////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    public static final String GET_HOST_CONTEXT = "GetHostContext";

    public static final String GET_HOST_RESOURCE = "GetHostRes";

    public static final String GET_HOST_ASSETS = "GetHostAssets";

    public static final String GET_HOST_CLASS_LOADER = "GetHostClassLoader";

    public static final String GET_PLUGIN_PATH = "GetPluginPath";

    public static final String GET_PLUGIN_PKG_NAME = "GetPluginPkgName";

    public static final String GET_PLUGIN_PKG_INFO = "GetPluginPkgInfo";

}
