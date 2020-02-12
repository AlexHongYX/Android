package com.example.myhelotest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyFragment2: Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment3,container,false)
        val button1: Button = view.findViewById(R.id.mButton1)
        // 获取数据集
        val model = activity?.let { it -> ViewModelProviders.of(it).get(MyViewModel::class.java) }
        // old和new的区分
        // 不仅要更新adapter的数据集，还要更新LiveData中的数据集
        val datas = model?.getFruitsLiveData()?.value
        button1.setOnClickListener {
            datas?.get(0)?.name = "hehe"
        }

        val button2: Button = view.findViewById(R.id.mButton2)
        button2.setOnClickListener {
            datas?.get(1)?.imageId = R.drawable.ic_launcher_background
        }

        return view
    }
}