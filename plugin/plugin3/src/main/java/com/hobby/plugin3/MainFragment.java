package com.hobby.plugin3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hobby.pluginlib.base.BasePluginFragment;
import com.hobby.pluginlib.utils.ToastUtils;

/**
 * Created by Chenyichang on 2016/11/29.
 */

public class MainFragment extends BasePluginFragment {

    private Button buttonTest;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView(view);
        return view;
    }

    private void initView(View view) {
        buttonTest = (Button) view.findViewById(R.id.btn_text);
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SecondActivity.class);
                startActivity(intent);
            }
        });
    }
}
