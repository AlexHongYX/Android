package com.example.databasetest

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dpHelper = MyDatabaseHelper(this,"BookStore.db",null,2)
        val button:Button = findViewById(R.id.create_database)
        button.setOnClickListener {
            dpHelper.writableDatabase
        }

        val sqLiteDatabase = dpHelper.writableDatabase
        val buttonInsert:Button = findViewById(R.id.insert)
        val buttonDelete:Button = findViewById(R.id.delete)
        val buttonUpdate:Button = findViewById(R.id.update)
        val buttonSelect:Button = findViewById(R.id.select)

        buttonInsert.setOnClickListener {
            sqLiteDatabase.execSQL("insert into Book(name,author,pages,price) values(?,?,?,?)",
                arrayOf("The Da Vinci Code","Dan Brown","454","16.96"))
            sqLiteDatabase.execSQL("insert into Book(name,author,pages,price) values(?,?,?,?)",
                arrayOf("The Lost Symbol","Dan Brown","510","19.95"))
        }

        buttonDelete.setOnClickListener {
            sqLiteDatabase.execSQL("delete from Book where pages > ?",
                arrayOf("500"))
        }

        buttonUpdate.setOnClickListener {
            sqLiteDatabase.execSQL("update Book set price = ? where name = ?",
                arrayOf("10.99","The Da Vinci Code"))
        }

        buttonSelect.setOnClickListener {
            // 返回Cursor对象
            val cursor:Cursor = sqLiteDatabase.rawQuery("select * from Book",null)
            // 遍历Cursor对象，取出数据并打印
            if (cursor.moveToFirst()){
                do{
                    val name = cursor.getString(cursor.getColumnIndex("name"))
                    val author = cursor.getString(cursor.getColumnIndex("author"))
                    val pages = cursor.getInt(cursor.getColumnIndex("pages"))
                    val price = cursor.getDouble(cursor.getColumnIndex("price"))
                    Log.d("MainActivity","name:$name,author:$author,pages:$pages,price:$price")
                }while (cursor.moveToNext())
            }
            cursor.close()
        }
    }
}
