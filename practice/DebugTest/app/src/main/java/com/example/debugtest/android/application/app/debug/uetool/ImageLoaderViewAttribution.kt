package com.ss.android.application.app.debug.uetool

import com.bytedance.i18n.business.debug.R
import com.ss.android.framework.imageloader.base.debug.ImageDebugInfo
import me.ele.uetool.base.Element
import me.ele.uetool.base.IAttrs
import me.ele.uetool.base.item.Item
import me.ele.uetool.base.item.TitleItem
import java.util.ArrayList

/**
 *
 * @author: weishenhong <a href="mailto:weishenhong@bytedance.com">contact me.</a>
 * @date: 2019-08-07 12:12
 *
 */

class ImageLoaderViewAttribution : IAttrs {

    override fun getAttrs(element: Element): List<Item> {
        val items = ArrayList<Item>()
        val imageDebugInfo = element.view.getTag(R.id.tag_glide_load_trace) as? ImageDebugInfo
        if (imageDebugInfo != null) {
            items.add(TitleItem("ImageLoader Debug"))
            items.add(ImageLoaderDebugItem(imageDebugInfo))
        }
        return items
    }
}
