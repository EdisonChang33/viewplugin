package com.hobby.pluginlib.ui;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hobby.pluginlib.PluginHelper;
import com.hobby.pluginlib.PluginInfo;
import com.hobby.pluginlib.loader.PluginResources;
import com.hobby.pluginlib.utils.ToastUtils;

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
        super.onAttach(context);
        Log.d(TAG, "onAttach");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        PluginInfo pluginInfo = PluginHelper.getPlugin(localPath);

        // Using PluginResources.inflate() instead of layoutinflater
        if (pluginInfo != null) {
            return pluginInfo.inflate(getActivity(), getLayoutId(), container, false);
        }
        return LayoutInflater.from(getContext()).inflate(getLayoutId(), container, false);
    }

    protected Resources getPluginResources() {
        PluginInfo info = PluginHelper.getPlugin(localPath);
        if (info != null) {
            return info.resources;
        }
        return getResources();
    }

    //由外面传进来
    public void setPluginPath(String path) {
        localPath = path;
    }
}
