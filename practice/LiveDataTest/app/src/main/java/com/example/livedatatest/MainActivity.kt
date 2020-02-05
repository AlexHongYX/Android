package com.example.livedatatest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mTvNumber:TextView = findViewById(R.id.tv_number)
        val mBtnStart: Button = findViewById(R.id.btn_start)

        // 定义一个livedata
        val mNumberLiveData = MutableLiveData<Int>()
        // 给livedata绑定一个观察者->一旦当前activity生命周期发生变化
        // 执行观察者的onChange()
        Transformations.map(mNumberLiveData){
            "$it"
        }.observe(this, Observer {
            // 更新UI
            mTvNumber.text = it
            Log.d("MainActivity:", it)
        })

        mBtnStart.setOnClickListener {
            Thread{
                for (i in 1..5){
                    try{
                        Thread.sleep(1000)
                    }catch (e:InterruptedException){
                        e.printStackTrace()
                    }
                    mNumberLiveData.postValue(i)
                }
            }.start()
        }
    }
}
