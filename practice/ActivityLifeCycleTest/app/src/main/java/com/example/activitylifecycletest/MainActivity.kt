package com.example.activitylifecycletest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {

    val TAG:String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"onCreate")
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null){
            val tempData = savedInstanceState.getString("data_key")
            Log.d(TAG,tempData)
        }

        var startNormalActivity:Button? = findViewById(R.id.start_normal_activity)
        var startDialogActivity:Button? = findViewById(R.id.start_dialog_activity)

        startNormalActivity?.setOnClickListener {
            val intent = Intent(this,NormalActivity::class.java)
            startActivity(intent)
        }

        startDialogActivity?.setOnClickListener {
            val intent = Intent(this,DialogActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG,"onRestart")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val tempData = "Something you just typed"
        outState.putString("data_key",tempData)
    }
}
