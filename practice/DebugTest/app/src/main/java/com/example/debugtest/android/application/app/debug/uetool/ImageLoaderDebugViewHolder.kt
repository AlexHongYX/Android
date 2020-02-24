package com.ss.android.application.app.debug.uetool

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.bytedance.i18n.business.debug.R
import me.ele.uetool.AttrsDialog

/**
 *
 * @author: weishenhong <a href="mailto:weishenhong@bytedance.com">contact me.</a>
 * @date: 2019-08-07 12:18
 *
 */

class ImageLoaderDebugViewHolder(itemView: View) : AttrsDialog.Adapter.BaseViewHolder<ImageLoaderDebugItem>(itemView) {

    private val originButton: Button = itemView.findViewById(R.id.origin_image)
    private val thumbnailButton: Button = itemView.findViewById(R.id.thumbnail_image)
    private val reuseButton: Button = itemView.findViewById(R.id.view_reuse_trace)

    override fun bindView(item: ImageLoaderDebugItem) {
        super.bindView(item)
        originButton.setOnClickListener {
            item.imageDebugInfo.printCurrentRequestTrace()
        }
        thumbnailButton.setOnClickListener {
            item.imageDebugInfo.printCurrentThumbnailRequestTrace()
        }
        reuseButton.setOnClickListener {
            item.imageDebugInfo.printReuseTrace()
        }
    }

    companion object {

        fun newInstance(parent: ViewGroup): ImageLoaderDebugViewHolder {
            return ImageLoaderDebugViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.uet_cell_imageloader_debug, parent, false))
        }
    }

}
