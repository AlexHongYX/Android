package com.example.servicetest

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class MyService : Service() {

    private val mBinder = DownloadBinder()

    /**
     * 模拟下载功能
     *  可决定何时开始下载以及随时查看下载进度
     */
    class DownloadBinder: Binder(){

        fun startDownload(){
            Log.d("MyService","startDownload executed")
        }

        fun getProgress():Int{
            Log.d("MyService","getProgress executed")
            return 0
        }

    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("MyService","onCreate executed")
        val intent = Intent(this,MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,0)
        val notification = NotificationCompat.Builder(this,"msg_1")
            .setContentTitle("This is Title")
            .setContentText("This is Text")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(resources,
                R.mipmap.ic_launcher))
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1,notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MyService","onStartCommand executed")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyService","onDestroy executed")
    }
}
