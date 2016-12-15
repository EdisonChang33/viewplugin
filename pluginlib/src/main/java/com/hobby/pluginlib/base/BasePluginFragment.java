package com.hobby.pluginlib.base;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hobby.pluginlib.PluginHelper;
import com.hobby.pluginlib.environment.PluginHostCallbacks;
import com.hobby.pluginlib.environment.PluginInfo;
import com.hobby.pluginlib.environment.PluginActivity;
import com.hobby.pluginlib.environment.PluginContext;
import com.hobby.pluginlib.utils.ReflectUtils;

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
        return LayoutInflater.from(getContext()).inflate(getLayoutId(), container, false);
    }

    //由外面传进来
    public void setPluginPath(String path) {
        localPath = path;
    }
}
