package com.example.servicetest

import android.app.IntentService
import android.content.Intent
import android.util.Log

/**
 * 用于执行耗时操作，服务创建新的线程并且服务执行完后自动停止
 */
class MyIntentService: IntentService("MyIntentService"){

    /**
     * 执行耗时服务操作
     */
    override fun onHandleIntent(intent: Intent?) {
        Log.d("MyIntentService:","Thread is"+Thread.currentThread().id)
    }

    /**
     * 测试会不会自动销毁服务
     */
    override fun onDestroy() {
        Log.d("MyIntentService:","onDestroy")
    }
}