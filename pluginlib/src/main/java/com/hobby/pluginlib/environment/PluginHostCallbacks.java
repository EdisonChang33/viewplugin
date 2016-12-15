package com.hobby.pluginlib.environment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentHostCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.hobby.pluginlib.utils.ReflectUtils;

import java.io.FileDescriptor;
import java.io.PrintWriter;

/**
 * Created by Chenyichang on 2016/12/6.
 */

public class PluginHostCallbacks extends FragmentHostCallback<FragmentActivity> {

    private FragmentActivity fragmentActivity;

    public PluginHostCallbacks(Activity activity, Context context, Handler handler, int windowAnimations) {
        super(context, handler, windowAnimations);
        fragmentActivity = (FragmentActivity) activity;
    }

/*    public PluginHostCallbacks(FragmentActivity activity) {
        super(activity *//*fragmentActivity*//*);
    }*/

    @Override
    public void onDump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        fragmentActivity.dump(prefix, fd, writer, args);
    }

    @Override
    public boolean onShouldSaveFragmentState(Fragment fragment) {
        return !fragmentActivity.isFinishing();
    }

    @Override
    public LayoutInflater onGetLayoutInflater() {
        return fragmentActivity.getLayoutInflater().cloneInContext(fragmentActivity);
    }

    @Override
    public FragmentActivity onGetHost() {
        return fragmentActivity;
    }

    @Override
    public void onSupportInvalidateOptionsMenu() {
        fragmentActivity.supportInvalidateOptionsMenu();
    }

    @Override
    public void onStartActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        fragmentActivity.startActivityFromFragment(fragment, intent, requestCode);
    }

    @Override
    public void onStartActivityFromFragment(
            Fragment fragment, Intent intent, int requestCode, @Nullable Bundle options) {
        fragmentActivity.startActivityFromFragment(fragment, intent, requestCode, options);
    }

    @Override
    public void onRequestPermissionsFromFragment(@NonNull Fragment fragment,
                                                 @NonNull String[] permissions, int requestCode) {

        ReflectUtils.invokeMethod(FragmentActivity.class, fragmentActivity, "onRequestPermissionsFromFragment",
                new Class[]{Fragment.class, String[].class, Integer.class}, fragment, permissions, requestCode);
//        fragmentActivity.requestPermissionsFromFragment(fragment, permissions,
//                requestCode);
    }

    @Override
    public boolean onShouldShowRequestPermissionRationale(@NonNull String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(
                fragmentActivity, permission);
    }

    @Override
    public boolean onHasWindowAnimations() {
        return fragmentActivity.getWindow() != null;
    }

    @Override
    public int onGetWindowAnimations() {
        final Window w = fragmentActivity.getWindow();
        return (w == null) ? 0 : w.getAttributes().windowAnimations;
    }

/*    @Override
    public void onAttachFragment(Fragment fragment) {
        fragmentActivity.onAttachFragment(fragment);
    }*/

    @Nullable
    @Override
    public View onFindViewById(int id) {
        return fragmentActivity.findViewById(id);
    }

    @Override
    public boolean onHasView() {
        final Window w = fragmentActivity.getWindow();
        return (w != null && w.peekDecorView() != null);
    }
}
