package com.hobby.pluginlib;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.hobby.pluginlib.utils.IntentConst;
import com.hobby.pluginlib.inflater.PluginInflaterFactory;
import com.hobby.pluginlib.ui.BasePluginActivity;

/**
 * Created by Chenyichang on 2016/11/29.
 */

public class PluginHostActivity extends BasePluginActivity {

    private String localPath;
    private String fragmentCls;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new PluginInflaterFactory());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        progressBar = (ProgressBar) findViewById(R.id.loading);

        localPath = getIntent().getStringExtra(IntentConst.INTENT_KEY_APK_PATH);
        fragmentCls = getIntent().getStringExtra(IntentConst.INTENT_KEY_FRAGMENT);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (!TextUtils.isEmpty(localPath)) {
                    installPlugin(localPath);
                    installFragment(fragmentCls);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressBar.setVisibility(View.GONE);
            }
        }.execute();

    }

    /**
     * 装载插件的运行环境,这个函数需要在Activity中运行，不能移动到Application中去
     */
    private void installPlugin(final String localPath) {
        PluginHelper.getInstance(this).install(localPath);
    }

    protected void installFragment(String fragClass) {
        try {
            if (isFinishing()) {
                return;
            }
            Fragment fragment = getFragment(localPath, fragClass);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, fragment).commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
