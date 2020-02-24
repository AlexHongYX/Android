package com.ss.android.application.app.debug.net;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.bytedance.i18n.business.framework.legacy.service.network.netclient.INetClientController;
import com.bytedance.i18n.business.framework.legacy.service.network.netclient.INetModel;
import com.bytedance.i18n.business.framework.legacy.service.setting.IDebugSettings;
import com.bytedance.i18n.claymore.ClaymoreServiceLoader;
import com.bytedance.ttnet_wrapper.TTNetModuleManager;
import com.ss.android.application.app.debug.DebugAdapter;
import com.bytedance.i18n.business.framework.legacy.service.constants.CommonConstants;
import com.ss.android.network.threadpool.SSThreadPoolProvider;
import com.ss.android.uilib.toast.ToastUtil;
import com.ss.android.utils.kit.Logger;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

/**
 * Created by yihuaqi on 5/22/17.
 */

public class DebugNetAdapter extends DebugAdapter {

    // TODO: 2017/7/19 for some reason, omit NetGetNetwork
    private DebugNetApi mDebugNetApi;

    public DebugNetAdapter(Context context) {
        super(context);
        mItems = new Item[]{
                Item.NetSDK,
                Item.TTNET_CRONET_ENABLE,
//                Item.NetGetNetwork,
                Item.NetSearchSuggestion
        };
    }

    @Override
    protected void bindCheckBoxViewHolder(CheckBoxViewHolder holder, Item item) {
        holder.mCheckbox.setChecked(ClaymoreServiceLoader.loadFirst(IDebugSettings.class).getEnableCronet());
        holder.setOnCheckedChangeListener((buttonView, isChecked) -> ClaymoreServiceLoader.loadFirst(IDebugSettings.class).setEnableCronet(isChecked));
    }

    @Override
    protected void bindTextViewViewHolder(TextViewViewHolder holder, final Item item) {
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (item) {
                    case NetGetNetwork:
                        Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                try {
                                    if (mDebugNetApi == null) {
                                        mDebugNetApi = TTNetModuleManager.INSTANCE.articleApiClient().createArticleApiService("http://i.isnssdk.com/", DebugNetApi.class);
                                    }
                                    String response = mDebugNetApi.getNetwork().execute().body();
                                    subscriber.onNext(response);
                                } catch (Exception e) {
                                    subscriber.onError(e);
                                }
                            }
                        }).subscribeOn(SSThreadPoolProvider.getCommonScheduler())
                                .subscribe(new Observer<String>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Logger.d("TTNet", "onError: ", e);
                                    }

                                    @Override
                                    public void onNext(String s) {
                                        Logger.d("TTNet", "onNext: " + s);
                                    }
                                });
                        break;
                    case NetGetStream:
                        break;
                }
            }
        });
    }

    @Override
    protected void bindEditTextViewHolder(final EditTextViewHolder holder, final Item item) {
        switch (item) {
            case NetSDK:
                holder.setText(String.valueOf(ClaymoreServiceLoader.loadFirstOrNull(INetClientController.class).curNetSdkType()));
        }

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (item) {
                    case NetSearchSuggestion:
                        Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                try {
                                    Map<String, Object> query = new HashMap<>();
                                    query.put("query", holder.getText());
                                    String response = mDebugNetApi.getSearchTrending(CommonConstants.API_VERSION_INTER, query).execute().body();
                                    subscriber.onNext(response);
                                } catch (Exception e) {
                                    subscriber.onError(e);
                                }
                            }
                        }).subscribeOn(SSThreadPoolProvider.getCommonScheduler())
                                .subscribe(new Observer<String>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Logger.d("TTNet", "onError: ", e);
                                    }

                                    @Override
                                    public void onNext(String s) {
                                        Logger.d("TTNet", "onNext: " + s);
                                    }
                                });
                        break;
                    case NetSDK:
                        if (holder.getText().equals("0")) {
                            ToastUtil.toast("use old sdk", Toast.LENGTH_SHORT);
                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNetSdkType(0);
                        } else if (holder.getText().equals("1")) {
                            ToastUtil.toast("use TTNet", Toast.LENGTH_SHORT);
                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNetSdkType(1);
                        } else {
                            ToastUtil.toast("Unknown input, resetting...", Toast.LENGTH_SHORT);
                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNetSdkType(-1);
                        }
                }
            }
        });
    }
}
