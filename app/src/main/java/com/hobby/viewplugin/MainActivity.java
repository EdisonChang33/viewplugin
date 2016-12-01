package com.hobby.viewplugin;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hobby.pluginlib.utils.Config;
import com.hobby.pluginlib.ui.BasePluginActivity;
import com.hobby.pluginlib.utils.ApkUtils;
import com.hobby.pluginlib.utils.FileUtils;

import java.io.File;

public class MainActivity extends BasePluginActivity {

    private Button btn;
    private Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn_text);
        btn2 = (Button) findViewById(R.id.btn_text2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPluginHost();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMultiPluginHost();
            }
        });
    }

    private void startPluginHost() {
        PluginItem item = new PluginItem();
        item.pluginPath = getFilePath(this, Config.SDK_APK_PLUGIN1);
        item.rootFragment = ApkUtils.getRootFragment(this, item.pluginPath);
        item.appLogo = ApkUtils.getApplicationIcon(this, item.pluginPath);
        item.name = ApkUtils.getApplicationLabel(this, item.pluginPath);
        startFragment(item.rootFragment, item.pluginPath, new Bundle());
    }

    private void startMultiPluginHost() {
        String[] fragments = new String[Config.SDK_APK_PLUGINS.length];
        String[] apkPaths = new String[Config.SDK_APK_PLUGINS.length];
        for (int index = 0; index < Config.SDK_APK_PLUGINS.length; index++) {
            PluginItem item = new PluginItem();
            item.pluginPath = getFilePath(this, Config.SDK_APK_PLUGINS[index]);
            item.rootFragment = ApkUtils.getRootFragment(this, item.pluginPath);
            //item.appLogo = ApkUtils.getApplicationIcon(this, item.pluginPath);
            //item.name = ApkUtils.getApplicationLabel(this, item.pluginPath);

            fragments[index] = item.rootFragment;
            apkPaths[index] = item.pluginPath;
        }

        startMultiFragment(fragments, apkPaths, new Bundle());
        // startFragment(item.rootFragment, item.pluginPath, new Bundle());
    }

    private String getFilePath(Context context, String jarName) {
        //you can run task in thread
        File file = FileUtils.getPrivateFile(context, jarName);
        if (!FileUtils.isLegal(file)) {
            FileUtils.copyFromAssets(context, jarName, file);
        }

        if (file != null && FileUtils.isLegal(file)) {
            return file.getAbsolutePath();
        } else {
            throw new RuntimeException("TestJarInvoker load jar file failed");
        }
    }

    public static class PluginItem {
        public CharSequence name;
        public String rootFragment;
        public String pluginPath;
        public Drawable appLogo;
    }

}
