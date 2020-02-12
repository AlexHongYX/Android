package com.example.myhelotest

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil

class DiffCallBack(oldDatas:List<Fruit>,newDatas:List<Fruit>): DiffUtil.Callback(){

    private val mOldDatas = oldDatas
    private val mNewDatas = newDatas

    /**
     * 对象是否相同
     *  没有唯一标识，默认两个集合的元素相同，直接返回true
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return true
    }

    /**
     * 判断内容是否相同
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFruit = mOldDatas[oldItemPosition]
        val newFruit = mNewDatas[newItemPosition]
        if (oldFruit.name != newFruit.name){
            return false
        }
        if (oldFruit.imageId != newFruit.imageId){
            return false
        }
        return true
    }

    /**
     * 定向局部刷新
     */
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldFruit = mOldDatas[oldItemPosition]
        val newFruit = mNewDatas[newItemPosition]

        val payload = Bundle()

        // 改名改照片只能二选一因此使用if-else
        if (oldFruit.name != newFruit.name){
            payload.putString("KEY_NAME",newFruit.name)
        }
        if (oldFruit.imageId != newFruit.imageId){
            payload.putInt("KEY_IMAGE",newFruit.imageId)
        }
        return payload
    }

    override fun getOldListSize(): Int {
        return mOldDatas.size
    }

    override fun getNewListSize(): Int {
        return mNewDatas.size
    }
}