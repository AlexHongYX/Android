package com.ss.android.application.app.debug;

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle;
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View;
import android.view.ViewGroup
import android.widget.TextView;
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager

import com.bytedance.i18n.business.debug.R;
import com.bytedance.router.annotation.RouteUri;
import com.google.android.material.tabs.TabLayout
import com.ss.android.application.social.account.client.google.GoogleAccountClient
import com.ss.android.framework.page.slideback.AbsSlideBackActivity;

/**
 * Created by yihuaqi on 10/26/16.
 */
@RouteUri("//topbuzz/debug")
class DebugActivity : AbsSlideBackActivity() {

    var isBottom = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragment = DebugFragment(supportFragmentManager)
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitAllowingStateLoss()
        init()
        title = "Debug"
    }

    override fun getLayout(): Int {
        return R.layout.debug_activity
    }
}
