package com.ss.android.application.app.debug

import android.app.ProgressDialog
import android.content.Context
import com.ss.android.article.pagenewark.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DebugJavaScriptUtil {
    companion object {
        const val TAG = "DebugJavaScriptUtil"
        @Volatile
        private var instance: DebugJavaScriptUtil? = null

        fun getInstance() = instance
            ?: synchronized(this) {
                instance ?: DebugJavaScriptUtil()
            }
    }

    fun doPatch(context: Context, urlStr: String) {
        val loadingView = ProgressDialog.show(context, "Script Update", "Script is updating...")
        loadingView.setIcon(R.drawable.icon)
        GlobalScope.launch(Dispatchers.IO) {
            val updateJob = async {
            }
            updateJob.await()
            loadingView.dismiss()
        }
    }
}
