package com.example.filepersistencetest

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import java.io.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private var edit:EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edit = findViewById(R.id.edit)

        val inputText = load()
        if(!TextUtils.isEmpty(inputText)){
            edit?.setText(inputText)
            edit?.setSelection(inputText.length)
            Toast.makeText(this,"Restoring succeeded",
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val inputText = edit?.text.toString()
        save(inputText)
    }

    /**
     * 写数据进入文件
     */
    private fun save(inputText: String){
        var out: FileOutputStream? = null
        var writer: BufferedWriter? = null
        try {
            out = openFileOutput("data", Context.MODE_PRIVATE)
            writer = BufferedWriter(OutputStreamWriter(out))
            writer.write(inputText)
        }catch (e: IOException){
            e.printStackTrace()
        }finally {
            try {
                writer?.close()
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    /**
     * 从文件中读数据
     */
    private fun load(): String{
        var input:FileInputStream? = null
        var reader:BufferedReader? = null
        val content:StringBuilder = StringBuilder()
        try {
            input = openFileInput("data")
            reader = BufferedReader(InputStreamReader(input))
            val text = reader.readText()
            content.append(text)
        }catch (e: IOException){
            e.printStackTrace()
        }finally {
            if(reader != null){
                try {
                    reader.close()
                }catch (e:IOException){
                    e.printStackTrace()
                }
            }
        }
        return content.toString()
    }
}
