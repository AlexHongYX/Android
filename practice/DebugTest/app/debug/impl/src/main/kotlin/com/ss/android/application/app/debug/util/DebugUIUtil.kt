package com.ss.android.application.app.debug.util

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.view.View
import android.view.ViewParent
import android.widget.TextView
import com.bytedance.i18n.business.debug.R
import com.bytedance.i18n.business.framework.legacy.service.constants.ArticleBaseBuildConfig
import com.bytedance.i18n.business.framework.legacy.service.setting.IDebugSettings
import com.google.android.material.snackbar.Snackbar
import com.ss.android.application.article.article.Article
import com.ss.android.application.article.article.needNativeTransCode
import com.ss.android.article.pagenewark.BuildConfig
import com.ss.android.buzz.getActivity
import com.ss.android.utils.loadFirst


object DebugUIUtil {
     var showArticleWebType = (BuildConfig.DEBUG || ArticleBaseBuildConfig.IS_LOCAL_TEST) && IDebugSettings::class.loadFirst().isShowArticleWebTypeEnable()

    private const val TYPE_AMP = "Amp"
    private const val TYPE_ORIGINAL_WEB = "Original"
    private const val TYPE_NATIVE_WEB = "NativeTrans"
    private const val TYPE_SMART_VIEW_WEB = "SmartView"
    private const val TYPE_SERVER_TRANS_WEB = "ServerTrans"
    private const val TYPE_UNKNOWN = "Unknown"

    private fun getArticleWebType(article: Article): String {
        if (article.needNativeTransCode()) {
            return TYPE_NATIVE_WEB
        }
        if (article.isSimpleWapArticleType) {
            val articleUrl = article.mArticleUrl
            //todo hxy
//            return if (AmpHelper.isAmp(articleUrl)) {
//                TYPE_AMP
//            } else {
//                TYPE_ORIGINAL_WEB
//            }
            return TYPE_ORIGINAL_WEB
        }
        if (article.isTranscodeArticleType) {
            if (article.isSmartView) {
                return TYPE_SMART_VIEW_WEB
            }
            return TYPE_SERVER_TRANS_WEB
        }
        return TYPE_UNKNOWN
    }

    @JvmStatic
    fun appendArticleWebTypeToTitleTextView(titleTextView: TextView?, article: Article?) {
        if (!(BuildConfig.DEBUG || ArticleBaseBuildConfig.IS_LOCAL_TEST)) {
            return
        }
        if (article == null || !showArticleWebType) {
            return
        }
        setSpannableText(titleTextView, article)
        val parentView: ViewParent = titleTextView?.parent ?: return
        if (parentView is View) {
            val popIcon: View = parentView.findViewById(R.id.popicon) ?: return
            appendLongClickListenerToPopIcon(popIcon, article)
        }
    }

    private fun appendLongClickListenerToPopIcon(popIcon: View?, article: Article?) {
        if (popIcon == null || article == null) {
            return
        }
        val articleStrInfo = getArticleStr(article)
        popIcon.setOnLongClickListener {
            val clipboardManager: ClipboardManager = popIcon.context.getActivity()?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            AlertDialog.Builder(popIcon.context)
                    .setTitle("Article")
                    .setMessage(articleStrInfo)
                    .setPositiveButton("Copy") { _, _ ->
                        run {
                            val clipData = ClipData.newPlainText("Article", articleStrInfo)
                            clipboardManager.primaryClip = clipData
                            Snackbar.make(popIcon, "Article has been copied to system clipboard.", Snackbar.LENGTH_LONG).show()
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                    .setIcon(R.drawable.icon)
                    .create()
                    .show()
            false
        }
    }

    private fun getArticleStr(article: Article?): String {
        article?.let {
            val strBuilder = StringBuilder()
            strBuilder
                    .append("Group Id:").append(it.mGroupId).append("\n")
                    .append("Item Id:").append(it.mItemId).append("\n")
                    .append("Impression Id:").append(it.mImprId).append("\n")
                    .append("Article Class:").append(it.mArticleClass).append("\n")
                    .append("Article Sub Class:").append(it.mArticleSubClass).append("\n")
                    .append("Article Url:").append(it.mArticleUrl).append("\n")
                    .append("Article Type:").append(it.mArticleType).append("\n")
                    .append("Display Type:").append(it.mDetailType).append("\n")
            return strBuilder.toString()
        }
        return "No Article"
    }

    private fun setSpannableText(textView: TextView?, article: Article) {
        if (textView == null) {
            return
        }
        val articleWebStyle = getArticleWebType(article)
        val titleStr = StringBuilder()
                .append("[")
                .append(articleWebStyle)
                .append("] ")
                .append(textView.text)
        textView.text = titleStr.toString()
    }

}
