package com.hobby.pluginlib;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HostPageAdapter extends FragmentPagerAdapter {

    private IPluginTricker pluginTricker;
    private String[] fragClsNames;

    public HostPageAdapter(FragmentManager fm, String[] fragClsNames, IPluginTricker pluginTricker) {
        super(fm);
        this.pluginTricker = pluginTricker;
        this.fragClsNames = fragClsNames;
    }


    @Override
    public Fragment getItem(int index) {
        if (index < fragClsNames.length) {
            return getPluginFragment(index, fragClsNames[index], new Bundle());
        }

        return new Fragment();
    }

    @Override
    public int getCount() {
        return fragClsNames.length;
    }

    private Fragment getPluginFragment(int index, String fragClass, Bundle bundle) {
        try {
            PluginInfo info = pluginTricker.getPluginInfo(index);
            if (info != null) {


            }

            ClassLoader classLoader = pluginTricker.getClassLoader(index);
            Fragment fragment = (Fragment) classLoader.loadClass(fragClass).newInstance();
            fragment.setArguments(bundle);



            return fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
