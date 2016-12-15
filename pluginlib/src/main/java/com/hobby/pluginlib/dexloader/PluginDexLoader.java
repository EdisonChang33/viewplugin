package com.hobby.pluginlib.dexloader;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;

/**
 * Created by Chenyichang on 2016/11/30.
 */

public class PluginDexLoader extends DexClassLoader {

    private static List<PluginDexLoader> deps = new ArrayList<>();

    public PluginDexLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
    }

    public static void addDeps(PluginDexLoader dexLoader) {
        deps.add(dexLoader);
    }

    public static void removeDeps(PluginDexLoader dexLoader) {
        deps.remove(dexLoader);
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        Log.d("PluginDexLoader", "className = " + className + " resolve = " + resolve);
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
        if (deps != null && !deps.isEmpty()) {
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
    }


}
