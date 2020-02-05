package com.example.mvvmtest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel: ViewModel(){
    // 将用户列表存放在usersLiveData中
    private var usersLiveData:MutableLiveData<MutableList<User>>? = null

    /**
     * 获取LiveData
     *  将返回值设置为LiveData<MutableList<User>>
     *  防止在ViewModel外部进行修改
     */
    fun getUsersLiveData():LiveData<MutableList<User>>{
        if (usersLiveData == null){
            usersLiveData = MutableLiveData()
            loadUsers()
        }
        return usersLiveData as LiveData<MutableList<User>>
    }

    /**
     * 加载用户
     */
    fun loadUsers(){}
}