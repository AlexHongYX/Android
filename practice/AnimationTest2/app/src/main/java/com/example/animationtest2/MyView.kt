package com.example.animationtest2

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * 自定义View
 */
class MyView(context: Context, attrs: AttributeSet): View(context){

    val RADIUS = 70f
    var currentPoint:Point? = null
    val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        mPaint.color = Color.BLUE
    }

    override fun onDraw(canvas: Canvas?) {
        if (currentPoint == null){
            currentPoint = Point(RADIUS,RADIUS)

            val x = currentPoint!!.x
            val y = currentPoint!!.y
            canvas!!.drawCircle(x,y,RADIUS,mPaint)

            val startPoint = Point(RADIUS,RADIUS)
            val endPoint = Point(700f,1000f)

            val anim = ValueAnimator.ofObject(PointEvaluator(),startPoint,endPoint)
            anim.duration = 5000

            anim.addUpdateListener {
                currentPoint = it.animatedValue as Point
                invalidate()
            }
            anim.start()
        }else {
            val x = currentPoint!!.x
            val y = currentPoint!!.y
            canvas!!.drawCircle(x,y,RADIUS,mPaint)
        }
    }
}