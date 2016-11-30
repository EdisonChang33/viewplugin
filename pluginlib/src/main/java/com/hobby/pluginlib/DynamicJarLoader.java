package com.hobby.pluginlib;

import dalvik.system.DexClassLoader;

/**
 * Created by Chenyichang on 2016/11/29.
 */

enum DynamicJarLoader {

    INSTANCE;

    public DexClassLoader getDexClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
        return new DexClassLoader(dexPath, optimizedDirectory, libraryPath, parent);
    }
}
