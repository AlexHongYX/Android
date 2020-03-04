package com.example.diffutiltest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DiffAdapter(mData:List<TestBean>): RecyclerView.Adapter<DiffAdapter.DiffVH>(){

    class DiffVH(view: View): RecyclerView.ViewHolder(view){
        val tv1:TextView = view.findViewById(R.id.tv1)
        val tv2:TextView = view.findViewById(R.id.tv2)
        val iv:ImageView = view.findViewById(R.id.iv)
    }

    val TAG = "xax"
    var mDatas = mData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiffVH {
        return DiffVH(LayoutInflater.from(parent.context).inflate(R.layout.item_diff,parent,false))
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    override fun onBindViewHolder(holder: DiffAdapter.DiffVH, position: Int) {
        val testBean = mDatas[position]
        holder.tv1.text = testBean.name
        holder.tv2.text = testBean.desc
        holder.iv.setImageResource(testBean.pic)
    }

    /**
    * 与DiffCallBack中的getChangePayload搭配使用实现定向局部更新
    */
    override fun onBindViewHolder(holder: DiffVH, position: Int, payloads: MutableList<Any>) {
        // payload不可能为null但可能为空，若为空则调用两个参数的onBindViewHolder做定向更新
        if (payloads.isEmpty()){
            onBindViewHolder(holder,position)
        }else{
            // 若不为空，则做定向局部更新
            // 当前payloads集合的第一个元素就是payload(Bundle)
            val payload:Bundle = payloads[0] as Bundle
            // 遍历该payload
            payload.keySet().forEach{
                // 根据不同属性的变化，局部更新属性的显示
                when(it){
                    "KEY_DESC"->holder.tv2.text = payload.getString(it)
                    "KEY_PIC"->holder.iv.setImageResource(payload.getInt(it))
                }
            }
        }
    }
}