package com.hobby.pluginlib.sample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;

import com.hobby.pluginlib.PluginHelper;
import com.hobby.pluginlib.R;
import com.hobby.pluginlib.view.BottomLayout;
import com.hobby.pluginlib.inflater.PluginInflaterFactory;
import com.hobby.pluginlib.base.BasePluginActivity;
import com.hobby.pluginlib.utils.IntentConst;
import com.hobby.pluginlib.view.HostPageAdapter;

/**
 * Created by Chenyichang on 2016/11/29.
 */

public class PluginHostMultiTabActivity extends BasePluginActivity implements BottomLayout.TabOnClickListener {

    private String localPath[];
    private String fragmentCls[];

    private BottomLayout bottomLayout;
    private ViewPager viewPager;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new PluginInflaterFactory());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_host);
        bottomLayout = (BottomLayout) findViewById(R.id.bottom_layout);
        viewPager = (ViewPager) findViewById(R.id.content);
        progressBar = (ProgressBar) findViewById(R.id.loading);

        localPath = getIntent().getStringArrayExtra(IntentConst.INTENT_KEY_APK_PATHS);
        fragmentCls = getIntent().getStringArrayExtra(IntentConst.INTENT_KEY_FRAGMENTS);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (localPath != null && localPath.length > 0) {
                    installPlugin();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                initViewPager();
                progressBar.setVisibility(View.GONE);
            }
        }.execute();
    }

    /**
     * 装载插件的运行环境,这个函数需要在Activity中运行，不能移动到Application中去
     */
    private void installPlugin() {
        for (String path : localPath) {
            PluginHelper.getInstance().install(path);
        }
    }

    private void initViewPager() {
        FragmentPagerAdapter adapter = new HostPageAdapter(this, getSupportFragmentManager(), fragmentCls, localPath);
        viewPager.setAdapter(adapter);
        bottomLayout.setOnTabClickListener(this);
    }

    @Override
    public void onTabClicked(int index) {
        viewPager.setCurrentItem(index);
    }
}
