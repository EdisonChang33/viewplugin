package com.hobby.viewplugin;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.hobby.pluginlib.base.Config;
import com.hobby.pluginlib.ui.BaseActivity;
import com.hobby.pluginlib.utils.ApkUtils;
import com.hobby.pluginlib.utils.FileUtils;

import java.io.File;

public class MainActivity extends BaseActivity {

    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn_text);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPluginHost();
            }
        });
    }

    private void startPluginHost() {
        PluginItem item = new PluginItem();
        item.pluginPath = getFilePath(this);
        item.rootFragment = ApkUtils.getRootFragment(this, item.pluginPath);
        item.appLogo = ApkUtils.getApplicationIcon(this, item.pluginPath);
        item.name = ApkUtils.getApplicationLabel(this, item.pluginPath);
        startFragment(item.rootFragment, item.pluginPath, new Bundle());
    }

    private String getFilePath(Context context) {
        //you can run task in thread
        File file = FileUtils.getPrivateFile(context, Config.SDK_APK_PLUGIN1);
        if (!FileUtils.isLegal(file)) {
            FileUtils.copyFromAssets(context, Config.SDK_APK_PLUGIN1, file);
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
