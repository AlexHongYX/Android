package com.ss.android.application.app.debug;

import com.ss.android.application.app.debug.gcm.GcmDebugFragment;

public class DebugModuleManager {

    public static boolean isDisablePushFilter() {
        return GcmDebugFragment.GcmDebugModel.getInstance().mDisablePushFilter.getValue();
    }

    public static boolean isDisableMultiReachFilter() {
        return GcmDebugFragment.GcmDebugModel.getInstance().mDisableMultiReachFilter.getValue();
    }
}
