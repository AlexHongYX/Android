package com.example.debugtest.android.application.app.debug.init;

import com.bytedance.i18n.business.framework.init.service.IModuleInitAdapter;
import com.bytedance.i18n.claymore.ClaymoreImpl;
import com.ss.android.application.app.debug.uetool.ImageLoaderDebugItem;
import com.ss.android.application.app.debug.uetool.ImageLoaderDebugItemBinder;
import com.ss.android.application.app.debug.uetool.ImageLoaderViewAttribution;

import me.ele.uetool.UETool;

@ClaymoreImpl(IModuleInitAdapter.class)
public class DebugModuleInit implements IModuleInitAdapter {

    @Override
    public void init() {
        initUETool();
    }

    @Override
    public String getName() {
        return "debug";
    }

    private void initUETool() {
        UETool.putAttrsProviderClass(ImageLoaderViewAttribution.class);
        UETool.registerAttrDialogItemViewBinder(ImageLoaderDebugItem.class, new ImageLoaderDebugItemBinder());
    }
}
