package com.example.myfirstkotlinapp.test.forloop

import java.util.*

class DateRange(val start: MyDate, val end: MyDate): Iterable<MyDate>{
    override fun iterator(): Iterator<MyDate>  = DateIterator(this)
}

class DateIterator(val dateRange: DateRange) :Iterator<MyDate>{

    var current:MyDate = dateRange.start
    override fun hasNext(): Boolean = current<=dateRange.end

    override fun next(): MyDate {
        val result = current
        current = current.nextDay()
        return result
    }
}


fun iterateOverDateRange(firstDate: MyDate, secondDate: MyDate, handler: (MyDate) -> Unit) {
    for (date in firstDate..secondDate) {
        handler(date)
    }

}

data class MyDate(val year: Int,val month: Int,val dayOfMonth: Int):Comparable<MyDate>{
    override fun compareTo(other: MyDate): Int = when{
        this.year != other.year -> this.year - other.year
        this.month != other.month -> this.month - other.year
        else -> dayOfMonth - other.dayOfMonth
    }
}

operator fun MyDate.rangeTo(other: MyDate) = DateRange(this,other)

fun MyDate.nextDay() = addTimeIntervals(TimeInterval.DAY,1)

enum class TimeInterval{
    DAY,
    WEEK,
    YEAR
}

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