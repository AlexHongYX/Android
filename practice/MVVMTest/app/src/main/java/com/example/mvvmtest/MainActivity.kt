package com.example.mvvmtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 获取MyViewModel实例->注意是获取不是创建(new)
        // 若Activity重建，虽然onCreate()会重新执行，但获取的仍是同一个MyViewModel实例
        val model = ViewModelProviders.of(this).get(MyViewModel::class.java)
        // 获取ViewModel中的LiveData，并添加观察者
        model.getUsersLiveData().observe(this, Observer {
            // 更新UI界面
        })
    }
}
