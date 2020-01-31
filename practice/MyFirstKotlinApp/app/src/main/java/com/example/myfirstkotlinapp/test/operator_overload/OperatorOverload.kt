package com.example.myfirstkotlinapp.test.operator_overload

import java.util.*

data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int)

enum class TimeInterval { DAY, WEEK, YEAR }

operator fun MyDate.plus(timeInterval: TimeInterval): MyDate = addTimeIntervals(timeInterval,1)

operator fun TimeInterval.times(number: Int) = RepeatedTimeInterval(this,number)

fun task1(today: MyDate): MyDate {
    return today + TimeInterval.YEAR + TimeInterval.WEEK
}

fun task2(today: MyDate): MyDate {
    return today + TimeInterval.YEAR*2 + TimeInterval.WEEK*3 + TimeInterval.DAY*5
}

class RepeatedTimeInterval(val timeInterval: TimeInterval,val number: Int)

operator fun MyDate.plus(timeIntervals:RepeatedTimeInterval):MyDate = addTimeIntervals(timeIntervals.timeInterval,timeIntervals.number)

fun MyDate.addTimeIntervals(timeInterval: TimeInterval,number: Int):MyDate{
    // 当前时间+number后的日期
    val c = Calendar.getInstance()
    c.set(year,month,dayOfMonth)
    when(timeInterval){
        TimeInterval.DAY -> c.add(Calendar.DAY_OF_MONTH,number)
        TimeInterval.WEEK -> c.add(Calendar.WEEK_OF_MONTH,number)
        TimeInterval.YEAR -> c.add(Calendar.YEAR,number)
    }
    return MyDate(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE))
}