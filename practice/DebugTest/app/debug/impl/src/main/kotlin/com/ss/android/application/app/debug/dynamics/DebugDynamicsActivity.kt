package com.ss.android.application.app.debug.dynamics

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bytedance.i18n.business.debug.R
import com.bytedance.i18n.business.framework.init.service.IDynamicFeatureLocalModel
import com.ss.android.commons.dynamic.installer.Dynamics
import com.ss.android.commons.dynamic.installer.configs.dynamicsConfig
import com.ss.android.utils.kit.Logger
import com.ss.android.utils.loadFirst
import kotlinx.android.synthetic.main.activity_debug_dynamics.*
import java.util.concurrent.TimeUnit

/**
 * @author: Created By nealkyliu
 * @date: 2019-06-11
 **/
class DebugDynamicsActivity : AppCompatActivity(), View.OnClickListener {

    inline fun ignoreCall(block : () -> Unit) {
        try {
            block()
        } catch (e : Throwable) {
            Logger.e("ignoreCall error", "", e)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_debug_dynamics)

        initView()
    }


    private fun initView() {
        dumpsSystemBt.setOnClickListener(this)
        dumpsGraphBt.setOnClickListener(this)
        saveSettingsBt.setOnClickListener(this)

        updateSettingsInfo()
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.dumpsSystemBt -> {
                dumpsSystemText.text = Dynamics.dumpsSystemInfo()
            }

            R.id.dumpsGraphBt -> {
                dumpsGraphText.text = Dynamics.dumpsGraph().fold("") { last, feature ->
                    val nameList = feature.flatList?.map { it.name }?.toList()
                    last + "\nfeatureName: ${feature.name} \nflatList: $nameList"
                }
            }

            R.id.saveSettingsBt -> {
                ignoreCall {

                    dynamicsConfig {
                        globalConfig {
                            retryIntervalSetEt.text.toString().toLongOrNull()?.let {
                                failRetryInterval = TimeUnit.MINUTES.toMillis(it)
                                IDynamicFeatureLocalModel::class.loadFirst().failRetryInterval.value = failRetryInterval
                            }

                            waitIntervalSetEt.text.toString().toLongOrNull()?.let {
                                waitRetryInterval = TimeUnit.MINUTES.toMillis(it)
                                IDynamicFeatureLocalModel::class.loadFirst().waitRetryInterval.value = waitRetryInterval
                            }
                        }
                    }

                    updateSettingsInfo()

                }
            }
        }
    }

    private fun updateSettingsInfo() {
        val settingInfo = "failRetryInterval: ${TimeUnit.MILLISECONDS.toMinutes(dynamicsConfig.failRetryInterval)}/min" +
            "\n waitRetryInterval: ${TimeUnit.MILLISECONDS.toMinutes(dynamicsConfig.waitRetryInterval)}/min "

        settingsText.text = settingInfo
    }
}
