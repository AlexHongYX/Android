package com.ss.android.application.app.debug

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
    Spinner(R.layout.debug_item_spinner_view),
    Separator(R.layout.debug_item_separator);

    var layoutId = id
}

/**
 * 存放所有Debug页面的初始数据及其类型
 */
class DebugDataModel(val text:String,val viewType: ViewType)
