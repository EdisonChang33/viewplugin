package com.hobby.plugin1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hobby.pluginlib.ui.BaseFragment;
import com.hobby.pluginlib.utils.ToastUtils;

/**
 * Created by Chenyichang on 2016/11/29.
 */

public class MainFragment extends BaseFragment {

    private Button buttonTest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_main, null, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        buttonTest = (Button) view.findViewById(R.id.btn_text);
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showShort(getContext(), R.string.toast_text);
            }
        });
    }
}
