package com.hobby.pluginlib.loader;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hobby.pluginlib.ui.BasePluginActivity;

import java.io.InputStream;

/**
 * Created by Chenyichang on 2016/11/30.
 */

public class PluginResources {

    private String packageName;
    private Resources res;
    private AssetManager asset;

    PluginResources(String packageName, Resources res,
                    AssetManager asset) {
        this.packageName = packageName;
        this.res = res;
        this.asset = asset;
    }

    /**
     * 从当前类所在的包载入PluginResources
     *
     * @param clazz
     * @return
     * @throws RuntimeException 如果当前类不是动态加载包载入的
     */
    public static PluginResources getResource(Class<?> clazz) {
        if (!(clazz.getClassLoader() instanceof PluginDexLoader)) {
            throw new RuntimeException(clazz
                    + " is not loaded from dynamic loader");
        }
        return getResource((PluginDexLoader) clazz.getClassLoader());
    }

    static PluginResources getResource(PluginDexLoader dexLoader) {
        return null;
    }


    /**
     * Resources.getDrawable(id)
     */
    public Drawable getDrawable(int id) {
        return res.getDrawable(id);
    }

    /**
     * Resources.getText(id)
     */
    public CharSequence getText(int id) {
        return res.getText(id);
    }

    /**
     * Resources.getString(id)
     */
    public String getString(int id) {
        return res.getString(id);
    }

    /**
     * Resources.getStringArray(id)
     */
    public String[] getStringArray(int id) {
        return res.getStringArray(id);
    }

    /**
     * Resources.getColor(id)
     */
    public int getColor(int id) {
        return res.getColor(id);
    }

    /**
     * Resources.getColorStateList(id)
     */
    public ColorStateList getColorStateList(int id) {
        return res.getColorStateList(id);
    }

    /**
     * Resources.getDimension(id)
     */
    public float getDimension(int id) {
        return res.getDimension(id);
    }

    /**
     * Resources.getDimensionPixelSize(id)
     */
    public int getDimensionPixelSize(int id) {
        return res.getDimensionPixelSize(id);
    }

    /**
     * Resources.getDimensionPixelOffset(id)
     */
    public int getDimensionPixelOffset(int id) {
        return res.getDimensionPixelOffset(id);
    }

    /**
     * Resources.openRawResource(id)
     */
    public InputStream openRawResource(int id) {
        return res.openRawResource(id);
    }
}
