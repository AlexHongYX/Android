//package com.ss.android.application.app.debug;
//
//import android.app.Activity;
//import android.app.ActivityManager;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.webkit.CookieManager;
//import android.webkit.CookieSyncManager;
//import android.widget.CompoundButton;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AlertDialog;
//
//import com.bytedance.ad.symphony.AdSymphony;
//import com.bytedance.crashtrigger.CrashTrigger;
//import com.bytedance.crashtrigger.config.CrashTriggerConfig;
//import com.bytedance.i18n.appbrandservice.IAppBrandService;
//import com.bytedance.i18n.business.debug.R;
//import com.bytedance.i18n.business.framework.legacy.service.appconfig.IAppConfig;
//import com.bytedance.i18n.business.framework.legacy.service.applog.IAppLogV3;
//import com.bytedance.i18n.business.framework.legacy.service.constants.AbsConstants;
//import com.bytedance.i18n.business.framework.legacy.service.constants.ArticleBaseBuildConfig;
//import com.bytedance.i18n.business.framework.legacy.service.network.netclient.ITTNetModuleInitHelper;
//import com.bytedance.i18n.business.framework.legacy.service.setting.IDebugSettings;
//import com.bytedance.i18n.business.framework.push.service.IJobModel;
//import com.bytedance.i18n.business.mine.service.IMineService;
//import com.bytedance.i18n.claymore.ClaymoreServiceLoader;
//import com.bytedance.memory.test.OOMMaker;
//import com.bytedance.router.SmartRouter;
//import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
//import com.google.android.gms.common.api.Result;
//import com.google.android.gms.common.api.ResultCallback;
//import com.ss.android.application.app.core.BaseApplication;
//import com.ss.android.application.app.debug.ad.AdDebugActivity;
//import com.ss.android.application.app.debug.ad.NewAdDebugPriorityFragment;
//import com.ss.android.application.app.debug.data.BuzzCitiesProvider;
//import com.ss.android.application.app.debug.data.CityInfo;
//import com.ss.android.application.app.debug.dynamics.DebugDynamicsActivity;
//import com.ss.android.application.app.debug.file.FileStructureTestActivity;
//import com.ss.android.application.app.debug.fpsmeter.FpsMeterView;
//import com.ss.android.application.app.debug.gcm.GcmDebugActivity;
//import com.ss.android.application.app.debug.location.LocationTestMainActivity;
//import com.ss.android.application.app.debug.net.DebugNetActivity;
//import com.ss.android.application.app.debug.qrscanner.QrCodeScannerActivity;
//import com.ss.android.application.app.debug.sharedpreference.DemoMultiProcessPrefModel;
//import com.ss.android.application.app.debug.util.DebugUIUtil;
//import com.ss.android.application.app.feedback.FeedbackServiceManager;
//import com.ss.android.application.app.feedback.constants.FeedbackConstants;
//import com.ss.android.application.social.account.client.google.GoogleAccountClient;
//import com.ss.android.buzz.BuzzSPModel;
//import com.ss.android.buzz.arouter.ArouterManager;
//import com.ss.android.buzz.live.HeloLiveService;
//import com.ss.android.buzz.live.IHeloLiveManager;
//import com.ss.android.buzz.live.model.IHeloLiveModel;
//import com.ss.android.buzz.ug.polaris.service.IPolarisDialogService;
//import com.ss.android.buzz.util.FinalizeTimeOutObject;
//import com.ss.android.buzz.util.clientab.ClientAbSPModel;
//import com.ss.android.cricket.debug.CricketDebugConfig;
//import com.ss.android.framework.imageloader.glideloader.datafetcher.TTNetUrlListFetcher;
//import com.ss.android.framework.sharedpref.MultiProcessSharedPrefModel;
//import com.ss.android.framework.statistic.asyncevent.AsyncEvent;
//import com.ss.android.i18n.apkinjector.impl.v2.ApkInjectWrapper;
//import com.ss.android.module.verify_applog.AppLogVerifyClient;
//import com.ss.android.network.utils.CookieManagerCompat;
//import com.ss.android.uilib.toast.ToastUtil;
//import com.ss.android.uilib.utils.UIUtils;
//import com.ss.android.utils.app.AppUtils;
//import com.ss.android.utils.debug.DebugConsole;
//import com.ss.android.utils.kit.Logger;
//import com.ss.android.widget.translation.ITranslationService;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import me.ele.uetool.UETool;
//import me.leolin.shortcutbadger.ShortcutBadger;
//
//public class OtherAdapter extends DebugStandardAdapter {
//
//    private GoogleAccountClient mGoogleAccountClient;
//
//    public OtherAdapter(List<DebugDataModel> list,Context context) {
//        super(list, context);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    @Override
//    public void bindCheckBoxViewHolder(@NotNull CheckBoxViewHolder holder, @NotNull DebugDataModel item) {
//        final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
//        switch (item.getText()) {
//            case "Auto Clean Delay 5-10s":
//                holder.mCheckbox.setChecked(ClaymoreServiceLoader.loadFirstOrNull(IJobModel.class).getCleanCacheInShortDelay().getValue());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        ClaymoreServiceLoader.loadFirstOrNull(IJobModel.class).getCleanCacheInShortDelay().setValue(isChecked);
//                    }
//                });
//                break;
//            case "Google Login":
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        if (isChecked) {
//                            try {
//                                mGoogleAccountClient.authorize();
//                            } catch (GooglePlayServicesNotAvailableException e) {
//                                Logger.d(TAG, "try signIn", e);
//                            }
//                        } else {
//                            try {
//                                mGoogleAccountClient.signOut(new ResultCallback() {
//                                    @Override
//                                    public void onResult(@NonNull Result result) {
//                                        Logger.d(GoogleAccountClient.TAG, result.getStatus().toString());
//                                    }
//                                });
//                            } catch (GooglePlayServicesNotAvailableException e) {
//                                Logger.d(TAG, "try signOut", e);
//                            }
//                        }
//                    }
//                });
//                break;
//            case "Show Article Web Type(Amp, Server TransCode or Original Web)":
//                holder.mCheckbox.setChecked(setting.isShowArticleWebTypeEnable());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setShowArticleWebStyle(isChecked);
//                        DebugUIUtil.INSTANCE.setShowArticleWebType(isChecked);
//                    }
//                });
//
//                break;
//            case "Enable Native Ads Priority":
//                holder.mCheckbox.setChecked(setting.isNativeAdPriorityEnable());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setNativeAdPriorityEnable(isChecked);
////                        AdCenter.getInstance(mContext).setDebugPriorityEnable(isChecked);
//                    }
//                });
//                break;
//            case "Enable Interstitial Ads Priority":
//                holder.mCheckbox.setChecked(setting.isNativeAdPriorityEnable());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setInterstitialAdPriorityEnable(isChecked);
//                        AdSymphony.getInstance().getInterstitialAdManager().getFillStrategyManager().setDebugPriorityEnable(isChecked);
//                    }
//                });
//                break;
//            case "Show Ad Provider Id":
//                holder.mCheckbox.setChecked(setting.showAdProviderId());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setShowAdProviderId(isChecked);
//                    }
//                });
//                break;
//            case "Use Http":
//                holder.mCheckbox.setChecked(setting.isUseHttp());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setUseHttp(isChecked);
//                        ClaymoreServiceLoader.loadFirstOrNull(ITTNetModuleInitHelper.class).setUseHttp(isChecked);
//                    }
//                });
//                break;
//            case "Enable FPS Meter":
//                holder.mCheckbox.setChecked(FpsMeterView.isShowing());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        if (isChecked) {
//                            FpsMeterView.show(mContext, 200);
//                        } else {
//                            FpsMeterView.hide();
//                        }
//                    }
//                });
//                break;
//            case "Always show login popup":
//                holder.mCheckbox.setChecked(setting.shouldAlwaysShowLoginPopup());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setAlwaysShowLoginPopup(isChecked);
//                    }
//                });
//                break;
//            case "Always get app settings":
//                holder.mCheckbox.setChecked(setting.shouldAlwaysGetAppSettings());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setAlwaysGetAppSettings(isChecked);
//                    }
//                });
//                break;
//            case "Always send sample http log":
//                holder.mCheckbox.setChecked(setting.shouldAlwaysSendSampleHttp());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setAlwaysSendSampleHttp(isChecked);
//                    }
//                });
//                break;
//            case "Fix Ok Http Proxy issue":
//                holder.mCheckbox.setChecked(setting.shouldFixOkHttpProxy());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setShouldFixOkHttpProxy(isChecked);
//                    }
//                });
//                break;
//            case "Apply MD Design on TabLayout":
//                holder.mCheckbox.setChecked(setting.shouldApplyMdDesignOnTabLayout());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setApplyMdDesignOnTabLayout(isChecked);
//                    }
//                });
//                break;
//            case "Always show video tab tip":
//                holder.mCheckbox.setChecked(setting.shouldAlwaysShowVideoTabTip());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setAlwaysShowVideoTabTip(isChecked);
//                    }
//                });
//                break;
//            case "Always show bottom tab refresh tip":
//                holder.mCheckbox.setChecked(setting.shouldAlwaysShowBottomTabRefreshTip());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setAlwaysShowBottomTabRefreshTip(isChecked);
//                    }
//                });
//                break;
//            case "Always jump to detail comment section":
//                holder.mCheckbox.setChecked(setting.shouldAlwaysJumpToComment());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setAlwaysJumpToComment(isChecked);
//                    }
//                });
//                break;
//            case "Always show pull to refresh guide":
//                holder.mCheckbox.setChecked(setting.shouldAlwaysShowPullToRefreshGuide());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setAlwaysShowPullToRefreshGuide(isChecked);
//                    }
//                });
//                break;
//            case "Always Show Video Error":
//                holder.mCheckbox.setChecked(setting.showVideoErrorContent());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setShowVideoErrorContent(isChecked);
//                    }
//                });
//                break;
//            case "Use SurfaceView":
//                holder.mCheckbox.setChecked(setting.useSurfaceViewRenderView());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setUseSurfaceViewRenderView(isChecked);
//                    }
//                });
//                break;
//            case "Show Used MediaPlayer":
//                holder.mCheckbox.setChecked(setting.showMediaPlayerUsed());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setShowMediaPlayerUsed(isChecked);
//                    }
//                });
//                break;
//            case "Test App Current Active Event":
//                holder.mCheckbox.setChecked(setting.testAppActiveEvent());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setTestAppActiveEvent(isChecked);
//                    }
//                });
//                break;
//            case "Local Push Enable Local Time Test":
//                holder.mCheckbox.setChecked(setting.testLocalPush());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setLocalPushTestEnable(isChecked);
//                    }
//                });
//                break;
//            case "Don't Bind Ad":
//                holder.mCheckbox.setChecked(setting.doNotBindAd());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setDoNotBindAd(isChecked);
//                    }
//                });
//                break;
//            case "Show Two Lines Relative News":
//                holder.mCheckbox.setChecked(setting.showTwoLineRelativeNews());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setShowTwoLineRelativeNews(isChecked);
//                    }
//                });
//                break;
//            case "Use async MediaPlayer":
//                holder.mCheckbox.setChecked(setting.useAsyncMediaPlayer());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setUseAsyncMediaPlayer(isChecked);
//                    }
//                });
//                break;
//            case "Show AddtoDebug":
//                holder.mCheckbox.setChecked(setting.showAddtoDebug());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setShowAddtoDebug(isChecked);
//                    }
//                });
//                break;
//            case "Show Feed Time":
//                break;
//            case "Swip To Next Page":
//                holder.mCheckbox.setChecked(setting.getEnableSwipeToNext());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setEnableSwipeToNext(isChecked);
//                    }
//                });
//                break;
//            case "Swip To Related Page":
//                holder.mCheckbox.setChecked(setting.getSwipeToRelated());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setSwipeToRelated(isChecked);
//                    }
//                });
//            case "Show Video Bitrate Info"://是否显示码率、分辨率信息
//                holder.mCheckbox.setChecked(setting.showVideoBitrateLayout());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setShowVideoBitrateLayout(isChecked);
//                    }
//                });
//                break;
//            case "MediaPlayer Type Switch":
//                holder.mCheckbox.setChecked(setting.useMediaPlayerDebug());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setMediaPlayerDebug(isChecked);
//                        List<DebugDataModel> list = new ArrayList<>(mItems);
////                        mItems = filterData(isChecked, DebugDataModel.SelectMediaPlayerType, list.indexOf(DebugDataModel.SelectMediaPlayerSwitch) + 1);
//                        notifyDataSetChanged();
//                    }
//                });
//                break;
//            case "Degree Youtube Leech":
//                holder.mCheckbox.setChecked(setting.degreeYoutubeLeech());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setDegreeYoutubeLeech(isChecked);
//                    }
//                });
//                break;
//            case "Http1 Only":
//                holder.mCheckbox.setChecked(setting.useHttp1Only());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setUseHttp1Only(isChecked);
//                    }
//                });
//                break;
//            case "Reset User":
//                holder.mCheckbox.setChecked(ClaymoreServiceLoader.loadFirstOrNull(IAppLogV3.class).isNewUserMode(mContext));
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        ClaymoreServiceLoader.loadFirstOrNull(IAppLogV3.class).setNewUserMode(mContext, isChecked);
//                        resetUser();
//                    }
//                });
//                break;
//            case "Always show buzz intro": {
//                holder.mCheckbox.setChecked(setting.getAlwaysShowBuzzIntro());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setAlwaysShowBuzzIntro(isChecked);
//                    }
//                });
//                break;
//            }
//            case "Show Invitation style": {
//                holder.mCheckbox.setChecked(setting.getUseInvitationStyle());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setUseInvitationStyle(isChecked);
//                    }
//                });
//                break;
//            }
//            case "Buzz always select language": {
//                holder.mCheckbox.setChecked(setting.getBuzzAlwaysSelectLanguage());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setBuzzAlwaysSelectLanguage(isChecked);
//                    }
//                });
//                break;
//            }
//            case "Show Debug Console": {
//                holder.mCheckbox.setChecked(DebugConsole.isShown());
//                Logger.d("Debug Console", "Debug Console setChecked " + DebugConsole.isShown());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        if (isChecked) {
//                            DebugConsole.show(mContext);
//                            DebugConsole.log("Debug Console On");
//                            Logger.d("Debug Console", "Debug Console On");
//                        } else {
//                            DebugConsole.hide();
//                            DebugConsole.log("Debug Console Off");
//                            Logger.d("Debug Console", "Debug Console Off");
//                        }
//                    }
//                });
//                break;
//            }
//            case "Enable Screen Shot for BuzzShare": {
//                holder.mCheckbox.setChecked(BuzzDebugModel.INSTANCE.getEnableScreenShot().getValue());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        BuzzDebugModel.INSTANCE.getEnableScreenShot().setValue(isChecked);
//                    }
//                });
//                break;
//            }
//            case "Disable AppLog Encryption": {
//                holder.mCheckbox.setChecked(setting.getDisableAppLogEncryption());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setDisableAppLogEncryption(isChecked);
//                    }
//                });
//                break;
//            }
//
//            case "Always login as new user": {
//                holder.mCheckbox.setChecked(setting.getAlwaysLoginAsNewUser());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setAlwaysLoginAsNewUser(isChecked);
//                    }
//                });
//                break;
//            }
//            case "Use new login style": {
//                holder.mCheckbox.setChecked(setting.getUseNewLoginStyle());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setUseNewLoginStyle(isChecked);
//                    }
//                });
//                break;
//            }
//
//            case "Video Use MediaLoader To PreLoad": {
//                holder.mCheckbox.setChecked(setting.getVideoUrlWithDataLoader());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                    setting.setVideoUrlWithDataLoader(isChecked);
//                });
//                break;
//            }
//
//            case "Enable Video Preload Radical": {
//                holder.mCheckbox.setChecked(setting.getEnablePreloadVideoRadical());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                    setting.setEnablePreloadVideoRadical(isChecked);
//                });
//                break;
//            }
//
//            case "Enable Video Preload": {
//                holder.mCheckbox.setChecked(setting.getEnableVideoPreload());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setEnableVideoPreload(isChecked);
//                    }
//                });
//                break;
//            }
//
//            case "TTVideo Use TTNet": {
//                holder.mCheckbox.setChecked(setting.getTTVideoUseTTNet());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setTTVideoUseTTNet(isChecked);
//                    }
//                });
//                break;
//            }
//
//            case "ImageLoader Use TTNet": {
//                holder.mCheckbox.setChecked(setting.getImageLoderUseTTnet());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setImageLoderUseTTnet(isChecked);
//                    }
//                });
//                break;
//            }
//
//            case "Enable Image Request Debug": {
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        TTNetUrlListFetcher.Companion.setDEBUG(isChecked);
//                    }
//                });
//                break;
//            }
//            case "enable async inflate": {
//                holder.mCheckbox.setChecked(setting.enableAsyncInflate());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) ->
//                        setting.setAsyncInflateEnable(isChecked));
//                break;
//            }
//            case "enable x2c": {
//                holder.mCheckbox.setChecked(setting.enableX2C());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) ->
//                        setting.setX2cEnable(isChecked));
//                break;
//            }
//            case "Enable immersive": {
//                holder.mCheckbox.setChecked(setting.getImmersiveDebugEnable());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) ->
//                        setting.setImmersiveDebugEnable(isChecked));
//                break;
//            }
//            case "Enable ImageView Debug":{
//                holder.mCheckbox.setChecked(setting.getEnableImageDebug());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setEnableImageDebug(isChecked);
//                    }
//                });
//                break;
//            }
//            case "Enable ImageView Reuse": {
//                holder.mCheckbox.setChecked(setting.getEnableImageReuse());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setEnableImageReuse(isChecked);
//                    }
//                });
//                break;
//            }
//            case "Enable Cricket Always Pull":{
//                holder.mCheckbox.setChecked(CricketDebugConfig.INSTANCE.getAlwaysPull());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        CricketDebugConfig.INSTANCE.setAlwaysPull(isChecked);
//                    }
//                });
//                break;
//            }
//            case "EnableNewImmersiveVideoCard": {
//                holder.mCheckbox.setChecked(BuzzSPModel.INSTANCE.isUseImmersiveNewVideoCard().getValue());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                    BuzzSPModel.INSTANCE.isUseImmersiveNewVideoCard().setValue(isChecked);
//                });
//                break;
//            }
//            case "CrazyVideoDownload": {
//                holder.mCheckbox.setChecked(setting.getCrazyVideoDownload());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                    setting.setCrazyVideoDownload(isChecked);
//                });
//                break;
//            }
//            case "EnableMainFeedVideoCard": {
//                holder.mCheckbox.setChecked(BuzzSPModel.INSTANCE.isUseMainFeedNewVideoCard().getValue());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                    BuzzSPModel.INSTANCE.isUseMainFeedNewVideoCard().setValue(isChecked);
//                });
//                break;
//            }
//            case "immersive video vertical": {
//                holder.mCheckbox.setChecked(BuzzSPModel.INSTANCE.isImmersiveVideoDirectVertical().getValue());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                    BuzzSPModel.INSTANCE.isImmersiveVideoDirectVertical().setValue(isChecked);
//                });
//                break;
//            }
//            case "EnableNewMediaViewer": {
//                holder.mCheckbox.setChecked(BuzzSPModel.INSTANCE.isUseNewMediaViewer().getValue());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                    BuzzSPModel.INSTANCE.isUseNewMediaViewer().setValue(isChecked);
//                });
//                break;
//            }
//            case "enable live permission":
//                MultiProcessSharedPrefModel.BooleanProperty canStartLive = ClaymoreServiceLoader.loadFirstOrNull(IHeloLiveModel.class).getCanStartLive();
//                holder.mCheckbox.setChecked(canStartLive.getValue());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                    canStartLive.setValue(isChecked);
//                });
//                break;
//            case "Show live guide dialog":
//                MultiProcessSharedPrefModel.BooleanProperty showed = ClaymoreServiceLoader.loadFirstOrNull(IHeloLiveModel.class).getShowedEntryGuideDialog();
//                holder.mCheckbox.setChecked(!showed.getValue());
//                holder.setOnCheckedChangeListener(((buttonView, isChecked) -> {
//                    showed.setValue(!isChecked);
//                }));
//                break;
//            case "show live debug info":
//                MultiProcessSharedPrefModel.BooleanProperty show = ClaymoreServiceLoader.loadFirstOrNull(IHeloLiveModel.class).getShowDebugInfo();
//                holder.mCheckbox.setChecked(show.getValue());
//                holder.setOnCheckedChangeListener(((buttonView, isChecked) -> {
//                    show.setValue(isChecked);
//                }));
//                break;
//            case "Use Voice Search":
//                holder.mCheckbox.setChecked(setting.getUseVoiceSearch());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setUseVoiceSearch(isChecked);
//                    }
//                });
//                break;
//            case "enable multi language":
//                holder.mCheckbox.setChecked(setting.getEnableMultiLanguage());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setEnableMultiLanguage(isChecked);
//                    }
//                });
//                break;
//            case "Enable Image Translation":
//                holder.mCheckbox.setChecked(setting.getEnableImageTranslate());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) -> setting.setEnableImageTranslate(isChecked));
//                break;
//            case "Enable Stream Protobuf":
//                holder.mCheckbox.setChecked(setting.getEnableStreamProtobuf());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                    setting.setEnableStreamProtobuf(isChecked);
//
//                });
//                break;
//            case "Enable Backup DNS":
//                holder.mCheckbox.setChecked(setting.getEnableBackupDNS());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                    setting.setEnableBackupDNS(isChecked);
//
//                });
//                break;
//            case "Disable Configurable Channel":
//                holder.mCheckbox.setChecked(setting.getDisableConfigurableChannel());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                    setting.setDisableConfigurableChannel(isChecked);
//                });
//                break;
//            case "Disable Stream Preload":
//                holder.mCheckbox.setChecked(setting.getDisableStreamPreload());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) -> setting.setDisableStreamPreload(isChecked));
//            default:
//                break;
//        }
//    }
//    private void resetUser() {
////        DataCleanManager.cleanApplicationData(mContext);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)).clearApplicationUserData();
//            return;
//        }
//
////        SharedPreferences sp = mContext.getSharedPreferences(AppLogConstants.getDeviceParamsSpName(), Context.MODE_PRIVATE);
////        sp.edit().clear()
////                .putString(KEY_OPENUDID, UDidUtils.createRandomUDID())
////                .putString(KEY_UDID, UDidUtils.createRandomUDID())
////                .putString(KEY_CLIENTUDID, UUID.randomUUID().toString())
////                .apply();
////        SharedPreferences sp2 = mContext.getSharedPreferences(
////                SP_APPLOG_STATS, Context.MODE_PRIVATE);
////        sp2.edit().clear().apply();
//
//        // must clear cookies
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            try {
//                CookieManagerCompat.getInstance().removeAllCookies(null);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(mContext.getApplicationContext());
//            cookieSyncManager.startSync();
//            CookieManager.getInstance().removeAllCookie();
//            cookieSyncManager.stopSync();
//        }
//
//        ToastUtil.toast("Restart app to take effect", Toast.LENGTH_SHORT);
//
//        // restart app
//        // method 1
////      ((Activity)mContext).finish();
////      Intent intent = AppUtils.getLaunchIntentForPackage(mContext, BuildConfig.PKG_NAME);
////      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
////      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////      mContext.startActivity(intent);
//
//        // method 2
////        Intent startIntent = new Intent(mContext,SplashActivity.class);
////        PackageManager pm = mContext.getPackageManager();
////        if(pm!=null)
////            startIntent = pm.getLaunchIntentForPackage(mContext.getPackageName());
////        AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
////        PendingIntent restartIntent = PendingIntent.getActivity(
////                mContext.getApplicationContext(), 0, startIntent, PendingIntent.FLAG_CANCEL_CURRENT);
////        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000, restartIntent);
////
////        System.exit(0);
//        ClaymoreServiceLoader.loadFirstOrNull(IMineService.class).getISwitchLanguageHelperUtil().goLaunchActivity(mContext, true, false);
//    }
//
//    @Override
//    public void bindEditTextViewHolder(@NotNull EditTextViewHolder holder, @NotNull DebugDataModel item) {
//        switch (item.getText()) {
//            case "set longitude":
//                holder.setText(ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getLongitude());
//                break;
//            case "set latitude":
//                holder.setText(ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getLatitude());
//                break;
//            default:
//                holder.setText("");
//        }
//        holder.setOnClickListener(new View.OnClickListener() {
//            @SuppressWarnings("deprecation")
//            @Override
//            public void onClick(View v) {
//                switch (item.getText()) {
//                    case "Short Cut Badge":
//                        try {
//                            ShortcutBadger.applyCount(mContext, Integer.parseInt(holder.getText()));
//                        } catch (Exception e) {
//
//                        }
//                        break;
//                    case "Async Event":
////                        try {
//                        AsyncEvent.test(mContext);
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
//                        break;
//                    case "Test Preference Property":
////                        DemoMultiProcessPrefModel.sInstance.test();
////                        DemoPreferenceModel.sInstance.testDemo(10000);
////                        DemoMultiProcessPrefModel.sInstance.testMultiProcess();
//                        DemoMultiProcessPrefModel.sInstance.test();
//                        break;
//                    case "Update App Config":
//                        ClaymoreServiceLoader.loadFirstOrNull(IAppConfig.class).testRefreshAppConfig();
////                        TTNetModuleManager.INSTANCE.doRefresh(true);
//                        break;
//                    case "Push Detail Back Strategy":
//                        try {
//                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setPushDetailBackStrategy(Integer.valueOf(holder.getText()));
//                            ToastUtil.toast("Using push strategy: " + holder.getText(), Toast.LENGTH_SHORT);
//                        } catch (Exception e) {
//                            ToastUtil.toast( e.getMessage(), Toast.LENGTH_SHORT);
//                        }
//                        break;
//                    case "watch_live":
//                        String id = holder.getText().trim();
//                        HeloLiveService service = ClaymoreServiceLoader.loadFirstOrNull(HeloLiveService.class);
//                        Context context = mContext;
//                        if (service != null && context != null) {
//                            try {
//                                long roomId = Long.parseLong(id);
//                                service.joinLive(context, roomId, null, null, "debug", "debug", null);
//                            } catch (Exception ignore) {
//                            }
//                        }
//                        break;
//                    case "double_gap":
//                        Long timeGap = Long.parseLong(holder.getText().trim());
//                        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setDoubleClickGapTime(timeGap);
//                        break;
//                    case "Trigger native crash":
//                        break;
//                    case "Collect native crash":
//                        break;
//                    case "Block Time":
//                        try {
//                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setBlockThreshold(Integer.parseInt(holder.getText()));
//                        } catch (Exception e) {
//                            ToastUtil.toast(e.getMessage(), Toast.LENGTH_SHORT);
//                        }
//                        break;
//                    case "Router Manager":
//                        try {
//                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setMagicUrl(holder.getText());
//                        } catch (Exception e) {
//                            ToastUtil.toast(e.getMessage(), Toast.LENGTH_SHORT);
//                        }
//                        ArouterManager.Companion.getInst().routeUniversal(mContext, holder.getText(), null, false, null);
//                        break;
//                    case "ScriptUpdate":
//                        String url;
//                        if (!AppUtils.isHttpUrl(url = holder.getText().trim())) {
//                            ToastUtil.toast("Please input a valid patch url to continue!", Toast.LENGTH_LONG);
//                            return;
//                        }
//                        DebugJavaScriptUtil.Companion.getInstance().doPatch(mContext, url);
//                        break;
//                    case "Language Dialog Style":
//                        try {
//                            String s = holder.getText().trim();
//                            if (!TextUtils.isEmpty(s)) {
//                                int digit = Integer.parseInt(s);
//                                BuzzSPModel.INSTANCE.getLanguageStyle().setValue(digit);
//                            }
//                        } catch (Exception e) {
//                        }
//                        break;
//
//                    case "set longitude":
//                        try {
//                            String s = holder.getText().trim();
//                            double value = Double.valueOf(s);
//                            if (value > 0) {
//                                ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setLongitude(s.trim());
//                                //清空城市选择
//                                ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseLanguage("");
//                                ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseTier("");
//                                ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseState("");
//                                ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseCity("");
//
//                            }
//                        } catch (Exception e) {
//                        }
//                        break;
//                    case "set latitude":
//                        try {
//                            String s = holder.getText().trim();
//                            double value = Double.valueOf(s.trim());
//                            if (value > 0) {
//                                ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setLatitude(s.trim());
//                                //清空城市选择
//                                ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseLanguage("");
//                                ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseTier("");
//                                ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseState("");
//                                ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseCity("");
//                            }
//                        } catch (Exception e) {
//                        }
//                        break;
//                    case "mock android id last three numbers":
//                        try {
//                            ClientAbSPModel.INSTANCE.clean();
//                            String s = holder.getText();
//                            int value = Integer.parseInt(s.trim());
//                            if (value > 0) {
//                                ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setAndroidIdLastThreeNumber(value);
//                                //清空城市选择
//                            } else {
//                                ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setAndroidIdLastThreeNumber(-1);
//                            }
//                        } catch (Exception e) {
//                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setAndroidIdLastThreeNumber(-1);
//                        }
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//    }
//
//    @Override
//    public void bindSpinnerViewHolder(@NotNull SpinnerViewHolder holder, @NotNull DebugDataModel item) {
//        switch (item.getText()) {
//            case "choose city":
//                try {
//                    String city = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseCity();
//                    String language = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseLanguage();
//                    String tier = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseTier();
//                    String state = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseState();
//
//                    holder.clearSpinnerItems();
//
//                    holder.addSpinnerItem(BuzzCitiesProvider.INSTANCE.getLanguageList(), (position, selectItem) -> {
//                        String language1 = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseLanguage();
//                        if (position == 0 || TextUtils.equals(language1, selectItem)) {
//                            return;
//                        }
//                        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseLanguage(selectItem);
//                        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseTier("");
//                        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseState("");
//                        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseCity("");
//                        holder.setSelection(1, 0);
//                        holder.setSelection(2, 0);
//                        holder.setSelection(3, 0);
//                        holder.updateItems(1, BuzzCitiesProvider.INSTANCE.getTierList(selectItem));
//                        holder.updateItems(2, BuzzCitiesProvider.INSTANCE.getStateList(selectItem, ""));
//                        holder.updateItems(3, BuzzCitiesProvider.INSTANCE.getCityList(selectItem, "", ""));
//                    });
//
//                    holder.addSpinnerItem(BuzzCitiesProvider.INSTANCE.getTierList(language), (position, selectItem) -> {
//                        String tier1 = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseTier();
//                        if (position == 0 || TextUtils.equals(tier1, selectItem)) {
//                            return;
//                        }
//                        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseTier(selectItem);
//                        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseState("");
//                        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseCity("");
//                        holder.setSelection(2, 0);
//                        holder.setSelection(3, 0);
//                        String language1 = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseLanguage();
//                        holder.updateItems(2, BuzzCitiesProvider.INSTANCE.getStateList(language1, selectItem));
//                        holder.updateItems(3, BuzzCitiesProvider.INSTANCE.getCityList(language1, selectItem, ""));
//                    });
//
//                    holder.addSpinnerItem(BuzzCitiesProvider.INSTANCE.getStateList(language, tier), (position, selectItem) -> {
//                        String state1 = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseState();
//                        if (position == 0 || TextUtils.equals(state1, selectItem)) {
//                            return;
//                        }
//                        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseState(selectItem);
//                        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseCity("");
//                        holder.setSelection(3, 0);
//                        String language1 = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseLanguage();
//                        String tier1 = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyChooseTier();
//                        holder.updateItems(3, BuzzCitiesProvider.INSTANCE.getCityList(language1, tier1, selectItem));
//                    });
//
//                    holder.addSpinnerItem(BuzzCitiesProvider.INSTANCE.getCityList(language, tier, state), (position, selectItem) -> {
//                        if (position == 0 || BuzzCitiesProvider.CITY.equals(selectItem)) {
//                            return;
//                        }
//                        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyChooseCity(selectItem);
//                        CityInfo cityEntity = BuzzCitiesProvider.INSTANCE.getCity(selectItem);
//                        if (cityEntity != null) {
//                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setLatitude(String.valueOf(cityEntity.getLatitude()));
//                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setLongitude(String.valueOf(cityEntity.getLongitude()));
//                        }
//                    });
//
//                    holder.setSelectedItem(0, language);
//                    holder.setSelectedItem(1, tier);
//                    holder.setSelectedItem(2, state);
//                    holder.setSelectedItem(3, city);
//                } catch (Exception e) {
//                }
//                break;
//
//            case "Preload Original Image Threshold":
//                holder.clearSpinnerItems();
//                int threshold = ClaymoreServiceLoader.loadFirst(IDebugSettings.class).getPreloadOriginalImageThreshold();
//                final List<String> thresholdOptions = new ArrayList<>();
//                thresholdOptions.add("0");
//                thresholdOptions.add("800");
//                thresholdOptions.add("1200");
//                thresholdOptions.add("1600");
//
//                int pos;
//                switch (threshold) {
//                    case 800:
//                        pos = 1;
//                        break;
//                    case 1200:
//                        pos = 2;
//                        break;
//                    case 1600:
//                        pos = 3;
//                        break;
//                    default:
//                        pos = 0;
//                        break;
//                }
//                holder.addSpinnerItemAndSelect(thresholdOptions, ((position, selectItem) -> {
//                    holder.setSelection(0, position);
//                    ClaymoreServiceLoader
//                            .loadFirst(IDebugSettings.class)
//                            .setPreloadOriginalImageThreshold(
//                                    Integer.parseInt(thresholdOptions.get(position)));
//                }), pos);
//
//                break;
//
//            case "Nearby Style":
//                int selectedPosition = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getNearbyStyle();
//                holder.clearSpinnerItems();
//                final List<String> list = new ArrayList<>();
//                list.add("none");
//                list.add("nearby single");
//                list.add("nearby double");
//                list.add("local");
//                holder.addSpinnerItem(list, (position, selectItem) -> {
//                    ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setNearbyStyle(position);
//                });
//                if (selectedPosition >= 0) {
//                    holder.setSelectedItem(0, list.get(selectedPosition));
//                }
//                break;
//        }
//    }
//
//    /**
//     * 配合UETool使用
//     */
//    private boolean isShowUETools = false;
//    @Override
//    public void bindTextViewViewHolder(@NotNull TextViewViewHolder holder, @NotNull DebugDataModel item) {
//        if (item.getText() == "Wake Up Times") {
//            holder.mText.setText("Wake times: " + ClaymoreServiceLoader.loadFirstOrNull(IJobModel.class).getWakeTimes().getValue());
//        }
//        holder.setOnClickListener(new View.OnClickListener() {
//            IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
//
//            @Override
//            public void onClick(View v) {
//                switch (item.getText()) {
//                    case "Debug with google map":
//                        SmartRouter.buildRoute(mContext,"//buzz/debug/gmap").open();
//                        break;
//                    case "TEST ANR":
//                        CrashTriggerConfig config =
//                                new CrashTriggerConfig.Builder()
//                                        .setAnrSleepTime(100 * 1000)
//                                        .setShakeSensitivity(60)
//                                        .create();
//
//                        CrashTrigger.install(mContext, config);
//                        ToastUtil.toast("shake to test ANR", Toast.LENGTH_SHORT);
//                    case "TEST OOM":
//                        OOMMaker.createOOM();
//                        ToastUtil.toast("oom", Toast.LENGTH_SHORT);
//                    case "TEST Finalize Timeout":
//                        if (ArticleBaseBuildConfig.IS_LOCAL_TEST) {
//                            FinalizeTimeOutObject.testFinalize();
//                        }
//                        break;
//                    case "Debug VE Video Editor":
//                    case "Repost Debug":
//
//                        break;
//                    case "Wake Up Times":
//                        holder.mText.setText("Wake times: " + ClaymoreServiceLoader.loadFirstOrNull(IJobModel.class).getWakeTimes().getValue());
//                        break;
//                    case "Native Ads Priority":
//                        Intent priorityIntent = new Intent(mContext, AdDebugActivity.class);
//                        priorityIntent.putExtra(AdDebugActivity.KEY_FRAGMENT_TYPE, AdDebugActivity.FRAGMENT_TYPE_AD_PRIORITY);
//                        priorityIntent.putExtra(AdDebugActivity.KEY_AD_TYPE, NewAdDebugPriorityFragment.AD_TYPE_NATIVE);
//                        mContext.startActivity(priorityIntent);
//                        break;
//                    case "Interstitial Ads Priority":
//                        Intent interstitialAdPriorityIntent = new Intent(mContext, AdDebugActivity.class);
//                        interstitialAdPriorityIntent.putExtra(AdDebugActivity.KEY_FRAGMENT_TYPE, AdDebugActivity.FRAGMENT_TYPE_AD_PRIORITY);
//                        interstitialAdPriorityIntent.putExtra(AdDebugActivity.KEY_AD_TYPE, NewAdDebugPriorityFragment.AD_TYPE_INTERSTITIAL);
//                        mContext.startActivity(interstitialAdPriorityIntent);
//                        break;
//                    case "Ads Preload Status":
//                        Intent preloadIntent = new Intent(mContext, AdDebugActivity.class);
//                        preloadIntent.putExtra(AdDebugActivity.KEY_FRAGMENT_TYPE, AdDebugActivity.FRAGMENT_TYPE_PRELOAD);
//                        mContext.startActivity(preloadIntent);
//                        break;
//                    case "Ad Style":
//                        Intent styleIntent = new Intent(mContext, AdDebugActivity.class);
//                        styleIntent.putExtra(AdDebugActivity.KEY_FRAGMENT_TYPE, AdDebugActivity.FRAGMENT_TYPE_AD_STYLE);
//                        mContext.startActivity(styleIntent);
//                        break;
//                    case "App Usage Summ":
//                        Intent usageIntent = new Intent(mContext, AdDebugActivity.class);
//                        usageIntent.putExtra(AdDebugActivity.KEY_FRAGMENT_TYPE, AdDebugActivity.FRAGMENT_TYPE_USAGE);
//                        mContext.startActivity(usageIntent);
//                        break;
//                    case "Gcm Debug":
//                        Intent gcmIntent = new Intent(mContext, GcmDebugActivity.class);
//                        mContext.startActivity(gcmIntent);
//                        break;
//                    case "Dynamics Debug":
//                        Intent dynamicsIntent = new Intent(mContext, DebugDynamicsActivity.class);
//                        mContext.startActivity(dynamicsIntent);
//                        break;
//
//                    case "TTNet Debug":
//                        Intent debugNet = new Intent(mContext, DebugNetActivity.class);
//                        mContext.startActivity(debugNet);
//                        break;
//                    case "MediaPlayer Type":
//                        AlertDialog.Builder builder = UIUtils.getThemedAlertDlgBuilder(mContext);
//                        builder.setSingleChoiceItems(R.array.media_player_type, setting.getMediaPlayerType(), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                setting.setMediaPlayerType(which);
//                            }
//                        });
//                        builder.setPositiveButton(R.string.confirm, null);
//                        builder.setNegativeButton(R.string.cancel, null);
//                        builder.show();
//                        break;
//                    case "Start Search": {
//                        break;
//                    }
//                    case "Demand Test": {
//                        AppLogVerifyClient.init(ArticleBaseBuildConfig.APP_ID, "", true);
//                        String user = AppLogVerifyClient.getUserOrNull(mContext);
//                        AppLogVerifyClient.beginVerifyAppLog((Activity) mContext, user);
//                        break;
//                    }
//
//                    case "UETool": {
//                        if (!isShowUETools) {
//                            isShowUETools = UETool.showUETMenu();
//                        } else {
//                            isShowUETools = false;
//                            UETool.dismissUETMenu();
//                        }
//                        break;
//                    }
//                    case "Debug Live H5":
//                        if (!ClaymoreServiceLoader.loadFirstOrNull(IHeloLiveManager.class).isFeatureInstalled()) {
//                            ToastUtil.toast("ugc feature not installed", Toast.LENGTH_SHORT);
//                            return;
//                        }
//                        ClaymoreServiceLoader.loadFirstOrNull(IHeloLiveManager.class).debugH5(mContext);
//                        break;
//                    case "FeedBack": {
//                        Bundle bundle = new Bundle();
//                        bundle.putBoolean(FeedbackConstants.BUNDLE_HAS_NEW_FEEDBACK, false);
//                        bundle.putString(FeedbackConstants.KEY_APPKEY, AbsConstants.FEEDBACK_APPKEY);
//                        bundle.putBoolean(FeedbackConstants.BUNDLE_MY_OPTION_ONLY, true);
//                        FeedbackServiceManager.getFeedBackActionImpl().jumpToFeedBackPage(mContext, bundle);
//                        break;
//                    }
//                    case "Enable Translation Tool": {
//                        if (mContext instanceof Activity) {
//                            ((Activity) mContext).finish();
//                        }
//
//                        ITranslationService service = ClaymoreServiceLoader.loadFirstOrNull(ITranslationService.class);
//                        if (service != null) {
//                            if (service.isMenuShown()) {
//                                service.setEnableTranslationOnTextChanged(false);
//                                service.dismissMenu();
//                                service.destroy();
//                            } else {
//                                service.setEnableTranslationOnTextChanged(true);
//                                service.showMenu(mContext);
//                                service.start();
//                            }
//                        }
//                        break;
//                    }
//                    case "start BDLocation Test": {
//                        if (mContext instanceof Activity) {
//                            ((Activity) mContext).startActivity(new Intent(mContext, LocationTestMainActivity.class));
//                        }
//                        break;
//                    }
//                    case "Print Apk Inject Info": {
//                        String apkInjectInfo = ApkInjectWrapper.INSTANCE.getApplicationInjectInfo(BaseApplication.getInst());
//                        if (apkInjectInfo == null) {
//                            apkInjectInfo = "";
//                        }
//                        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
//                                .setMessage(apkInjectInfo)
//                                .create();
//                        alertDialog.show();
//                        break;
//                    }
//                    case "Print Apk file structure": {
//                        if (mContext instanceof Activity) {
//                            mContext.startActivity(new Intent(mContext, FileStructureTestActivity.class));
//                        }
//                        break;
//                    }
//                    case "ImmortalMediaDownload": {
//                        if (mContext instanceof Activity) {
//                            SmartRouter.buildRoute(mContext,"//buzz/mediadownload_act").open();
//                        }
//                        break;
//                    }
//                    case "enter IM setting page":{
//                        SmartRouter.buildRoute(mContext,"//buzz/im/debug").open();
//                    }
//                    case "Little App Test": {
//                        ClaymoreServiceLoader.loadFirstOrNull(IAppBrandService.class).openDebug(mContext);
//                        break;
//                    }
//                    case "QR Code H5 Jumper": {
//                        Intent intent = new Intent(mContext, QrCodeScannerActivity.class);
//                        mContext.startActivity(intent);
//                        break;
//                    }
//                    case "bind paytm":{
//                        ClaymoreServiceLoader.loadFirstOrNull(IPolarisDialogService.class).showBindPayTmDialog();
//                        break;
//                    }
//                    default:
//                        break;
//                }
//            }
//        });
//    }
//
//
//}
