package com.hobby.pluginlib.loader;

import dalvik.system.DexClassLoader;

/**
 * Created by Chenyichang on 2016/11/30.
 */

public class PluginDexLoader extends DexClassLoader {

    private PluginDexLoader[] deps;

    public PluginDexLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
    }

   /* @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = findLoadedClass(className);
        if (clazz != null)
            return clazz;
        try {
            clazz = getParent().loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (clazz != null)
            return clazz;
        if (deps != null) {
            for (PluginDexLoader c : deps) {
                try {
                    clazz = c.findClass(className);
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (clazz != null)
            return clazz;
        clazz = findClass(className);
        return clazz;
    }*/


}
