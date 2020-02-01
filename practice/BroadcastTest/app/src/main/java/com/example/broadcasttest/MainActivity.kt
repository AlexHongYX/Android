package com.example.broadcasttest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener{
            //            Intent对象用于创建广播对象
            val intent = Intent("com.example.broadcasttest.MY_BROADCAST")
//            通过send将该对象发送出去
            sendBroadcast(intent)
        }
    }
}
//动态注册
//class MainActivity : AppCompatActivity() {
//
//    private val intentFilter:IntentFilter = IntentFilter()
//    private val networkChangeReceiver:NetworkChangeReceiver = NetworkChangeReceiver()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
////        设置广播接收器到底要接收哪一条广播(action)
////        网络状态变化时系统发出的广播就是android.net.conn.CONNECTIVITY_CHANGE
//        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
////        完成广播接收器的注册
//        registerReceiver(networkChangeReceiver,intentFilter)
//    }
//
//    /**
//     * 动态注册的广播必须取消，因此在当前活动销毁时进行广播的取消
//     */
//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(networkChangeReceiver)
//    }
//
//    /**
//     * 创建一个自定义类继承BroadcastReceiver完成广播接收器的创建
//     */
//    class NetworkChangeReceiver: BroadcastReceiver(){
//        /**
//         * 当有广播到来时会执行该方法
//         */
//        override fun onReceive(context: Context?, intent: Intent?) {
//            Toast.makeText(context,"network changes",Toast.LENGTH_SHORT)
//                .show()
//
//        }
//    }
//}
