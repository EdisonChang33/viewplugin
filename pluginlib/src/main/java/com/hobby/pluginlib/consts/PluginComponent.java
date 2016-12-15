package com.hobby.pluginlib.consts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chenyichang on 2016/12/15.
 */

public class PluginComponent {

    private static final Map<String, List<String>> mapPkgCls = new HashMap<>();
    private static boolean hasInit;

    public static void init() {
        if (hasInit){
            return;
        }
        hasInit = true;
        List<String> pluginClassNames = new ArrayList<>(7);
        pluginClassNames.add("com.qihoo360.newssdk.page.ChannelEditorPage");
        pluginClassNames.add("com.qihoo360.newssdk.page.NewsImagePage");
        pluginClassNames.add("com.qihoo360.newssdk.page.NewsVideoPage");
        pluginClassNames.add("com.qihoo360.newssdk.page.NewsWebViewPage");
        pluginClassNames.add("com.qihoo360.newssdk.page.CommentInfoPage");
        pluginClassNames.add("com.qihoo360.newssdk.page.AppDetailActivity");
        pluginClassNames.add("com.qihoo360.newssdk.page.AppDetailImageActivity");
        mapPkgCls.put("com.qihoo360.newssdk", pluginClassNames);

        List<String> plugin1ClassNames = new ArrayList<>(1);
        plugin1ClassNames.add("com.hobby.plugin1.SecondActivity");
        mapPkgCls.put("com.hobby.plugin1", plugin1ClassNames);

        List<String> plugin2ClassNames = new ArrayList<>(1);
        plugin2ClassNames.add("com.hobby.plugin2.SecondActivity");
        mapPkgCls.put("com.hobby.plugin2", plugin2ClassNames);

        List<String> plugin3ClassNames = new ArrayList<>(1);
        plugin3ClassNames.add("com.hobby.plugin3.SecondActivity");
        mapPkgCls.put("com.hobby.plugin3", plugin3ClassNames);
    }

    public static String getPluginPkgName(String activityName) {
        for (Map.Entry<String, List<String>> entry : mapPkgCls.entrySet()) {
            if (entry.getValue().contains(activityName)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
