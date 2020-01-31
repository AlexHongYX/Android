package com.example.myfirstkotlinapp.test

fun <T> T.myApply(f: T.() -> Unit): T {
    this.f()
    return this
}

fun createString(): String {
    return StringBuilder().myApply {
        append("Numbers: ")
        for (i in 1..10) {
            append(i)
        }
    }.toString()
}

fun createMap(): Map<Int, String> {
    return hashMapOf<Int, String>().myApply {
        put(0, "0")
        for (i in 1..10) {
            put(i, "$i")
        }
    }
}


enum class Answer { a, b, c }

val answers = mapOf<Int, Answer?>(
    1 to Answer.a, 2 to null, 3 to null, 4 to null
)