package com.hobby.pluginlib.bottom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hobby.pluginlib.R;


/**
 * Created by Chenyichang on 2016/10/17.
 */

public class BottomLayout extends LinearLayout implements View.OnClickListener {

    private static final int TAB_COUNT = 3;
    private TabOnClickListener listener;
    private TextView[] containers = new TextView[TAB_COUNT];

    public BottomLayout(Context context) {
        this(context, null);
    }

    public BottomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.home_bottom_layout, this);
        containers[0] = (TextView) findViewById(R.id.tab1_txt);
        containers[1] = (TextView) findViewById(R.id.tab2_txt);
        containers[2] = (TextView) findViewById(R.id.tab3_txt);

        for (TextView container : containers) {
            container.setOnClickListener(this);
        }
        selectTab(0);
    }

    public void setOnTabClickListener(TabOnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tab1_txt) {
            selectTab(0);
        } else if (i == R.id.tab2_txt) {
            selectTab(1);
        } else if (i == R.id.tab3_txt) {
            selectTab(2);
        }
    }

    private void selectTab(int index) {
        for (int i = 0; i < TAB_COUNT; i++) {
            if (i == index) {
                containers[i].setTextColor(Color.RED);
            } else {
                containers[i].setTextColor(Color.BLACK);
            }
        }

        if (this.listener != null) {
            this.listener.onTabClicked(index);
        }
    }

    public interface TabOnClickListener {
        void onTabClicked(int index);
    }
}
