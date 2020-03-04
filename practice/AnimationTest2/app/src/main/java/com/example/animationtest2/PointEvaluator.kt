package com.example.animationtest2

import android.animation.TypeEvaluator

/**
 * 自定义如何从初始点坐标过渡到结束点坐标
 */
class PointEvaluator: TypeEvaluator<Point>{
    override fun evaluate(fraction: Float, startValue: Point?, endValue: Point?): Point {
        // 根据fraction计算当前动画的x和y值
        val x = startValue!!.x + fraction*(endValue!!.x - startValue.x)
        val y = startValue.y + fraction*(endValue.y - startValue.y)

        return Point(x,y)
    }

}