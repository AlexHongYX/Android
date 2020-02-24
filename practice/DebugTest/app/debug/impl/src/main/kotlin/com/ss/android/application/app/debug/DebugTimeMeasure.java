package com.ss.android.application.app.debug;

import android.os.Environment;

import com.bytedance.i18n.business.framework.legacy.service.constants.ArticleBaseBuildConfig;
import com.ss.android.utils.kit.Logger;

import java.util.HashMap;

/**
 * Created by yihuaqi on 5/11/17.
 */

public class DebugTimeMeasure {
    private static final HashMap<String, Long> sMap = new HashMap<>();

    public static void measure(String name) {
        if (ArticleBaseBuildConfig.IS_LOCAL_TEST) {
            if (sMap.containsKey(name)) {
                Logger.i("PushDetailTime", name + " " + (System.currentTimeMillis() - sMap.get(name)));
                sMap.remove(name);
            } else {
                sMap.put(name, System.currentTimeMillis());
            }
        }
    }

    public static String getTracePath(String name) {
        return Environment.getExternalStorageDirectory()+"/traces/"+name+".trace";
    }

    public static void startTracing(String name) {
//        Debug.startMethodTracing(DebugTimeMeasure.getTracePath(name));
    }

    public static void stopTracing() {
//        Debug.stopMethodTracing();
    }
}
