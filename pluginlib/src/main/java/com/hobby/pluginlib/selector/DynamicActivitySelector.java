package com.hobby.pluginlib.selector;

import android.app.Activity;
import android.content.pm.ActivityInfo;

/**
 * @author Lody
 * @version 1.0
 */
public interface DynamicActivitySelector {

    Class<? extends Activity> selectDynamicActivity(ActivityInfo pluginActivityInfo);


}
