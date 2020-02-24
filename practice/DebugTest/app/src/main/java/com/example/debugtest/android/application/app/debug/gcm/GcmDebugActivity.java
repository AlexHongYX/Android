package com.example.debugtest.android.application.app.debug.gcm;

import com.bytedance.i18n.business.debug.R;
import com.ss.android.framework.page.BaseActivity;

/**
 * Created by yihuaqi on 3/14/17.
 */

public class GcmDebugActivity extends BaseActivity {

    @Override
    protected void init() {
        super.init();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new GcmDebugFragment())
                .commitAllowingStateLoss();
    }
}
