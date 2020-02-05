package com.example.fragmentbestpractice

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.StringBuilder
import java.util.*

class NewsTitleFragment: Fragment(){

    // 是否采取双页
    var isTwoPane = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.news_title_frag,container,false)
        val newsTitleRecyclerView:RecyclerView = view.findViewById(R.id.news_title_recycler_view)
        val layoutManager = LinearLayoutManager(activity)
        newsTitleRecyclerView.layoutManager = layoutManager
        val adapter = NewsAdapter(getNews())
        newsTitleRecyclerView.adapter = adapter
        return view
    }

    private fun getNews(): MutableList<News> {
        val newsList:MutableList<News> = mutableListOf()
        for (i in 1..50){
            val news = News("This is news title $i",
                getRandomLengthContent("This is news content $i . "))
            newsList.add(news)
        }
        return newsList
    }

    private fun getRandomLengthContent(content: String): String {
        val random = Random()
        val length = random.nextInt(20)+1
        val builder = StringBuilder()
        for(i in 0 until length){
            builder.append(content)
        }
        return builder.toString()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // news_content_layout布局!=null返回true
        // 采取双页
        isTwoPane = activity?.findViewById<View>(R.id.news_content_layout)!=null
    }

    // RecycleView展示
    inner class NewsAdapter(newsList:List<News>): RecyclerView.Adapter<NewsAdapter.ViewHolder>(){

        private val mNewsList = newsList

        inner class ViewHolder(view:View): RecyclerView.ViewHolder(view){
            val newsTitleText:TextView = view.findViewById(R.id.news_title)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item,parent,false)
            val holder = ViewHolder(view)

            view.setOnClickListener{
                val news = mNewsList[holder.adapterPosition]
                if (isTwoPane){
                    // 如果是双页模式，则刷新NewsContentFragment中的内容
                    val newsContentFragment:NewsContentFragment = fragmentManager
                        ?.findFragmentById(R.id.news_content_fragment) as NewsContentFragment
                    newsContentFragment.refresh(news.title,news.content)
                }else{
                    // 如果是单页模式，则直接启动NewsContentActivity
                    NewsContentActivity.actionStart(activity,news.title,news.content)
                }
            }
            return holder
        }

        override fun getItemCount(): Int {
            return mNewsList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val news = mNewsList[position]
            holder.newsTitleText.text = news.title
        }

    }
}
