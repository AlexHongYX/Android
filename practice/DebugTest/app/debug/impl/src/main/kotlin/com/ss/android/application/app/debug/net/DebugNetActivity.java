package com.ss.android.application.app.debug.net;

import com.bytedance.i18n.business.debug.R;
import com.ss.android.framework.page.BaseActivity;

/**
 * Created by yihuaqi on 5/21/17.
 */

public class DebugNetActivity extends BaseActivity {
    @Override
    protected void init() {
        super.init();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new DebugNetFragment())
                .commitAllowingStateLoss();
    }
}
