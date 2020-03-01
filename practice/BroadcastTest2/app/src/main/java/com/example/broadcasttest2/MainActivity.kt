package com.example.broadcasttest2

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener{
//            Intent对象用于创建广播对象
            val intent = Intent("com.example.broadcasttest2.BROADCAST")


//            通过send将该对象发送出去
            sendOrderedBroadcast(intent,null)
        }
    }
}
