package com.hobby.pluginlib.dexloader;

import dalvik.system.DexClassLoader;

/**
 * Created by Chenyichang on 2016/11/29.
 */

public enum DynamicJarLoader {

    INSTANCE;

    public DexClassLoader getDexClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
        return new PluginDexLoader(dexPath, optimizedDirectory, libraryPath, parent);
    }
}
