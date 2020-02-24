package com.ss.android.application.app.debug.ad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bytedance.i18n.business.debug.R;
import com.ss.android.application.app.debug.usage.AppUsageFragment;
import com.ss.android.framework.page.ArticleAbsFragment;
import com.ss.android.framework.page.BaseActivity;
import com.ss.android.utils.kit.string.StringUtils;

public class AdDebugActivity extends BaseActivity {
    public static final String KEY_FRAGMENT_TYPE = "key_fragment_type";
    public static final String KEY_AD_TYPE = "key_ad_type";
    public static final String FRAGMENT_TYPE_AD_PRIORITY = "fragment_native_ad_priority";
    public static final String FRAGMENT_TYPE_PRELOAD = "fragment_preload";
    public static final String FRAGMENT_TYPE_USAGE = "fragment_usage";
    public static final String FRAGMENT_TYPE_AD_STYLE = "fragment_ad_style";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        Intent intent = getIntent();
        String type = intent.getStringExtra(KEY_FRAGMENT_TYPE);
        String adType = intent.getStringExtra(KEY_AD_TYPE);
        final ArticleAbsFragment fragment;
        if (FRAGMENT_TYPE_AD_PRIORITY.equals(type)) {
            if (NewAdDebugPriorityFragment.AD_TYPE_NATIVE.equals(adType)) {
                fragment = new AdDebugPriorityFragment();
                setTitle("Native Ads Priority");
                mRightBtn.setText("Reset");
            } else if (NewAdDebugPriorityFragment.AD_TYPE_INTERSTITIAL.equals(adType)) {
                fragment = new NewAdDebugPriorityFragment();
                ((NewAdDebugPriorityFragment) fragment).setAdType(adType);
                setTitle(adType + "Ads Priority");
                mRightBtn.setText("");
            } else {
                finish();
                return;
            }
        } else if (FRAGMENT_TYPE_PRELOAD.equals(type)) {
            fragment = new AdDebugPreloadFragment();
            setTitle("Ads Preload Status");
            mRightBtn.setText("Refresh");
        } else if (FRAGMENT_TYPE_USAGE.equals(type)) {
            fragment = new AppUsageFragment();
            setTitle("App Usage Summary");
            mRightBtn.setText("Refresh");
        } else if (FRAGMENT_TYPE_AD_STYLE.equals(type)) {
            fragment = new AdDebugStyleFragment();
        } else {
            finish();
            return;
        }
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commitAllowingStateLoss();

        mRightBtn.setVisibility(View.VISIBLE);


        mRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment instanceof AdDebugPriorityFragment) {
                    ((AdDebugPriorityFragment) fragment).handleResetClick();
                } else if (fragment instanceof AdDebugPreloadFragment) {
                    ((AdDebugPreloadFragment) fragment).refreshData();
                } else if (fragment instanceof AppUsageFragment) {
                    ((AppUsageFragment) fragment).refreshData();
                }
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.ad_debug_activity;
    }
}
