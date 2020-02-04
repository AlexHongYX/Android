package com.example.fragmentbestpractice

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NewsContentActivity : AppCompatActivity() {

    fun actionStart(context: Context,newsTitle: String,newsContent: String){
        val intent = Intent(context,NewsContentFragment::class.java)
        intent.putExtra("news_title",newsTitle)
        intent.putExtra("news_content",newsContent)
        context.startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_content)

        // 获取传入的新闻标题及内容
        val newsTitle = intent.getStringExtra("news_title")
        val newsContent = intent.getStringExtra("news_content")

        val newsContentFragment:NewsContentFragment = supportFragmentManager
            .findFragmentById(R.id.news_content) as NewsContentFragment
        newsContentFragment.refresh(newsTitle,newsContent)
    }
}
