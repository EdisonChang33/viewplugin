package com.hobby.pluginlib.ui;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentHostCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hobby.pluginlib.PluginActivity;
import com.hobby.pluginlib.PluginContext;
import com.hobby.pluginlib.PluginHelper;
import com.hobby.pluginlib.PluginHostCallbacks;
import com.hobby.pluginlib.PluginInfo;
import com.hobby.pluginlib.loader.PluginResources;
import com.hobby.pluginlib.utils.ReflectUtils;
import com.hobby.pluginlib.utils.ToastUtils;

import java.lang.reflect.Field;

/**
 * Created by Chenyichang on 2016/11/29.
 */

public abstract class BasePluginFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    protected abstract int getLayoutId();

    private String localPath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onAttach(Context context) {
        PluginInfo pluginInfo = PluginHelper.getPlugin(localPath);
        if (pluginInfo != null) {
            Context pluginContext = new PluginContext(context, pluginInfo);
            Activity pluginActivity = new PluginActivity(context, pluginInfo);

            Object mFragmentHostCallbackHostValue = ReflectUtils.getFieldValue(this, "mHost");

            FragmentActivity oldActivity = (FragmentActivity) ReflectUtils.getFieldValue(mFragmentHostCallbackHostValue, "mActivity");
            Context oldContext = (Context) ReflectUtils.getFieldValue(mFragmentHostCallbackHostValue, "mContext");
            Handler oldHandler = (Handler) ReflectUtils.getFieldValue(mFragmentHostCallbackHostValue, "mHandler");
            int windowAnimations = (int) ReflectUtils.getFieldValue(mFragmentHostCallbackHostValue, "mWindowAnimations");

            PluginHostCallbacks hostCallbacks = new PluginHostCallbacks(oldActivity, oldContext, oldHandler, windowAnimations);

            ReflectUtils.setFieldValue(hostCallbacks, "mContext", pluginContext);
            ReflectUtils.setFieldValue(hostCallbacks, "mActivity", pluginActivity);
           // ReflectUtils.setFieldValue(hostCallbacks, "mHandler", oldActivity.mHandler);

            ReflectUtils.setFieldValue(this, "mHost", hostCallbacks);

           // ReflectUtils.setFieldValue(mFragmentHostCallbackHostValue, "mContext", pluginContext);
          //  ReflectUtils.setFieldValue(mFragmentHostCallbackHostValue, "mActivity", pluginActivity);



            super.onAttach(pluginContext);
        }


        super.onAttach(context);
        Log.d(TAG, "onAttach");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        PluginInfo pluginInfo = PluginHelper.getPlugin(localPath);
//
//        // Using PluginResources.inflate() instead of layoutinflater
//        if (pluginInfo != null) {
//            return pluginInfo.inflate(getActivity(), getLayoutId(), container, false);
//        }
        return LayoutInflater.from(getContext()).inflate(getLayoutId(), container, false);
    }

    /*protected Resources getPluginResources() {
        PluginInfo info = PluginHelper.getPlugin(localPath);
        if (info != null) {
            return info.resources;
        }
        return getResources();
    }*/

    //由外面传进来
    public void setPluginPath(String path) {
        localPath = path;
    }
}
