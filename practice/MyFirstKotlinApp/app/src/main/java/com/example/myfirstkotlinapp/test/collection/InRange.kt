package com.example.myfirstkotlinapp.test.collection

data class MyDate(val year: Int,val month: Int,val dayOfMonth: Int):Comparable<MyDate>{
    override fun compareTo(other: MyDate): Int = when{
        this.year != other.year -> this.year - other.year
        this.month != other.month -> this.month - other.year
        else -> dayOfMonth - other.dayOfMonth
    }
}

operator fun MyDate.rangeTo(other: MyDate) = DateRange(this,other)

class DateRange(override val start: MyDate, override val endInclusive: MyDate): ClosedRange<MyDate>

fun checkInRange(date: MyDate, first: MyDate, last: MyDate): Boolean {
    return date in first..last
}
