package com.ss.android.common.util

import android.widget.Toast
import com.bytedance.i18n.business.framework.legacy.service.network.netclient.IArticleApiClient
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.ss.android.application.app.model.BaseHttpResponse
import com.ss.android.network.threadpool.SSThreadPoolProvider
import com.ss.android.uilib.toast.ToastUtil
import com.ss.android.utils.GsonProvider
import com.ss.android.utils.kit.Logger
import com.ss.android.utils.loadFirst
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers

data class DebugFakeData(@SerializedName("data") var fakeData: FakeDataItems)

data class FakeDataItem(@SerializedName("group_id") var gid: Long, @SerializedName("category") var category: String)

class FakeDataItems(gidList: LongArray, category: String) {
    @SerializedName("items")
    var mList: ArrayList<FakeDataItem> = ArrayList()

    init {
        for (gid in gidList) {
            mList.add(FakeDataItem(gid, category))
        }
    }
}

object DebugClientPgUtil {
    @JvmStatic
    fun addOrDeleteGidToCategory(gidList: LongArray, category: String = "223", isAddGid: Boolean) {
        val bodyStr = GsonProvider.getDefaultGson().toJson(
            DebugFakeData(
                FakeDataItems(gidList, category)
            )
        )
        Observable.create(Observable.OnSubscribe<Boolean> { subscriber ->
            try {
                val result = GsonProvider.getDefaultGson().fromJson<BaseHttpResponse<Any>>(
                    if (isAddGid)
                        IArticleApiClient::class.loadFirst().addToDebug(bodyStr)
                    else IArticleApiClient::class.loadFirst().deleteFromDebug(bodyStr), object : TypeToken<BaseHttpResponse<Any>>() {}.type)
                subscriber.onNext(result != null && result.isSuccess)
                subscriber.onCompleted()
            } catch (e: Exception) {
                subscriber.onError(e)
                subscriber.onCompleted()
            }
        }).subscribeOn(SSThreadPoolProvider.getCommonScheduler()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Subscriber<Boolean>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                if (Logger.debug()) {
                    e.printStackTrace()
                }
            }

            override fun onNext(success: Boolean?) {
                ToastUtil.toast(if (success != null && success) "Success" else "Fail", Toast.LENGTH_SHORT)
            }
        })
    }
}
