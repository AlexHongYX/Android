package com.example.viewpagertest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class MyFragment4: Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fg_content,container,false)
        val textView: TextView = view.findViewById(R.id.txt_content)
        textView.text = "第四个Fragment"
        println("Fragment4")

        return view
    }
}