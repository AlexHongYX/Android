package com.ss.android.application.app.debug

import android.widget.*
import android.widget.EditText
import com.bytedance.i18n.business.debug.R
/**
 * 数据类
 */

/**
 * 存放可添加数据的类型
 */
enum class ViewType (val id: Int){
    EditText(R.layout.debug_item_edit_text),
    CheckBox(R.layout.debug_item_checkbox),
    TextView(R.layout.debug_item_text_view),
    Spinner(R.layout.debug_item_spinner_view);
    var layoutId = id
}

/**
 * 存放所有Debug页面的初始数据及其类型
 */

open class DebugDataModel(val text:String,val viewType: ViewType)

class DebugCheckBoxModel(mText:String,val realization:(viewHolder:CheckBoxViewHolder)->Unit):DebugDataModel(mText,ViewType.CheckBox)
class DebugEditTextModel(mText:String,val realization:(viewHolder:EditTextViewHolder)->Unit):DebugDataModel(mText,ViewType.EditText)
class DebugTextViewModel(mText:String,val realization:(viewHolder:TextViewViewHolder)->Unit):DebugDataModel(mText,ViewType.TextView)
class DebugSpinnerModel(mText:String,val realization:(viewHolder:SpinnerViewHolder)->Unit):DebugDataModel(mText,ViewType.Spinner)

