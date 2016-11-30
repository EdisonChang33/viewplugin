package com.hobby.pluginlib;

/**
 * Created by Chenyichang on 2016/11/30.
 */

public interface IPluginTricker {

    ClassLoader getClassLoader(int pos);
    PluginInfo getPluginInfo(int pos);
}
