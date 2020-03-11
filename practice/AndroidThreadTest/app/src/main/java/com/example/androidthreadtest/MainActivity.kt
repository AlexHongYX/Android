package com.example.androidthreadtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private val UPDATE_TEXT = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.change_text)
        val text:TextView = findViewById(R.id.text)

        val handler = object: Handler(){
            override fun handleMessage(msg: Message) {
                when(msg.what){
                    UPDATE_TEXT->
                        text.text = "Nice to meet you"
                }
            }
        }

        button.setOnClickListener {
            val thread = Thread{
                val message = Message()
                message.what = UPDATE_TEXT
                handler.sendMessage(message)
            }
            thread.start()
        }
    }
}
