package com.ss.android.application.app.debug.uetool

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import me.ele.uetool.attrdialog.AttrsDialogItemViewBinder

/**
 *
 * @author: weishenhong <a href="mailto:weishenhong@bytedance.com">contact me.</a>
 * @date: 2019-08-07 12:28
 *
 */

class ImageLoaderDebugItemBinder : AttrsDialogItemViewBinder<ImageLoaderDebugItem, ImageLoaderDebugViewHolder>() {
    override fun onCreateViewHolder(@NonNull inflater: LayoutInflater, @NonNull parent: ViewGroup, adapter: RecyclerView.Adapter<*>): ImageLoaderDebugViewHolder {
        return ImageLoaderDebugViewHolder.newInstance(parent)
    }

    override fun onBindViewHolder(@NonNull holder: ImageLoaderDebugViewHolder, @NonNull item: ImageLoaderDebugItem) {
        holder.bindView(item)
    }
}