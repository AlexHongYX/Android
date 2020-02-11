package com.example.diffutiltest

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil

class DiffCallBack(oldDatas:List<TestBean>,newDatas:List<TestBean>): DiffUtil.Callback(){

    private val mOldDatas = oldDatas
    private val mNewDatas = newDatas

    /**
     * 判断是否是同一个对象->根据其唯一标识(名字)
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldDatas[oldItemPosition].name == mNewDatas[newItemPosition].name
    }

    /**
     * 判断两个对象的数据是否相同->只有新老集合中是同一个对象(areItemsTheSame返回true)->才能继续判断该对象的内容有没有发生变化
     * 如果返回false会进行定向更新->更新该项
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldBean = mOldDatas[oldItemPosition]
        val newBean = mNewDatas[newItemPosition]
        // 如果内容不同则返回false
        if(oldBean.desc != newBean.desc){
            return false
        }
        if (oldBean.pic != newBean.pic){
            return false
        }
        // 如果内容相同则返回true
        return true
    }

    /**
     * 与Adapter中的onBindViewHolder(三个参数)配合实现局部定向刷新
     *  当areItemsTheSame返回true+areContentsTheSame返回false
     *      也就是是同一个对象，但数据发生的变化->进行定向局部刷新->只需要改一处即可
     *  不是刷新某一项，而是刷新某一项的某一个属性
     */
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldBean = mOldDatas[oldItemPosition]
        val newBean = mNewDatas[newItemPosition]

        // key-value格式的集合
        val payload = Bundle()
        // 核心字段name不需要比较肯定相等->当areItemsTheSame返回true
        // 比较其他属性，将变化了的属性添加到payload中并回调Adapter的onBindViewHolder(两个参数,payload)
        if (oldBean.desc != newBean.desc){
            // 传入新变量的值
            payload.putString("KEY_DESC",newBean.desc)
        }
        if (oldBean.pic != newBean.pic){
            payload.putInt("KEY_PIC",newBean.pic)
        }

        // 如果都没变化则传null，但不可能为null->没变化咋可能areContentsTheSame返回false
        // 直接返回payload即可
        return payload
    }

    /**
     * 返回老集合的长度
     */
    override fun getOldListSize(): Int {
        return mOldDatas.size
    }

    /**
     * 返回新集合的长度
     */
    override fun getNewListSize(): Int {
        return mNewDatas.size
    }

}