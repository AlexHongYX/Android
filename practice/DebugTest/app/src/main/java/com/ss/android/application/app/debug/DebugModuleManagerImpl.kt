package com.ss.android.application.app.debug

import com.bytedance.i18n.claymore.ClaymoreImpl

@ClaymoreImpl(IDebugModuleManager::class)
class DebugModuleManagerImpl : IDebugModuleManager {
    override fun isDisablePushFilter(): Boolean = DebugModuleManager.isDisablePushFilter()

    override fun isDisableMultiReachFilter(): Boolean = DebugModuleManager.isDisableMultiReachFilter()
}
