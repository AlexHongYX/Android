package com.example.myhelotest


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView的适配器Adapter
 */
class FruitAdapter2(fruitList:List<Fruit>): RecyclerView.Adapter<FruitAdapter2.ViewHolder>(){

    /**
     * 声明内部类用于缓存View->之后直接操作ViewHolder即可，无需反复调用findXXX操作
     */
    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val fruitImage:ImageView = view.findViewById(R.id.fruit_image)
        val fruitName:TextView = view.findViewById(R.id.fruit_name)
    }

    // 声明数据集合
    var mFruitList:List<Fruit> = fruitList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 获取View对象
        val view:View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fruit_item2,parent,false)
        // 创建holder对象
        val holder = ViewHolder(view)
        // 通过holder设置View对象中元素的点击事件
        holder.fruitName.setOnClickListener{
            val position = holder.adapterPosition
            val fruit = mFruitList[position]
            Toast.makeText(it.context,"you clicked name "+fruit.name,Toast.LENGTH_SHORT).show()
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit:Fruit = mFruitList[position]
        // 通过ViewHolder渲染当前元素
        holder.fruitImage.setImageResource(fruit.imageId)
        holder.fruitName.text = fruit.name
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()){
            onBindViewHolder(holder,position)
        }else{
            val payload = payloads[0] as Bundle
            holder.fruitImage.setImageResource(payload.getInt("KEY_IMAGE"))
        }
    }
}
















