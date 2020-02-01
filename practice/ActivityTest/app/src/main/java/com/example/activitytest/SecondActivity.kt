package com.example.activitytest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val button2:Button? = findViewById(R.id.button_2)
        Log.d("SecondActivity", "Task id is $taskId")
        button2?.setOnClickListener {
            val intent = Intent(this,ThirdActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SecondActivity","onDestroy")
    }
}
