package com.example.activitytest

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast

class FirstActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_layout)
        val button1:Button? = findViewById(R.id.button_1)
        Log.d("FirstActivity", "Task id is $taskId")
        button1?.setOnClickListener {
//            弹窗提示
//            Toast.makeText(this,"You clicked Button",
//                Toast.LENGTH_SHORT).show()
//            销毁活动
//            finish()
//            显式Intent
//            val intent = Intent(this,SecondActivity::class.java)
//            startActivity(intent)
//            隐式Intent
//            val intent = Intent("com.example.activitytest.ACTION_START")
//            intent.addCategory("com.example.activitytest.MY_CATEGORY")
//            startActivity(intent)
//            访问网页
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.data = Uri.parse("http://www.baidu.com")
//            startActivity(intent)
//            我调我自己，测试下standard启动模式
            val intent = Intent(this,SecondActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * 定义菜单
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        inflate(资源文件位置,菜单对象)
        menuInflater.inflate(R.menu.main,menu)
        return true
    }

    /**
     * 定义菜单响应事件
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        通过itemId判断点击的是哪个菜单项
        when(item.itemId){
            R.id.add_item -> Toast.makeText(this,"You clicked Add",
                Toast.LENGTH_SHORT).show()
            R.id.remove_item -> Toast.makeText(this,"You clicked Remove",
                Toast.LENGTH_SHORT).show()
        }
        return true
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("FirstActivity","onRestart")
    }
}











