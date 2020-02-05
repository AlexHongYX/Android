package com.example.mvvmtest

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

class MasterFragment: Fragment(){

    // 创建ViewModel属性供所有方法使用
    private var model:MyViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 通过activity获取当前fragment所属的activity中的ViewModel
        model = activity?.let { ViewModelProviders.of(it).get(MyViewModel::class.java) }
    }
}