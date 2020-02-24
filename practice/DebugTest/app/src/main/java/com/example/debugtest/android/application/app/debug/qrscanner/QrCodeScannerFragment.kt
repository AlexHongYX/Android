package com.ss.android.application.app.debug.qrscanner

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bytedance.i18n.business.debug.R
import com.bytedance.i18n.claymore.ClaymoreServiceLoader
import com.ss.android.application.app.core.AppData
import com.ss.android.application.app.permission.PermissionHelper
import com.ss.android.application.app.schema.UrlRouterManager
import com.ss.android.framework.IQrcodeScanner
import com.ss.android.framework.ScanResultListener
import com.ss.android.framework.page.ArticleAbsFragment
import com.ss.android.framework.permission.PermissionsResultAction

/**
 *
 * @description:
 * @author: yangyang
 * @data 2019-08-30 10:58
 */
class QrCodeScannerFragment : ArticleAbsFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.debug_fragment_qr_scanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val qrCodeScanner = ClaymoreServiceLoader.loadFirstOrNull(IQrcodeScanner::class.java)

        qrCodeScanner?.setScanResultListener(object : ScanResultListener {
            override fun onResult(result: String) {
                if (!TextUtils.isEmpty(result)) {
                    UrlRouterManager.getInstance().handleOpenUri(context, result, null)
                }
                qrCodeScanner.stopDetectQR()
                finish()
            }
        })

        context?.let {
            val scannerView = qrCodeScanner?.getScannerView(it)
            scannerView?.visibility = View.GONE
            if (view is ViewGroup) {
                view.addView(scannerView)
            }
        }

        PermissionHelper.requestPermissions(AppData.inst().getCurrentActivity(), object : PermissionsResultAction() {
            override fun onGranted() {
                qrCodeScanner?.startDetectQR()
            }
            override fun onDenied(permission: List<String>) {}
        }, PermissionHelper.CAMERA)

    }

    private fun finish() {
        if (activity != null) {
            activity?.finish()
        }
    }
}
