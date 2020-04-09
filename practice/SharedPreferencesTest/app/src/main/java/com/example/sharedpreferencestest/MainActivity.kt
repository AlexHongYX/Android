package com.example.sharedpreferencestest

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button1: Button = findViewById(R.id.save_data)
        // 写操作
        button1.setOnClickListener {
            val sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("first",1)
            editor.putString("second","hello")
            editor.putBoolean("third",true)

            // 提交
            editor.apply()
        }

        val button2:Button = findViewById(R.id.get_data)
        // 读操作
        button2.setOnClickListener {
            val sharedPreferences = getSharedPreferences("data",Context.MODE_PRIVATE)
            val age = sharedPreferences.getInt("age",15)
            val name = sharedPreferences.getString("second","hello")
            val isNo = sharedPreferences.getBoolean("third",false)

            Log.d("MainActivity","age:$age")
            Log.d("MainActivity","name:$name")
            Log.d("MainActivity","isNo:$isNo")
        }
    }
}
