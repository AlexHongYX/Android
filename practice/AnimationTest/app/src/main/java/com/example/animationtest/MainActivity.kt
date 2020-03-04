package com.example.animationtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.button)

        // 使用组合补间动画
        val animationSet = AnimationSet(true)
        val animation1 = RotateAnimation(0F,360F,Animation.RELATIVE_TO_SELF,0.5F,Animation.RELATIVE_TO_SELF,0.5F)
        animation1.duration = 1000
        animation1.repeatMode = Animation.RESTART
        animation1.repeatCount = Animation.INFINITE

        val animation2 = TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,-0.5f,
            TranslateAnimation.RELATIVE_TO_PARENT,0.5f,
            TranslateAnimation.RELATIVE_TO_SELF,0f
            ,TranslateAnimation.RELATIVE_TO_SELF,0f)
        animation1.duration = 1000

        animationSet.addAnimation(animation1)
        animationSet.addAnimation(animation2)


        button.setOnClickListener {
            button.startAnimation(animationSet)
//            val intent = Intent(this,SecActivity::class.java)
//            startActivity(intent)
//            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
//            // 从左向右滑动的效果
//            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }
}
