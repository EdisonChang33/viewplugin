package com.hobby.plugin3;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hobby.pluginlib.utils.ToastUtils;

public class SecondActivity extends AppCompatActivity {

    private Button buttonTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        buttonTest = (Button) findViewById(R.id.btn_text);

        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showShort(view.getContext(), R.string.toast_text);
            }
        });
    }

    @Override
    public ClassLoader getClassLoader() {
        return super.getClassLoader();
    }

    @Override
    public Resources.Theme getTheme() {
        return super.getTheme();
    }

    @Override
    public Resources getResources() {
        return super.getResources();
    }

    @Override
    public Context getBaseContext() {
        return super.getBaseContext();
    }
}
