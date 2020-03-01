package com.example.uiwidgettest

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button: Button = findViewById(R.id.button)
        val editText:EditText = findViewById(R.id.edit_text)
        val imageView:ImageView = findViewById(R.id.image_view)
        val progressBar:ProgressBar = findViewById(R.id.progress_bar)
        button.setOnClickListener {
//            点击一下按钮，本来显示就隐藏，本来隐藏就显示
//            if (progressBar.visibility == View.GONE){
//                progressBar.visibility = View.VISIBLE
//            }else{
//                progressBar.visibility = View.GONE
//            }
//            点击按钮进度条每次+10
//            var progressVal = progressBar.progress
//            progressVal += 10
//            progressBar.progress = progressVal
//            普通对话框
//            val dialog = AlertDialog.Builder(this)
//            dialog.setTitle("This is Dialog")
//            dialog.setMessage("Something important")
////            不可以通过Back键取消
//            dialog.setCancelable(false)
//            dialog.setPositiveButton("OK"){
//                dialog, which ->
//                    println("this is OK.$dialog,$which")
//            }
//            dialog.setNegativeButton("Cancel"){
//                dialog, which ->
//                    println("this is Cancel.$dialog,$which")
//            }
//            dialog.show()
        }
    }
}
