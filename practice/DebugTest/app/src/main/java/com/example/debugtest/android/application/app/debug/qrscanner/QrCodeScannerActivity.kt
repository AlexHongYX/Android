package com.ss.android.application.app.debug.qrscanner

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bytedance.i18n.business.framework.legacy.service.locale.ILocaleHelper
import com.ss.android.framework.page.slideback.AbsSlideBackActivity
import com.ss.android.framework.statistic.asyncevent.EventV3
import com.ss.android.uilib.utils.StatusBarUtil
import com.ss.android.utils.loadFirst

/**
 *
 * @description:
 * @author: yangyang
 * @data 2019-08-30 10:56
 */
class QrCodeScannerActivity : AbsSlideBackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.setVenusStatusBarColor(this, ContextCompat.getColor(this, com.ss.android.article.pagenewark.R.color.white))

        ILocaleHelper::class.loadFirst().updateLocale(this)
        mEventParamHelper.put(EventV3.ParamKeys.SETTINGS_POSITION, EventV3.SettingsPosition.SETTINGS)
        val qrFragment = QrCodeScannerFragment()
        supportFragmentManager.beginTransaction()
                .replace(com.ss.android.article.pagenewark.R.id.fragment_container, qrFragment)
                .commitAllowingStateLoss()
        title = "QrCodeScanner"

    }

}
