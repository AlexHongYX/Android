package com.example.activitytest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class ThirdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ThirdActivity", "Task id is $taskId")
        setContentView(R.layout.activity_third)
    }
}
