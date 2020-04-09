package com.example.broadcastbestpractice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

/**
 * 设置所有活动的父类，
 *      用以在活动创建时给活动集合中添加活动，
 *      在活动销毁时在活动集合中删除该集合
 *
 *  由于所有活动都是继承自BaseActivity的，
 *      因此将广播接收器写在BaseActivity中
 *
 */
open class BaseActivity: AppCompatActivity(){

    private var receiver: ForcedOfflineReceiver? = null

    /**
     * 注册广播接收器
     */
    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.example.broadcastbestpractice.FORCE_OFFLINE")
        receiver = ForcedOfflineReceiver()
        registerReceiver(receiver,intentFilter)
    }

    /**
     * 取消广播接收器
     */
    override fun onPause() {
        super.onPause()
        if (receiver != null){
            unregisterReceiver(receiver)
            receiver = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityController.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityController.removeActivity(this)
    }

    /**
     * 所有活动都是BaseActivity的子类，会因此将广播接收器内嵌在公共父类中
     * 保证所有活动都有广播接收器
     */
    class ForcedOfflineReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            // 设置弹出框
            val builder = AlertDialog.Builder(context!!)
            builder.setTitle("Warning")
            builder.setMessage("You are forced to be offline.Please try to login again.")
            // 将对话框设置为不可取消
            builder.setCancelable(false)
            builder.setPositiveButton("OK"){
                dialog, which ->
                    // 销毁所有活动
                    ActivityController.finishAll()
                    val intent = Intent(context,LoginActivity::class.java)
                    // 重新启动LoginActivity
                    context.startActivity(intent)
            }
            builder.show()
        }
    }
}