package com.hobby.pluginlib.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hobby.pluginlib.base.IntentConst;

/**
 * Created by Chenyichang on 2016/11/29.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void startFragment(String fragment, String apkPath, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(IntentConst.ACTION_HOST_ACTIVITY);
        intent.putExtra(IntentConst.INTENT_KEY_APK_PATH, apkPath);
        intent.putExtra(IntentConst.INTENT_KEY_FRAGMENT, fragment);
        intent.putExtra(IntentConst.INTENT_KEY_BUNDLE, bundle);
        startActivity(intent);
    }
}
