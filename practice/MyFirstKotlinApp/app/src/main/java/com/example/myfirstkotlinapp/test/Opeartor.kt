package com.example.myfirstkotlinapp.test
data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int) : Comparable<MyDate> {

    override operator fun compareTo(other: MyDate): Int{
        return when{
            this.year!=other.year -> this.year - other.year
            this.month!=other.month -> this.month - other.month
            else -> this.dayOfMonth-other.dayOfMonth
        }
    }
}

fun compare(date1: MyDate, date2: MyDate) = date1 < date2