package com.ss.android.application.app.debug;

import android.util.Log;
import android.widget.CompoundButton;

import com.bytedance.apm.logging.Logger;
import com.bytedance.i18n.business.framework.legacy.service.setting.IDebugSettings;
import com.bytedance.i18n.claymore.ClaymoreServiceLoader;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.bytedance.ad.symphony.AdSymphony;
import com.bytedance.crashtrigger.CrashTrigger;
import com.bytedance.crashtrigger.config.CrashTriggerConfig;
import com.bytedance.i18n.appbrandservice.IAppBrandService;
import com.bytedance.i18n.business.debug.R;
import com.bytedance.i18n.business.framework.legacy.service.appconfig.IAppConfig;
import com.bytedance.i18n.business.framework.legacy.service.applog.IAppLogV3;
import com.bytedance.i18n.business.framework.legacy.service.constants.AbsConstants;
import com.bytedance.i18n.business.framework.legacy.service.constants.ArticleBaseBuildConfig;
import com.bytedance.i18n.business.framework.legacy.service.network.netclient.ITTNetModuleInitHelper;
import com.bytedance.i18n.business.framework.legacy.service.setting.IDebugSettings;
import com.bytedance.i18n.business.framework.push.service.IJobModel;
import com.bytedance.i18n.business.mine.service.IMineService;
import com.bytedance.i18n.claymore.ClaymoreServiceLoader;
import com.bytedance.memory.test.OOMMaker;
import com.bytedance.router.SmartRouter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.ss.android.application.app.core.BaseApplication;
import com.ss.android.application.app.debug.ad.AdDebugActivity;
import com.ss.android.application.app.debug.ad.NewAdDebugPriorityFragment;
import com.ss.android.application.app.debug.data.BuzzCitiesProvider;
import com.ss.android.application.app.debug.data.CityInfo;
import com.ss.android.application.app.debug.dynamics.DebugDynamicsActivity;
import com.ss.android.application.app.debug.file.FileStructureTestActivity;
import com.ss.android.application.app.debug.fpsmeter.FpsMeterView;
import com.ss.android.application.app.debug.gcm.GcmDebugActivity;
import com.ss.android.application.app.debug.location.LocationTestMainActivity;
import com.ss.android.application.app.debug.net.DebugNetActivity;
import com.ss.android.application.app.debug.qrscanner.QrCodeScannerActivity;
import com.ss.android.application.app.debug.sharedpreference.DemoMultiProcessPrefModel;
import com.ss.android.application.app.debug.util.DebugUIUtil;
import com.ss.android.application.app.feedback.FeedbackServiceManager;
import com.ss.android.application.app.feedback.constants.FeedbackConstants;
import com.ss.android.application.social.account.client.google.GoogleAccountClient;
import com.ss.android.buzz.BuzzSPModel;
import com.ss.android.buzz.arouter.ArouterManager;
import com.ss.android.buzz.live.HeloLiveService;
import com.ss.android.buzz.live.IHeloLiveManager;
import com.ss.android.buzz.live.model.IHeloLiveModel;
import com.ss.android.buzz.ug.polaris.service.IPolarisDialogService;
import com.ss.android.buzz.util.FinalizeTimeOutObject;
import com.ss.android.buzz.util.clientab.ClientAbSPModel;
import com.ss.android.cricket.debug.CricketDebugConfig;
import com.ss.android.framework.imageloader.glideloader.datafetcher.TTNetUrlListFetcher;
import com.ss.android.framework.sharedpref.MultiProcessSharedPrefModel;
import com.ss.android.framework.statistic.asyncevent.AsyncEvent;
import com.ss.android.i18n.apkinjector.impl.v2.ApkInjectWrapper;
import com.ss.android.module.verify_applog.AppLogVerifyClient;
import com.ss.android.network.utils.CookieManagerCompat;
import com.ss.android.uilib.toast.ToastUtil;
import com.ss.android.uilib.utils.UIUtils;
import com.ss.android.utils.app.AppUtils;
import com.ss.android.utils.debug.DebugConsole;
import com.ss.android.widget.translation.ITranslationService;

import org.jetbrains.annotations.NotNull;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bytedance.crashtrigger.CrashTrigger;
import com.bytedance.crashtrigger.config.CrashTriggerConfig;
import com.bytedance.i18n.appbrandservice.IAppBrandService;
import com.bytedance.i18n.business.debug.R;
import com.bytedance.i18n.business.framework.legacy.service.constants.AbsConstants;
import com.bytedance.i18n.business.framework.legacy.service.constants.ArticleBaseBuildConfig;
import com.bytedance.i18n.business.framework.legacy.service.setting.IDebugSettings;
import com.bytedance.i18n.business.framework.push.service.IJobModel;
import com.bytedance.i18n.claymore.ClaymoreServiceLoader;
import com.bytedance.memory.test.OOMMaker;
import com.bytedance.router.SmartRouter;
import com.ss.android.application.app.core.BaseApplication;
import com.ss.android.application.app.debug.ad.AdDebugActivity;
import com.ss.android.application.app.debug.ad.NewAdDebugPriorityFragment;
import com.ss.android.application.app.debug.data.BuzzCitiesProvider;
import com.ss.android.application.app.debug.data.CityInfo;
import com.ss.android.application.app.debug.dynamics.DebugDynamicsActivity;
import com.ss.android.application.app.debug.file.FileStructureTestActivity;
import com.ss.android.application.app.debug.gcm.GcmDebugActivity;
import com.ss.android.application.app.debug.location.LocationTestMainActivity;
import com.ss.android.application.app.debug.net.DebugNetActivity;
import com.ss.android.application.app.debug.qrscanner.QrCodeScannerActivity;
import com.ss.android.application.app.feedback.FeedbackServiceManager;
import com.ss.android.application.app.feedback.constants.FeedbackConstants;
import com.ss.android.buzz.live.IHeloLiveManager;
import com.ss.android.buzz.ug.polaris.service.IPolarisDialogService;
import com.ss.android.buzz.util.FinalizeTimeOutObject;
import com.ss.android.i18n.apkinjector.impl.v2.ApkInjectWrapper;
import com.ss.android.module.verify_applog.AppLogVerifyClient;
import com.ss.android.uilib.toast.ToastUtil;
import com.ss.android.uilib.utils.UIUtils;
import com.ss.android.widget.translation.ITranslationService;

import org.jetbrains.annotations.NotNull;


import kotlin.Unit;
import me.ele.uetool.UETool;
import me.leolin.shortcutbadger.ShortcutBadger;

public class InitFragment {
    public static List<DebugDataModel> addUgc() {
        List<DebugDataModel> ugcList = new ArrayList<>();
        ugcList.add(new DebugCheckBoxModel("Force Jump To Post", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getForceJumpToPost());
            holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                setting.setForceJumpToPost(isChecked);
            });
            return Unit.INSTANCE;
        }));

        ugcList.add(new DebugCheckBoxModel("ForceQuickUpload", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getForceUseQuickUpload());
            holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                setting.setForceUseQuickUpload(isChecked);
            });
            return Unit.INSTANCE;
        }));

        ugcList.add(new DebugCheckBoxModel("Enable UgcChallenge", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getEnableUgcChallenge());
            holder.setOnCheckedChangeListener((buttonView, isChecked) ->
                    setting.setEnableUgcChallenge(isChecked));
            return Unit.INSTANCE;
        }));

        ugcList.add(new DebugCheckBoxModel("Enable Test Effect", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getEnableTestEffect());
            holder.setOnCheckedChangeListener((buttonView, isChecked) ->
                    setting.setEnableTestEffect(isChecked));
            return Unit.INSTANCE;
        }));

        ugcList.add(new DebugCheckBoxModel("Use UGC Preload", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getUgcPreload());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setUseUgcPreload(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));

        ugcList.add(new DebugCheckBoxModel("Use default mv preload config(first check Use UGC Preload)", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getUseDefaultPreloadEffectsConfig());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setUseDefaultPreloadEffectsConfig(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));

        ugcList.add(new DebugCheckBoxModel("Use Old Ugc Tools", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getForceUseOldUgcTools());
            holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                setting.setForceUseOldUgcTools(isChecked);
            });
            return Unit.INSTANCE;
        }));

        ugcList.add(new DebugCheckBoxModel("UGC disable image compression", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getDisableImageCompress());
            holder.setOnCheckedChangeListener((buttonView, isChecked) ->
                    setting.setDisableImageCompress(isChecked)
            );
            return Unit.INSTANCE;
        }));

        ugcList.add(new DebugCheckBoxModel("UGC use new Luban compute size", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.useNewLubanComputeSize());
            holder.setOnCheckedChangeListener((buttonView, isChecked) ->
                    setting.setUseNewLubanComputeSize(isChecked)
            );
            return Unit.INSTANCE;
        }));

        ugcList.add(new DebugCheckBoxModel("Force show MV", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getForceShowMV());
            holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                setting.setForceShowMV(isChecked);
            });
            return Unit.INSTANCE;
        }));

        ugcList.add(new DebugEditTextModel("UGC Entrance Type(-1,0,1,2)", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    try {
                        Log.d("UGCEntrance:","hello");
                        int type = Integer.parseInt(holder.getText());
                        System.out.println("UGCEntrance:"+type);
                        ClaymoreServiceLoader.loadFirst(IDebugSettings.class).setUgcEntranceType(type);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        ClaymoreServiceLoader.loadFirst(IDebugSettings.class).setUgcEntranceType(-1);
                    }
                }
            });
            return Unit.INSTANCE;
        }));
        return ugcList;
    }

    private static GoogleAccountClient mGoogleAccountClient;
    private static final String TAG = DebugStandardAdapter.class.getSimpleName();
    private static boolean isShowUETools = false;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static List<DebugDataModel> addOther() {
        List<DebugDataModel> otherList = new ArrayList<>();
        otherList.add(new DebugCheckBoxModel("Auto Clean Delay 5-10s", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(ClaymoreServiceLoader.loadFirstOrNull(IJobModel.class).getCleanCacheInShortDelay().getValue());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ClaymoreServiceLoader.loadFirstOrNull(IJobModel.class).getCleanCacheInShortDelay().setValue(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Google Login", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        try {
                            mGoogleAccountClient.authorize();
                        } catch (GooglePlayServicesNotAvailableException e) {
                            Logger.d(TAG, "try signIn");
                        }
                    } else {
                        try {
                            mGoogleAccountClient.signOut(new ResultCallback() {
                                @Override
                                public void onResult(@NonNull Result result) {
                                    Logger.d(GoogleAccountClient.TAG, result.getStatus().toString());
                                }
                            });
                        } catch (GooglePlayServicesNotAvailableException e) {
                            Logger.d(TAG, "try signOut");
                        }
                    }
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Show Article Web Type(Amp, Server TransCode or Original Web)", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.isShowArticleWebTypeEnable());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setShowArticleWebStyle(isChecked);
                    DebugUIUtil.INSTANCE.setShowArticleWebType(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Enable Native Ads Priority", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.isNativeAdPriorityEnable());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setNativeAdPriorityEnable(isChecked);
//                        AdCenter.getInstance(mContext).setDebugPriorityEnable(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Enable Interstitial Ads Priority", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.isNativeAdPriorityEnable());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setInterstitialAdPriorityEnable(isChecked);
                    AdSymphony.getInstance().getInterstitialAdManager().getFillStrategyManager().setDebugPriorityEnable(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Show Ad Provider Id", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.showAdProviderId());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setShowAdProviderId(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Use Http", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.isUseHttp());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setUseHttp(isChecked);
                    ClaymoreServiceLoader.loadFirstOrNull(ITTNetModuleInitHelper.class).setUseHttp(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Enable FPS Meter", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(FpsMeterView.isShowing());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        FpsMeterView.show(holder.mContext, 200);
                    } else {
                        FpsMeterView.hide();
                    }
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Always show login popup", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.shouldAlwaysShowLoginPopup());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setAlwaysShowLoginPopup(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Always get app settings", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.shouldAlwaysGetAppSettings());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setAlwaysGetAppSettings(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Always send sample http log", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.shouldAlwaysSendSampleHttp());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setAlwaysSendSampleHttp(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Fix Ok Http Proxy issue", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.shouldFixOkHttpProxy());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setShouldFixOkHttpProxy(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Apply MD Design on TabLayout", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.shouldApplyMdDesignOnTabLayout());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setApplyMdDesignOnTabLayout(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Always show video tab tip", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.shouldAlwaysShowVideoTabTip());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setAlwaysShowVideoTabTip(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Always show bottom tab refresh tip", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.shouldAlwaysShowBottomTabRefreshTip());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setAlwaysShowBottomTabRefreshTip(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Always jump to detail comment section", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.shouldAlwaysJumpToComment());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setAlwaysJumpToComment(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Always show pull to refresh guide", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.shouldAlwaysShowPullToRefreshGuide());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setAlwaysShowPullToRefreshGuide(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Always Show Video Error", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.showVideoErrorContent());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setShowVideoErrorContent(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Use SurfaceView", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.useSurfaceViewRenderView());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setUseSurfaceViewRenderView(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Show Used MediaPlayer", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.showMediaPlayerUsed());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setShowMediaPlayerUsed(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Test App Current Active Event", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.testAppActiveEvent());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setTestAppActiveEvent(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Local Push Enable Local Time Test", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.testLocalPush());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setLocalPushTestEnable(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Don't Bind Ad", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.doNotBindAd());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setDoNotBindAd(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Show Two Lines Relative News", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.showTwoLineRelativeNews());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setShowTwoLineRelativeNews(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Use async MediaPlayer", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.useAsyncMediaPlayer());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setUseAsyncMediaPlayer(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Show AddtoDebug", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.showAddtoDebug());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setShowAddtoDebug(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Show Feed Time", holder -> {
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Swip To Next Page", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getEnableSwipeToNext());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setEnableSwipeToNext(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Swip To Related Page", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getSwipeToRelated());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setSwipeToRelated(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Show Video Bitrate Info", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.showVideoBitrateLayout());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setShowVideoBitrateLayout(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("MediaPlayer Type Switch", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.useMediaPlayerDebug());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setMediaPlayerDebug(isChecked);
//                    List<DebugDataModel> list = new ArrayList<>(holder.getItems());
//                        mItems = filterData(isChecked, DebugDataModel.SelectMediaPlayerType, list.indexOf(DebugDataModel.SelectMediaPlayerSwitch) + 1);
//                    notifyDataSetChanged();
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Degree Youtube Leech", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.degreeYoutubeLeech());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setDegreeYoutubeLeech(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Http1 Only", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.useHttp1Only());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setUseHttp1Only(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Reset User", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(ClaymoreServiceLoader.loadFirstOrNull(IAppLogV3.class).isNewUserMode(holder.mContext));
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ClaymoreServiceLoader.loadFirstOrNull(IAppLogV3.class).setNewUserMode(holder.mContext, isChecked);
                    //        DataCleanManager.cleanApplicationData(mContext);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        ((ActivityManager) holder.mContext.getSystemService(Context.ACTIVITY_SERVICE)).clearApplicationUserData();
                    }else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            try {
                                CookieManagerCompat.getInstance().removeAllCookies(null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(holder.mContext.getApplicationContext());
                            cookieSyncManager.startSync();
                            CookieManager.getInstance().removeAllCookie();
                            cookieSyncManager.stopSync();
                        }

                        ToastUtil.toast("Restart app to take effect", Toast.LENGTH_SHORT);

                        ClaymoreServiceLoader.loadFirstOrNull(IMineService.class).getISwitchLanguageHelperUtil().goLaunchActivity(holder.mContext, true, false);
                    }

                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Always show buzz intro", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getAlwaysShowBuzzIntro());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setAlwaysShowBuzzIntro(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Show Invitation style", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getUseInvitationStyle());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setUseInvitationStyle(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Buzz always select language", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getBuzzAlwaysSelectLanguage());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setBuzzAlwaysSelectLanguage(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Show Debug Console", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(DebugConsole.isShown());
            Logger.d("Debug Console", "Debug Console setChecked " + DebugConsole.isShown());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        DebugConsole.show(holder.mContext);
                        DebugConsole.log("Debug Console On");
                        Logger.d("Debug Console", "Debug Console On");
                    } else {
                        DebugConsole.hide();
                        DebugConsole.log("Debug Console Off");
                        Logger.d("Debug Console", "Debug Console Off");
                    }
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Enable Screen Shot for BuzzShare", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(BuzzDebugModel.INSTANCE.getEnableScreenShot().getValue());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    BuzzDebugModel.INSTANCE.getEnableScreenShot().setValue(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Disable AppLog Encryption", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getDisableAppLogEncryption());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setDisableAppLogEncryption(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Always login as new user", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getAlwaysLoginAsNewUser());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setAlwaysLoginAsNewUser(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Use new login style", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getUseNewLoginStyle());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setUseNewLoginStyle(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Video Use MediaLoader To PreLoad", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getVideoUrlWithDataLoader());
            holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                setting.setVideoUrlWithDataLoader(isChecked);
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Enable Video Preload Radical", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getEnablePreloadVideoRadical());
            holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                setting.setEnablePreloadVideoRadical(isChecked);
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Enable Video Preload", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getEnableVideoPreload());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setEnableVideoPreload(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("TTVideo Use TTNet", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getTTVideoUseTTNet());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setTTVideoUseTTNet(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("ImageLoader Use TTNet", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getImageLoderUseTTnet());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setImageLoderUseTTnet(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Enable Image Request Debug", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    TTNetUrlListFetcher.Companion.setDEBUG(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("enable async inflate", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.enableAsyncInflate());
            holder.setOnCheckedChangeListener((buttonView, isChecked) ->
                    setting.setAsyncInflateEnable(isChecked));
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("enable x2c", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.enableX2C());
            holder.setOnCheckedChangeListener((buttonView, isChecked) ->
                    setting.setX2cEnable(isChecked));
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Enable immersive", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getImmersiveDebugEnable());
            holder.setOnCheckedChangeListener((buttonView, isChecked) ->
                    setting.setImmersiveDebugEnable(isChecked));
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Enable ImageView Debug", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getEnableImageDebug());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setEnableImageDebug(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Enable ImageView Reuse", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getEnableImageReuse());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setEnableImageReuse(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Enable Cricket Always Pull", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(CricketDebugConfig.INSTANCE.getAlwaysPull());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    CricketDebugConfig.INSTANCE.setAlwaysPull(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("EnableNewImmersiveVideoCard", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(BuzzSPModel.INSTANCE.isUseImmersiveNewVideoCard().getValue());
            holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                BuzzSPModel.INSTANCE.isUseImmersiveNewVideoCard().setValue(isChecked);
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("CrazyVideoDownload", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getCrazyVideoDownload());
            holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                setting.setCrazyVideoDownload(isChecked);
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("EnableMainFeedVideoCard", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(BuzzSPModel.INSTANCE.isUseMainFeedNewVideoCard().getValue());
            holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                BuzzSPModel.INSTANCE.isUseMainFeedNewVideoCard().setValue(isChecked);
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("immersive video vertical", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(BuzzSPModel.INSTANCE.isImmersiveVideoDirectVertical().getValue());
            holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                BuzzSPModel.INSTANCE.isImmersiveVideoDirectVertical().setValue(isChecked);
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("EnableNewMediaViewer", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(BuzzSPModel.INSTANCE.isUseNewMediaViewer().getValue());
            holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                BuzzSPModel.INSTANCE.isUseNewMediaViewer().setValue(isChecked);
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("enable live permission", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            MultiProcessSharedPrefModel.BooleanProperty canStartLive = ClaymoreServiceLoader.loadFirstOrNull(IHeloLiveModel.class).getCanStartLive();
            holder.mCheckbox.setChecked(canStartLive.getValue());
            holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                canStartLive.setValue(isChecked);
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Show live guide dialog", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            MultiProcessSharedPrefModel.BooleanProperty showed = ClaymoreServiceLoader.loadFirstOrNull(IHeloLiveModel.class).getShowedEntryGuideDialog();
            holder.mCheckbox.setChecked(!showed.getValue());
            holder.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                showed.setValue(!isChecked);
            }));
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("show live debug info", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            MultiProcessSharedPrefModel.BooleanProperty show = ClaymoreServiceLoader.loadFirstOrNull(IHeloLiveModel.class).getShowDebugInfo();
            holder.mCheckbox.setChecked(show.getValue());
            holder.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                show.setValue(isChecked);
            }));
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Use Voice Search", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getUseVoiceSearch());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setUseVoiceSearch(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("enable multi language", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getEnableMultiLanguage());
            holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setting.setEnableMultiLanguage(isChecked);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Enable Image Translation", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getEnableImageTranslate());
            holder.setOnCheckedChangeListener((buttonView, isChecked) -> setting.setEnableImageTranslate(isChecked));
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Enable Stream Protobuf", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getEnableStreamProtobuf());
            holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                setting.setEnableStreamProtobuf(isChecked);

            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Enable Backup DNS", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getEnableBackupDNS());
            holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                setting.setEnableBackupDNS(isChecked);

            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Disable Configurable Channel", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getDisableConfigurableChannel());
            holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                setting.setDisableConfigurableChannel(isChecked);
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugCheckBoxModel("Disable Stream Preload", holder -> {
            final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
            holder.mCheckbox.setChecked(setting.getDisableStreamPreload());
            holder.setOnCheckedChangeListener((buttonView, isChecked) -> setting.setDisableStreamPreload(isChecked));
            return Unit.INSTANCE;
        }));


        otherList.add(new DebugEditTextModel("set longitude", holder -> {
            holder.setText(ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getLongitude());
            holder.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    try {
                        String s = holder.getText().trim();
                        double value = Double.valueOf(s);
                        if (value > 0) {
                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setLongitude(s.trim());
                            //清空城市选择
                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseLanguage("");
                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseTier("");
                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseState("");
                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseCity("");

                        }
                    } catch (Exception e) {
                    }
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugEditTextModel("set latitude", holder -> {
            holder.setText(ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getLatitude());
            holder.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    try {
                        String s = holder.getText().trim();
                        double value = Double.valueOf(s.trim());
                        if (value > 0) {
                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setLatitude(s.trim());
                            //清空城市选择
                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseLanguage("");
                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseTier("");
                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseState("");
                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseCity("");
                        }
                    } catch (Exception e) {
                    }
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugEditTextModel("Short Cut Badge", holder -> {
            holder.setText("");
            holder.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    try {
                        ShortcutBadger.applyCount(holder.mContext, Integer.parseInt(holder.getText()));
                    } catch (Exception e) {

                    }
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugEditTextModel("Async Event", holder -> {
            holder.setText("");
            holder.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    AsyncEvent.test(holder.mContext);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugEditTextModel("Test Preference Property", holder -> {
            holder.setText("");
            holder.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    DemoMultiProcessPrefModel.sInstance.test();
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugEditTextModel("Update App Config", holder -> {
            holder.setText("");
            holder.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    ClaymoreServiceLoader.loadFirstOrNull(IAppConfig.class).testRefreshAppConfig();
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugEditTextModel("Push Detail Back Strategy", holder -> {
            holder.setText("");
            holder.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    try {
                        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setPushDetailBackStrategy(Integer.valueOf(holder.getText()));
                        ToastUtil.toast("Using push strategy: " + holder.getText(), Toast.LENGTH_SHORT);
                    } catch (Exception e) {
                        ToastUtil.toast(e.getMessage(), Toast.LENGTH_SHORT);
                    }
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugEditTextModel("watch_live", holder -> {
            holder.setText("");
            holder.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    String id = holder.getText().trim();
                    HeloLiveService service = ClaymoreServiceLoader.loadFirstOrNull(HeloLiveService.class);
                    Context context = holder.mContext;
                    if (service != null && context != null) {
                        try {
                            long roomId = Long.parseLong(id);
                            service.joinLive(context, roomId, null, null, "debug", "debug", null);
                        } catch (Exception ignore) {
                        }
                    }
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugEditTextModel("double_gap", holder -> {
            holder.setText("");
            holder.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    Long timeGap = Long.parseLong(holder.getText().trim());
                    ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setDoubleClickGapTime(timeGap);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugEditTextModel("Trigger native crash", holder -> {
            holder.setText("");
            holder.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {

                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugEditTextModel("Collect native crash", holder -> {
            holder.setText("");
            holder.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {

                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugEditTextModel("Block Time", holder -> {
            holder.setText("");
            holder.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    try {
                        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setBlockThreshold(Integer.parseInt(holder.getText()));
                    } catch (Exception e) {
                        ToastUtil.toast(e.getMessage(), Toast.LENGTH_SHORT);
                    }
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugEditTextModel("Router Manager", holder -> {
            holder.setText("");
            holder.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    try {
                        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setMagicUrl(holder.getText());
                    } catch (Exception e) {
                        ToastUtil.toast(e.getMessage(), Toast.LENGTH_SHORT);
                    }
                    ArouterManager.Companion.getInst().routeUniversal(holder.mContext, holder.getText(), null, false, null);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugEditTextModel("ScriptUpdate", holder -> {
            holder.setText("");
            holder.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    String url;
                    if (!AppUtils.isHttpUrl(url = holder.getText().trim())) {
                        ToastUtil.toast("Please input a valid patch url to continue!", Toast.LENGTH_LONG);
                        return;
                    }
                    DebugJavaScriptUtil.Companion.getInstance().doPatch(holder.mContext, url);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugEditTextModel("Language Dialog Style", holder -> {
            holder.setText("");
            holder.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    try {
                        String s = holder.getText().trim();
                        if (!TextUtils.isEmpty(s)) {
                            int digit = Integer.parseInt(s);
                            BuzzSPModel.INSTANCE.getLanguageStyle().setValue(digit);
                        }
                    } catch (Exception e) {
                    }
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugEditTextModel("mock android id last three numbers", holder -> {
            holder.setText("");
            holder.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    try {
                        ClientAbSPModel.INSTANCE.clean();
                        String s = holder.getText();
                        int value = Integer.parseInt(s.trim());
                        if (value > 0) {
                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setAndroidIdLastThreeNumber(value);
                            //清空城市选择
                        } else {
                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setAndroidIdLastThreeNumber(-1);
                        }
                    } catch (Exception e) {
                        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setAndroidIdLastThreeNumber(-1);
                    }
                }
            });
            return Unit.INSTANCE;
        }));


        otherList.add(new DebugSpinnerModel("choose city", holder -> {
            try {
                String city = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseCity();
                String language = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseLanguage();
                String tier = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseTier();
                String state = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseState();

                holder.clearSpinnerItems();

                holder.addSpinnerItem(BuzzCitiesProvider.INSTANCE.getLanguageList(), (position, selectItem) -> {
                    String language1 = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseLanguage();
                    if (position == 0 || TextUtils.equals(language1, selectItem)) {
                        return;
                    }
                    ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseLanguage(selectItem);
                    ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseTier("");
                    ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseState("");
                    ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseCity("");
                    holder.setSelection(1, 0);
                    holder.setSelection(2, 0);
                    holder.setSelection(3, 0);
                    holder.updateItems(1, BuzzCitiesProvider.INSTANCE.getTierList(selectItem));
                    holder.updateItems(2, BuzzCitiesProvider.INSTANCE.getStateList(selectItem, ""));
                    holder.updateItems(3, BuzzCitiesProvider.INSTANCE.getCityList(selectItem, "", ""));
                });

                holder.addSpinnerItem(BuzzCitiesProvider.INSTANCE.getTierList(language), (position, selectItem) -> {
                    String tier1 = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseTier();
                    if (position == 0 || TextUtils.equals(tier1, selectItem)) {
                        return;
                    }
                    ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseTier(selectItem);
                    ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseState("");
                    ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseCity("");
                    holder.setSelection(2, 0);
                    holder.setSelection(3, 0);
                    String language1 = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseLanguage();
                    holder.updateItems(2, BuzzCitiesProvider.INSTANCE.getStateList(language1, selectItem));
                    holder.updateItems(3, BuzzCitiesProvider.INSTANCE.getCityList(language1, selectItem, ""));
                });

                holder.addSpinnerItem(BuzzCitiesProvider.INSTANCE.getStateList(language, tier), (position, selectItem) -> {
                    String state1 = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseState();
                    if (position == 0 || TextUtils.equals(state1, selectItem)) {
                        return;
                    }
                    ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseState(selectItem);
                    ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseCity("");
                    holder.setSelection(3, 0);
                    String language1 = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseLanguage();
                    String tier1 = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseTier();
                    holder.updateItems(3, BuzzCitiesProvider.INSTANCE.getCityList(language1, tier1, selectItem));
                });

                holder.addSpinnerItem(BuzzCitiesProvider.INSTANCE.getCityList(language, tier, state), (position, selectItem) -> {
                    if (position == 0 || BuzzCitiesProvider.CITY.equals(selectItem)) {
                        return;
                    }
                    ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseCity(selectItem);
                    CityInfo cityEntity = BuzzCitiesProvider.INSTANCE.getCity(selectItem);
                    if (cityEntity != null) {
                        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setLatitude(String.valueOf(cityEntity.getLatitude()));
                        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setLongitude(String.valueOf(cityEntity.getLongitude()));
                    }
                });

                holder.setSelectedItem(0, language);
                holder.setSelectedItem(1, tier);
                holder.setSelectedItem(2, state);
                holder.setSelectedItem(3, city);
            } catch (Exception e) {
            }
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugSpinnerModel("Preload Original Image Threshold", holder -> {
            holder.clearSpinnerItems();
            int threshold = ClaymoreServiceLoader.loadFirst(IDebugSettings.class).getPreloadOriginalImageThreshold();
            final List<String> thresholdOptions = new ArrayList<>();
            thresholdOptions.add("0");
            thresholdOptions.add("800");
            thresholdOptions.add("1200");
            thresholdOptions.add("1600");

            int pos;
            switch (threshold) {
                case 800:
                    pos = 1;
                    break;
                case 1200:
                    pos = 2;
                    break;
                case 1600:
                    pos = 3;
                    break;
                default:
                    pos = 0;
                    break;
            }
            holder.addSpinnerItemAndSelect(thresholdOptions, ((position, selectItem) -> {
                holder.setSelection(0, position);
                ClaymoreServiceLoader
                        .loadFirst(IDebugSettings.class)
                        .setPreloadOriginalImageThreshold(
                                Integer.parseInt(thresholdOptions.get(position)));
            }), pos);
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugSpinnerModel("Nearby Style", holder -> {
            int selectedPosition = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyStyle();
            holder.clearSpinnerItems();
            final List<String> list = new ArrayList<>();
            list.add("none");
            list.add("nearby single");
            list.add("nearby double");
            list.add("local");
            holder.addSpinnerItem(list, (position, selectItem) -> {
                ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyStyle(position);
            });
            if (selectedPosition >= 0) {
                holder.setSelectedItem(0, list.get(selectedPosition));
            }
            return Unit.INSTANCE;
        }));

        otherList.add(new DebugTextViewModel("Wake Up Times", holder -> {
            holder.mText.setText("Wake times: " + ClaymoreServiceLoader.loadFirstOrNull(IJobModel.class).getWakeTimes().getValue());
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    holder.mText.setText("Wake times: " + ClaymoreServiceLoader.loadFirstOrNull(IJobModel.class).getWakeTimes().getValue());
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("Debug with google map", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    SmartRouter.buildRoute(holder.mContext, "//buzz/debug/gmap").open();
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("TEST ANR", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    CrashTriggerConfig config =
                            new CrashTriggerConfig.Builder()
                                    .setAnrSleepTime(100 * 1000)
                                    .setShakeSensitivity(60)
                                    .create();

                    CrashTrigger.install(holder.mContext, config);
                    ToastUtil.toast("shake to test ANR", Toast.LENGTH_SHORT);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("TEST OOM", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    OOMMaker.createOOM();
                    ToastUtil.toast("oom", Toast.LENGTH_SHORT);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("TEST Finalize Timeout", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    if (ArticleBaseBuildConfig.IS_LOCAL_TEST) {
                        FinalizeTimeOutObject.testFinalize();
                    }
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("Debug VE Video Editor", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {

                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("Repost Debug", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {

                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("Native Ads Priority", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    Intent priorityIntent = new Intent(holder.mContext, AdDebugActivity.class);
                    priorityIntent.putExtra(AdDebugActivity.KEY_FRAGMENT_TYPE, AdDebugActivity.FRAGMENT_TYPE_AD_PRIORITY);
                    priorityIntent.putExtra(AdDebugActivity.KEY_AD_TYPE, NewAdDebugPriorityFragment.AD_TYPE_NATIVE);
                    holder.mContext.startActivity(priorityIntent);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("Interstitial Ads Priority", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    Intent interstitialAdPriorityIntent = new Intent(holder.mContext, AdDebugActivity.class);
                    interstitialAdPriorityIntent.putExtra(AdDebugActivity.KEY_FRAGMENT_TYPE, AdDebugActivity.FRAGMENT_TYPE_AD_PRIORITY);
                    interstitialAdPriorityIntent.putExtra(AdDebugActivity.KEY_AD_TYPE, NewAdDebugPriorityFragment.AD_TYPE_INTERSTITIAL);
                    holder.mContext.startActivity(interstitialAdPriorityIntent);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("Ads Preload Status", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    Intent preloadIntent = new Intent(holder.mContext, AdDebugActivity.class);
                    preloadIntent.putExtra(AdDebugActivity.KEY_FRAGMENT_TYPE, AdDebugActivity.FRAGMENT_TYPE_PRELOAD);
                    holder.mContext.startActivity(preloadIntent);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("Ad Style", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    Intent styleIntent = new Intent(holder.mContext, AdDebugActivity.class);
                    styleIntent.putExtra(AdDebugActivity.KEY_FRAGMENT_TYPE, AdDebugActivity.FRAGMENT_TYPE_AD_STYLE);
                    holder.mContext.startActivity(styleIntent);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("App Usage Summ", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    Intent usageIntent = new Intent(holder.mContext, AdDebugActivity.class);
                    usageIntent.putExtra(AdDebugActivity.KEY_FRAGMENT_TYPE, AdDebugActivity.FRAGMENT_TYPE_USAGE);
                    holder.mContext.startActivity(usageIntent);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("Gcm Debug", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    Intent gcmIntent = new Intent(holder.mContext, GcmDebugActivity.class);
                    holder.mContext.startActivity(gcmIntent);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("Dynamics Debug", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    Intent dynamicsIntent = new Intent(holder.mContext, DebugDynamicsActivity.class);
                    holder.mContext.startActivity(dynamicsIntent);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("TTNet Debug", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    Intent debugNet = new Intent(holder.mContext, DebugNetActivity.class);
                    holder.mContext.startActivity(debugNet);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("MediaPlayer Type", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = UIUtils.getThemedAlertDlgBuilder(holder.mContext);
                    builder.setSingleChoiceItems(R.array.media_player_type, setting.getMediaPlayerType(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setting.setMediaPlayerType(which);
                        }
                    });
                    builder.setPositiveButton(R.string.confirm, null);
                    builder.setNegativeButton(R.string.cancel, null);
                    builder.show();
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("Start Search", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {

                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("Demand Test", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    AppLogVerifyClient.init(ArticleBaseBuildConfig.APP_ID, "", true);
                    String user = AppLogVerifyClient.getUserOrNull(holder.mContext);
                    AppLogVerifyClient.beginVerifyAppLog((Activity) holder.mContext, user);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("UETool", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    if (!isShowUETools) {
                        isShowUETools = UETool.showUETMenu();
                    } else {
                        isShowUETools = false;
                        UETool.dismissUETMenu();
                    }
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("Debug Live H5", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    if (!ClaymoreServiceLoader.loadFirstOrNull(IHeloLiveManager.class).isFeatureInstalled()) {
                        ToastUtil.toast("ugc feature not installed", Toast.LENGTH_SHORT);
                        return;
                    }
                    ClaymoreServiceLoader.loadFirstOrNull(IHeloLiveManager.class).debugH5(holder.mContext);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("FeedBack", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {

                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("Enable Translation Tool", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    if (holder.mContext instanceof Activity) {
                        ((Activity) holder.mContext).finish();
                    }

                    ITranslationService service = ClaymoreServiceLoader.loadFirstOrNull(ITranslationService.class);
                    if (service != null) {
                        if (service.isMenuShown()) {
                            service.setEnableTranslationOnTextChanged(false);
                            service.dismissMenu();
                            service.destroy();
                        } else {
                            service.setEnableTranslationOnTextChanged(true);
                            service.showMenu(holder.mContext);
                            service.start();
                        }
                    }
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("start BDLocation Test", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    if (holder.mContext instanceof Activity) {
                        ((Activity) holder.mContext).startActivity(new Intent(holder.mContext, LocationTestMainActivity.class));
                    }
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("Print Apk Inject Info", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    String apkInjectInfo = ApkInjectWrapper.INSTANCE.getApplicationInjectInfo(BaseApplication.getInst());
                    if (apkInjectInfo == null) {
                        apkInjectInfo = "";
                    }
                    AlertDialog alertDialog = new AlertDialog.Builder(holder.mContext)
                            .setMessage(apkInjectInfo)
                            .create();
                    alertDialog.show();
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("Print Apk file structure", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    if (holder.mContext instanceof Activity) {
                        holder.mContext.startActivity(new Intent(holder.mContext, FileStructureTestActivity.class));
                    }
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("ImmortalMediaDownload", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    if (holder.mContext instanceof Activity) {
                        SmartRouter.buildRoute(holder.mContext, "//buzz/mediadownload_act").open();
                    }
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("enter IM setting page", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    SmartRouter.buildRoute(holder.mContext, "//buzz/im/debug").open();
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("Little App Test", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    ClaymoreServiceLoader.loadFirstOrNull(IAppBrandService.class).openDebug(holder.mContext);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("QR Code H5 Jumper", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.mContext, QrCodeScannerActivity.class);
                    holder.mContext.startActivity(intent);
                }
            });
            return Unit.INSTANCE;
        }));
        otherList.add(new DebugTextViewModel("bind paytm", holder -> {
            holder.setOnClickListener(new View.OnClickListener() {
                IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

                @Override
                public void onClick(View v) {
                    ClaymoreServiceLoader.loadFirstOrNull(IPolarisDialogService.class).showBindPayTmDialog();
                }
            });
            return Unit.INSTANCE;
        }));

        return otherList;
    }
}

