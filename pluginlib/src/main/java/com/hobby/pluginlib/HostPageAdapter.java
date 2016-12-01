package com.hobby.pluginlib;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hobby.pluginlib.ui.BasePluginActivity;

public class HostPageAdapter extends FragmentPagerAdapter {

    private BasePluginActivity pluginHostActivity;
    private String[] fragClsNames;
    private String[] localPaths;

    public HostPageAdapter(BasePluginActivity pluginHostActivity, FragmentManager fm, String[] fragClsNames, String[] localPaths) {
        super(fm);
        this.pluginHostActivity = pluginHostActivity;
        this.fragClsNames = fragClsNames;
        this.localPaths = localPaths;
    }


    @Override
    public Fragment getItem(int index) {
        if (index < fragClsNames.length) {
            return getPluginFragment(localPaths[index], fragClsNames[index]);
        }

        return new Fragment();
    }

    @Override
    public int getCount() {
        return fragClsNames.length;
    }

    private Fragment getPluginFragment(String localPath, String fragClass) {
        try {
            Fragment fragment = pluginHostActivity.getFragment(localPath, fragClass);
            return fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Fragment();
    }
}
