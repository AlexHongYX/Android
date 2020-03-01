package com.example.broadcasttest3

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private val receiver1 = BroadcastReceiver1()
    private val receiver2 = BroadcastReceiver2()
//    多个filter对应多个接收器->设置filter的优先级即可
    private val filter1 = IntentFilter()
    private val filter2 = IntentFilter()

    /**
     * Android8后尽量使用动态注册的方式实现广播注册
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 动态注册广播
        // 两个filter设置同一个广播
        filter1.addAction("com.broadcast.action.MY_ACTION")
        filter2.addAction("com.broadcast.action.MY_ACTION")

        // 设置filter的优先级即可
        filter2.priority = 1000
        filter2.priority = 10

        // 不同filter对应不同接收器(都是同一个广播)
        registerReceiver(receiver1,filter1)
        registerReceiver(receiver2,filter2)

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener{
            // 发送广播
            val intent = Intent("com.broadcast.action.MY_ACTION")
            sendOrderedBroadcast(intent,null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver1)
        unregisterReceiver(receiver2)
    }
}
