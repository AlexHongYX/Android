package com.example.animationtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.*
import androidx.recyclerview.widget.RecyclerView

/**
 * 测试ViewGroup子元素的切换
 */
class SecActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sec)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)

        // 子元素出场动画
        val animation = AnimationUtils.loadAnimation(this,R.anim.view_animation)

        val controller = LayoutAnimationController(animation)
        controller.delay = 0.5f
        controller.order = LayoutAnimationController.ORDER_NORMAL

        recyclerView.layoutAnimation = controller

    }
}
