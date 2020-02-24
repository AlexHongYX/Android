package com.ss.android.application.app.debug

import android.annotation.SuppressLint
import com.ss.android.framework.sharedpref.MultiProcessSharedPrefModel

@SuppressLint("StaticFieldLeak", "CI_StaticFieldLeak")
object BuzzDebugModel : MultiProcessSharedPrefModel() {
    override fun getPrefName(): String {
        return "buzz_debug_model"
    }

    override fun getMigrationVersion(): Int {
        return 0
    }

    override fun onMigrate(version: Int) {

    }

    val enableScreenShot = BooleanProperty("key_enable_screenshot_for_buzzshare", false)
}
