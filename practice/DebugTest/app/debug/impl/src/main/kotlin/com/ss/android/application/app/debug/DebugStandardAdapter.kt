package com.ss.android.application.app.debug

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.i18n.business.debug.R
import com.bytedance.i18n.business.framework.legacy.service.setting.IDebugSettings
import com.bytedance.i18n.claymore.ClaymoreServiceLoader
import com.ss.android.uilib.utils.UIUtils
import java.util.*

/**
 * 4种不同的类型对应4种ViewHolder
 */
abstract class ViewHolder(view: View): RecyclerView.ViewHolder(view){
    @JvmField
    val mContext = view.context

    // 当设置mData值在子类中进行使用
    var mItems:DebugDataModel? = null
        set(value) {
            field = value
            onItemSet()
        }

    companion object{
        /**
         * 根据viewType来判断构建什么样的viewHolder
         */
        fun newViewHolder(view:View,viewType:ViewType):ViewHolder{
            return when(viewType){
                ViewType.EditText-> EditTextViewHolder(view)
                ViewType.CheckBox-> CheckBoxViewHolder(view)
                ViewType.TextView-> TextViewViewHolder(view)
                ViewType.Separator-> SeparatorViewHolder(view)
                ViewType.Spinner-> SpinnerViewHolder(view)
            }
        }
    }

    /**
     * 操作Data的属性->沉到子类做
     */
    abstract fun onItemSet()
}
class EditTextViewHolder(view: View) : ViewHolder(view) {

    @JvmField
    val mEditText: AppCompatEditText = view.findViewById(R.id.debug_edit_text)
    @JvmField
    val mTestButton: Button = view.findViewById(R.id.debug_btn)

    override fun onItemSet() {
        mEditText.hint = mItems?.text
    }

    fun getText(): String{
        return mEditText.text.toString()
    }

    fun setText(text: String){
        mEditText.setText(text)
    }

    fun setOnClickListener(listener: View.OnClickListener) {
        mTestButton.setOnClickListener(listener)
    }
}

class CheckBoxViewHolder(view: View): ViewHolder(view){

    @JvmField
    val mCheckbox: CheckBox = view.findViewById(R.id.debug_checked_text_view)

    override fun onItemSet() {
        mCheckbox.text = mItems?.text
    }

    fun setOnCheckedChangeListener(listener: CompoundButton.OnCheckedChangeListener?) {
        mCheckbox.setOnCheckedChangeListener(listener)
    }
}

class TextViewViewHolder(view: View): ViewHolder(view){

    @JvmField
    val mText: TextView = view.findViewById(R.id.text)

    override fun onItemSet() {
        mText.text = mItems?.text
    }
    fun setOnClickListener(listener: View.OnClickListener) {
        itemView.setOnClickListener(listener)
    }
}

/**
 * 搭配SpinnerViewHolder和某个case使用，还算有点用
 */
interface OnItemSelectListener {
    fun onItemSelect(position: Int, selectItem: String)
}
class SpinnerViewHolder(view: View) : ViewHolder(view) {
    @JvmField
    val mText: TextView = view.findViewById(R.id.title)
    @JvmField
    val mSpinnerContainer: LinearLayout = view.findViewById(R.id.spinner_container)
    private val adapters = ArrayList<ArrayAdapter<String>>()
    private val items = ArrayList<List<String>>()
    private val spinners = ArrayList<Spinner>()

    fun clearSpinnerItems() {
        mSpinnerContainer.removeAllViews()
    }

    fun addSpinnerItem(spinnerItems: List<String>?, listener: OnItemSelectListener?) {
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.topMargin = UIUtils.dip2Px(mContext, 5).toInt()
        params.bottomMargin = UIUtils.dip2Px(mContext, 5).toInt()
        val spinner = Spinner(mContext)
        mSpinnerContainer.addView(spinner, params)
        val adapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, spinnerItems!!)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        adapters.add(adapter)
        items.add(spinnerItems)
        spinners.add(spinner)
        if (listener != null) {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    if (spinnerItems.size - 1 >= position && position >= 0) {
                        listener!!.onItemSelect(position, spinnerItems[position])
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }

    fun addSpinnerItemAndSelect(spinnerItems: List<String>, listener: OnItemSelectListener?, selectedIndex: Int) {
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.topMargin = UIUtils.dip2Px(mContext, 5).toInt()
        params.bottomMargin = UIUtils.dip2Px(mContext, 5).toInt()
        val spinner = Spinner(mContext)
        mSpinnerContainer.addView(spinner, params)
        val adapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        adapters.add(adapter)
        items.add(spinnerItems)
        spinners.add(spinner)
        spinner.setSelection(selectedIndex)
        if (listener != null) {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    if (spinnerItems.size - 1 >= position && position >= 0) {
                        listener!!.onItemSelect(position, spinnerItems[position])
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }

    fun updateItems(index: Int, items: List<String>?) {
        if (adapters == null || index < 0 ||
                index >= adapters.size ||
                items == null || items.size <= 0) {
            return
        }
        val adapter = adapters[index]
        adapter.clear()
        adapter.addAll(items)
        adapter.notifyDataSetChanged()
    }

    fun setSelectedItem(index: Int, selectedItem: String) {
        if (index < 0 || index >= spinners.size) {
            return
        }
        val item = items[index]
        val spinner = spinners[index]
        if (item == null || spinner == null) {
            return
        }
        val size = item.size
        for (i in 0 until size) {
            if (TextUtils.equals(item[i], selectedItem)) {
                spinner.setSelection(i)
                break
            }
        }
    }

    fun setSelection(index: Int, itemIndex: Int) {
        if (index < 0 || index >= spinners.size) {
            return
        }
        val item = items[index]
        val spinner = spinners[index]
        if (item == null || spinner == null) {
            return
        }
        spinner.setSelection(itemIndex)
    }


    override fun onItemSet() {
        mText.text = mItems?.text
    }
}

class SeparatorViewHolder(itemView: View) : ViewHolder(itemView) {
    override fun onItemSet() {
        // nothing
    }
}

open class DebugStandardAdapter(list:List<DebugDataModel>,context:Context?): RecyclerView.Adapter<ViewHolder>(){
    @JvmField
    val TAG = DebugStandardAdapter::class.java.simpleName
    @JvmField
    var mItems = list
    @JvmField
    val mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewTypeIndex: Int): ViewHolder {
        val viewType = ViewType.values()[viewTypeIndex]
        val view = LayoutInflater.from(parent.context).inflate(viewType.layoutId,parent,false)
        return ViewHolder.newViewHolder(view,viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return mItems[position].viewType.ordinal
    }

    /**
     * 垃圾回收
     */
    override fun onViewRecycled(holder: ViewHolder) {
        if(holder is CheckBoxViewHolder){
            holder.setOnCheckedChangeListener(null)
            holder.mCheckbox.isEnabled
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.mItems = item
        when (item.viewType){
            ViewType.EditText-> {
                if (holder is EditTextViewHolder){
                    bindEditTextViewHolder(holder, item)
                }
            }
            ViewType.CheckBox-> {
                if (holder is CheckBoxViewHolder){
                    bindCheckBoxViewHolder(holder, item)
                }
            }
            ViewType.TextView-> {
                if (holder is TextViewViewHolder){
                    bindTextViewViewHolder(holder, item)
                }
            }
            ViewType.Separator-> {
                if (holder is SeparatorViewHolder){
                    bindSeparatorViewHolder(holder, item)
                }
            }
            ViewType.Spinner-> {
                if (holder is SpinnerViewHolder){
                    bindSpinnerViewHolder(holder, item)
                }
            }
        }
    }

    open fun bindEditTextViewHolder(holder: EditTextViewHolder,item: DebugDataModel){}
    open fun bindCheckBoxViewHolder(holder: CheckBoxViewHolder,item: DebugDataModel){}
    open fun bindTextViewViewHolder(holder: TextViewViewHolder,item: DebugDataModel){}
    open fun bindSeparatorViewHolder(holder: SeparatorViewHolder,item: DebugDataModel){}
    open fun bindSpinnerViewHolder(holder: SpinnerViewHolder,item: DebugDataModel){}

}
