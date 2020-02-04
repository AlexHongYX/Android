package com.example.fragmentbestpractice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class NewsContentFragment: Fragment(){
    // 属性名不能为view，Fragment中存在一个getView()用于返回该碎片的布局
    // 若再将属性名命名为view，相当于是在覆写getValue()->kotlin的属性默认生成getter/setter
    private var fragView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragView = inflater.inflate(R.layout.news_content_frag,container,false)
        return fragView
    }

    fun refresh(newsTitle:String,newsContent:String){
        // 获取布局
        val visibilityLayout:View? = fragView?.findViewById(R.id.visibility_layout)
        visibilityLayout?.visibility = View.VISIBLE

        // 将新闻标题和内容显示在界面上
        val newsTitleText: TextView? = fragView?.findViewById(R.id.news_title)
        val newsContentText:TextView? = fragView?.findViewById(R.id.news_content)

        newsTitleText?.text = newsTitle
        newsContentText?.text = newsContent
    }
}