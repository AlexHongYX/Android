package com.ss.android.application.app.debug

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bytedance.i18n.business.debug.R

class DebugActivity: AppCompatActivity(){
    var rightText: TextView? = null
    var isBottom = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragment = DebugFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitAllowingStateLoss()
        title = "Debug"
        rightText = findViewById(R.id.right_text)
        rightText?.text = "Bottom"
        rightText?.visibility = View.VISIBLE
        rightText?.setOnClickListener { v ->
            if (isBottom) {
                rightText?.text = "Bottom"
            } else {
                rightText?.text = "Top"
            }
            isBottom = !isBottom
            fragment.scrollBottom(isBottom)

        }


    }

    protected fun getLayout(): Int {
        return R.layout.debug_activity
    }
}
