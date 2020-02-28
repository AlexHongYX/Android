package com.example.servicetest

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton:Button = findViewById(R.id.start_service)
        val stopButton:Button = findViewById(R.id.stop_service)

        // 绑定活动与服务
        var downloadBinder:MyService.DownloadBinder
        val connection = object: ServiceConnection{
            override fun onServiceDisconnected(name: ComponentName?) {

            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                downloadBinder = service as MyService.DownloadBinder
                downloadBinder.startDownload()
                downloadBinder.getProgress()
            }
        }

        val bindButton:Button = findViewById(R.id.bind_service)
        val unbindButton:Button = findViewById(R.id.unbind_service)

        bindButton.setOnClickListener {
            val intent = Intent(this,MyService::class.java)
            // 绑定服务
            bindService(intent,connection, Context.BIND_AUTO_CREATE)
        }

        unbindButton.setOnClickListener {
            // 解绑服务
            unbindService(connection)
        }

        startButton.setOnClickListener {
            val startIntent = Intent(this,MyService::class.java)
            // 启动服务
            startService(startIntent)
        }
        stopButton.setOnClickListener {
            val stopIntent = Intent(this,MyService::class.java)
            // 停止服务
            stopService(stopIntent)
        }


        val  intentServiceButton:Button = findViewById(R.id.intent_service)
        intentServiceButton.setOnClickListener {
            // 打印主线程ID
            Log.d("MainActivity:","Thread id is "+Thread.currentThread().id)
            val intentService = Intent(this,MyIntentService::class.java)
            startService(intentService)
        }
    }
}
