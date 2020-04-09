package com.example.broadcastbestpractice

import android.app.Activity

class ActivityController {

    companion object{
        private val activities:MutableList<Activity> = ArrayList()

        fun addActivity(activity: Activity){
            activities.add(activity)
        }

        fun removeActivity(activity: Activity){
            activities.remove(activity)
        }

        fun finishAll(){
            activities.removeAll(activities)
        }
    }
}