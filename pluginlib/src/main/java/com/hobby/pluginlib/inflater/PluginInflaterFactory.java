package com.hobby.pluginlib.inflater;

import android.content.Context;
import android.support.v4.view.LayoutInflaterFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Chenyichang on 2016/10/28.
 */

public class PluginInflaterFactory implements LayoutInflaterFactory {

    private static final String TAG = "PluginInflaterFactory";

    private static final String[] VIEWS_CLS_PREFIX = {
            "android.view.",
            "android.widget.",
            "android.webkit."
    };

    private static final Set<String> namesVisited = new HashSet<>();
    private static final Map<String, Constructor<? extends View>> constructorMap = new HashMap<>();
    private static final Class<?>[] constructorSignature = new Class[]{
            Context.class, AttributeSet.class};

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return createViewFromTag(name, context, attrs);
    }

    private View createViewFromTag(String name, Context context, AttributeSet attrs) {
        View result;
        result = createViewImp1(name, context, attrs);
        if (result == null) {
            result = createViewImp2(name, context, attrs);
        }
        return result;
    }

    private View createViewImp1(String name, Context context, AttributeSet attrs) {
        View result = null;

        if (!namesVisited.contains(name)) {
            namesVisited.add(name);
            if (name.contains(".")) {
                Constructor<? extends View> constructor = getConstructor(name, context);
                if (constructor != null) {
                    constructorMap.put(name, constructor);
                }
            } else {
                for (String prefix : VIEWS_CLS_PREFIX) {
                    Constructor<? extends View> constructor = getConstructor(name, context);
                    if (constructor != null) {
                        constructorMap.put(prefix + name, constructor);
                        break;
                    }
                }
            }
        }

        Constructor<? extends View> constructor = constructorMap.get(name);
        if (constructor != null) {
            try {
                constructor.setAccessible(true);
                result = constructor.newInstance(context, attrs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private View createViewImp2(String name, Context context, AttributeSet attrs) {
        View view = null;
        try {
            if (name.contains(".")) {
                view = LayoutInflater.from(context).createView(name, null, attrs);
            } else {
                if ("View".equals(name)) {
                    view = LayoutInflater.from(context).createView(name, "android.view.", attrs);
                }
                if (view == null) {
                    for (String prefix : VIEWS_CLS_PREFIX) {
                        try {
                            view = LayoutInflater.from(context).createView(name, prefix, attrs);
                            if (view != null) {
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    private Constructor<? extends View> getConstructor(String name, Context context) {
        try {
            Class<? extends View> clazz = context.getClassLoader()
                    .loadClass(name).asSubclass(View.class);
            try {
                return clazz.getConstructor(constructorSignature);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
