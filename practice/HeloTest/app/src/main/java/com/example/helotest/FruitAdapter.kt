package com.example.myhelotest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.helotest.R

/**
 * RecyclerView的适配器Adapter
 */
class FruitAdapter(fruitList:List<Fruit>): RecyclerView.Adapter<FruitAdapter.ViewHolder>(){

    /**
     * 声明内部类用于缓存View->之后直接操作ViewHolder即可，无需反复调用findXXX操作
     */
    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val fruitImage:ImageView = view.findViewById(R.id.fruit_image)
        val fruitName:TextView = view.findViewById(R.id.fruit_name)

        val fruitView:View = view
    }

    // 声明数据集合
    val mFruitList:List<Fruit> = fruitList

    /**
     * 创建holder实例
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 获取View对象
        val view:View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fruit_item,parent,false)
        // 创建holder对象
        val holder = ViewHolder(view)
        // 通过holder设置View对象中元素的点击事件
        holder.fruitName.setOnClickListener{
            val position = holder.adapterPosition
            val fruit = mFruitList[position]
            Toast.makeText(it.context,"you clicked view "+fruit.name,Toast.LENGTH_SHORT).show()
        }
        holder.fruitImage.setOnClickListener{
            val position:Int = holder.adapterPosition
            val fruit:Fruit = mFruitList[position]
            Toast.makeText(it.context,"you clicked image "+fruit.name,Toast.LENGTH_SHORT).show()
        }
        return holder
    }

    /**
     * 返回集合长度
     */
    override fun getItemCount(): Int {
        return mFruitList.size
    }

    /**
     * 渲染当前元素
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit:Fruit = mFruitList[position]
        // 通过ViewHolder渲染当前元素
        holder.fruitImage.setImageResource(fruit.imageId)
        holder.fruitName.text = fruit.name
    }


}