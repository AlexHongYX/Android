package com.example.databasetest

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class MyDatabaseHelper(context: Context,name: String,factory: SQLiteDatabase.CursorFactory?,version: Int)
    : SQLiteOpenHelper(context,name,factory,version){

    private val CREATE_BOOK = "create table Book (" +
            "id integer primary key autoincrement," +
            "author text," +
            "price real," +
            "pages integer," +
            "name text)"

    private val CREATE_CATEGORY = "create table Category (" +
            "id integer primary key autoincrement," +
            "category_name text," +
            "category_code integer)"

    // 由于需要在当前活动(上下文)中显示提示信息，因此需要将该上下文(活动)获取到，通过Toast的参数进行传递
    private var mContext: Context? = null
    init {
        mContext = context
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_BOOK)
        db?.execSQL(CREATE_CATEGORY)
        Toast.makeText(mContext,"Create succeeded",Toast.LENGTH_SHORT)
            .show()
    }

    /**
     * 更新:先将两张表删除再重新调用onCreate()
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists Book")
        db?.execSQL("drop table if exists Category")
        onCreate(db)
    }


}