package com.ss.android.application.app.debug;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
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
import com.ss.android.utils.kit.Logger;
import com.ss.android.widget.translation.ITranslationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;
import me.ele.uetool.UETool;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by yihuaqi on 10/26/16.
 */
@Deprecated
public class DebugAdapter extends RecyclerView.Adapter<DebugAdapter.ViewHolder> {

    public static final String TAG = DebugAdapter.class.getSimpleName();
    protected final Context mContext;
    // TODO:
    private GoogleAccountClient mGoogleAccountClient;

    private boolean isShowUETools = false;
    private boolean isShowTranslationTool = false;

    public enum ViewType {
        EditText(R.layout.debug_item_edit_text),
        CheckBox(R.layout.debug_item_checkbox),
        TextView(R.layout.debug_item_text_view),
        Spinner(R.layout.debug_item_spinner_view),
        Separator(R.layout.debug_item_separator);
        public final int layoutId;

        ViewType(int id) {
            layoutId = id;
        }
    }

    public enum Item {
        DisableConfigurableChannel("Disable Configurable Channel", ViewType.CheckBox),
        DisableStreamPreload("Disable Stream Preload", ViewType.CheckBox),
        EnableBackupDNS("Enable Backup DNS", ViewType.CheckBox),
        EnableStreamProtobuf("Enable Stream Protobuf", ViewType.CheckBox),
        MultiLanguageEnable("enable multi language", ViewType.CheckBox),
        ImageTranslateEnable("Enable Image Translation", ViewType.CheckBox),
        MockAndroidId("mock android id last three numbers", ViewType.EditText),
        LittleAppTest("Little App Test", ViewType.TextView),
        QRCodeH5Jumper("QR Code H5 Jumper", ViewType.TextView),
        BINDPAYTM("bind paytm", ViewType.TextView),
        TestANR("TEST ANR", ViewType.TextView),
        TestOOM("TEST OOM", ViewType.TextView),
        TestFinalizeTimeOut("TEST Finalize Timeout", ViewType.TextView),
        ForceUseOldUgc("Use Old Ugc Tools", ViewType.CheckBox),
        ForceJumpToPost("Force Jump To Post", ViewType.CheckBox),
        ForceQuickUpload("ForceQuickUpload", ViewType.CheckBox),
        VIDEO_URL_PREVIEW("preview_url", ViewType.TextView),
        LIVE_PERMISSION("enable live permission", ViewType.CheckBox),
        ENABLE_X2C("enable x2c", ViewType.CheckBox),
        ENABLE_ASYNC_LAYOUT("enable async inflate", ViewType.CheckBox),
        MediaDownload("ImmortalMediaDownload", ViewType.TextView),
        SHOW_LIVE_GUIDE("Show live guide dialog", ViewType.CheckBox),
        LIVE_DEBUG_H5("Debug Live H5", ViewType.TextView),
        LIVE_SHOW_DEBUG_INFO("show live debug info", ViewType.CheckBox),
        LIVE_WATCH("watch_live", ViewType.EditText),
        DebugVeVideoEditor("Debug VE Video Editor", ViewType.TextView),
        CatMan("SuperCatMan", ViewType.TextView),
        DOUBLE_GAP("double_gap", ViewType.EditText),
        EnableImageRequestDebug("Enable Image Request Debug", ViewType.CheckBox),
        EnableNewCard("EnableNewImmersiveVideoCard", ViewType.CheckBox),
        CrazyVideoDownload("CrazyVideoDownload", ViewType.CheckBox),
        EnableMainFeedNewCard("EnableMainFeedVideoCard", ViewType.CheckBox),
        EnableNewMediaViewer("EnableNewMediaViewer", ViewType.CheckBox),
        Music("Music", ViewType.TextView),
        FeedBack("FeedBack", ViewType.TextView),
        UETool("UETool", ViewType.TextView),
        LanguageDialogStyle("Language Dialog Style", ViewType.EditText),
        Location("choose city", ViewType.Spinner),
        LocationPage("start BDLocation Test", ViewType.TextView),
        Longitude("set longitude", ViewType.EditText),
        Latitude("set latitude", ViewType.EditText),
        NearbyStyle("Nearby Style", ViewType.Spinner),
        TranslationTool("Enable Translation Tool", ViewType.TextView),
        UseNewLoginStyle("Use new login style", ViewType.CheckBox),
        VideoUseUrlWithDataLoader("Video Use MediaLoader To PreLoad", ViewType.CheckBox),
        VideoPreloadRadical("Enable Video Preload Radical", ViewType.CheckBox),
        UseVideoPreload("Enable Video Preload", ViewType.CheckBox),
        TTVideoUseTTNet("TTVideo Use TTNet", ViewType.CheckBox),
        ImageLoaderUseTTNet("ImageLoader Use TTNet", ViewType.CheckBox),
        AlwaysLoginAsNewUser("Always login as new user", ViewType.CheckBox),
        DisableAppLogEncryption("Disable AppLog Encryption", ViewType.CheckBox),
        EnableScreenShot("Enable Screen Shot for BuzzShare", ViewType.CheckBox),
        DebugConsole("Show Debug Console", ViewType.CheckBox),
        ShowArticleWebType("Show Article Web Type(Amp, Server TransCode or Original Web)", ViewType.CheckBox),
        FabricDebug("Fabric Debug", ViewType.TextView),
        AutoCleanInShortDelay("Auto Clean Delay 5-10s", ViewType.CheckBox),
        InAppBilling("In App Billing", ViewType.TextView),
        RepostDebug("Repost Debug", ViewType.TextView),
        MigrateNrSavedArticle("Migrate Nr Saved Article", ViewType.TextView),
        WakeUpTimes("Wake Up Times", ViewType.TextView),
        ShortcutBadge("Short Cut Badge", ViewType.EditText),
        AsyncEvent("Async Event", ViewType.EditText),
        GoogleLogin("Google Login", ViewType.CheckBox),
        UseHttp("Use Http", ViewType.CheckBox),
        NativeAdPriorityOption("Enable Native Ads Priority", ViewType.CheckBox),
        NativeAdPriorityEdit("Native Ads Priority", ViewType.TextView),
        InterstitialAdPriorityOption("Enable Interstitial Ads Priority", ViewType.CheckBox),
        InterstitialPriorityEdit("Interstitial Ads Priority", ViewType.TextView),
        AdProviderId("Show Ad Provider Id", ViewType.CheckBox),
        AdPreloadEdit("Ads Preload Status", ViewType.TextView),
        AdStyleEdit("Ad Style", ViewType.TextView),
        AppUsage("App Usage Summ", ViewType.TextView),
        FpsMeter("Enable FPS Meter", ViewType.CheckBox),
        AlwaysShowLoginPopup("Always show login popup", ViewType.CheckBox),
        AlwaysGetAppSettings("Always get app settings", ViewType.CheckBox),
        ForceSwitchSession("Force Switch Session", ViewType.CheckBox),
        AlwaysSendSampleHttp("Always send sample http log", ViewType.CheckBox),
        FixOkHttpProxy("Fix Ok Http Proxy issue", ViewType.CheckBox),
        ApplyMdDesignOnTabLayout("Apply MD Design on TabLayout", ViewType.CheckBox),
        AlwaysShowVideoTabTip("Always show video tab tip", ViewType.CheckBox),
        AlwaysShowBottomTabRefreshTip("Always show bottom tab refresh tip", ViewType.CheckBox),
        AlwaysJumpToComment("Always jump to detail comment section", ViewType.CheckBox),
        AlwaysShowVideoErrorContent("Always Show Video Error", ViewType.CheckBox),
        AlwaysShowPullToRefreshGuide("Always show pull to refresh guide", ViewType.CheckBox),
        UseTextureViewRenderView("Use SurfaceView", ViewType.CheckBox),
        ShowMediaPlayerUsed("Show Used MediaPlayer", ViewType.CheckBox),
        UseAsyncMediaPlayer("Use async MediaPlayer", ViewType.CheckBox),
        ShowVideoBitrateLayout("Show Video Bitrate Info", ViewType.CheckBox),
        SelectMediaPlayerSwitch("MediaPlayer Type Switch", ViewType.CheckBox),
        SelectMediaPlayerType("MediaPlayer Type", ViewType.TextView),
        TestAppCurrActiveEvent("Test App Current Active Event", ViewType.CheckBox),
        LocalPushTest("Local Push Enable Local Time Test", ViewType.CheckBox),
        DoNotBindAd("Don't Bind Ad", ViewType.CheckBox),
        ShowTwoLineRelativeNews("Show Two Lines Relative News", ViewType.CheckBox),
        TestPreferenceProperty("Test Preference Property", ViewType.EditText),
        TestAppConfig("Update App Config", ViewType.EditText),
        TryNetChannelSelect("Try NetChannelSelect", ViewType.EditText),
        GcmDebug("Gcm Debug", ViewType.TextView),
        DynamicsDebug("Dynamics Debug", ViewType.TextView),
        TTNetDebug("TTNet Debug", ViewType.TextView),
        ShowAddToDebug("Show AddtoDebug", ViewType.CheckBox),
        PushDetailBackStrategy("Push Detail Back Strategy", ViewType.EditText),
        ShowFeedItemTime("Show Feed Time", ViewType.CheckBox),
        SwipToNextPage("Swip To Next Page", ViewType.CheckBox),
        SwipToRelated("Swip To Related Page", ViewType.CheckBox),
        NetGetNetwork("NetGetNetwork", ViewType.TextView),
        NetGetStream("NetGetStream", ViewType.TextView),
        NetSearchSuggestion("NetSearchSuggestion", ViewType.EditText),
        NetSDK("Net SDK Version", ViewType.EditText),
        TTNET_CRONET_ENABLE("TTNet Cronet Enable", ViewType.CheckBox),
        NativeCrash("Trigger native crash", ViewType.EditText),
        CollectNativeCrash("Collect native crash", ViewType.EditText),
        DegreeYoutubeLeech("Degree Youtube Leech", ViewType.CheckBox),
        ResetUser("Reset User", ViewType.CheckBox),
        HTTP1Only("Http1 Only", ViewType.CheckBox),
        GetAppLogConfig("Get app log config", ViewType.TextView),
        BlockThreshold("Block Time", ViewType.EditText),
        StartSearch("Start Search", ViewType.TextView),
        CommunityDebug("Community Debug", ViewType.TextView),
        BuzzAlwaysShowIntro("Always show buzz intro", ViewType.CheckBox),
        BuzzIntroStyle("Show Invitation style", ViewType.CheckBox),
        Separator("Separator", ViewType.Separator),
        DemandTest("Demand Test", ViewType.TextView),
        BuzzAlwaysSelectLanguage("Buzz always select language", ViewType.CheckBox),
        MagicBrowser("Router Manager", ViewType.EditText),
        ScriptUpdate("ScriptUpdate", ViewType.EditText),
        UgcChallengeDebug("Enable UgcChallenge", ViewType.CheckBox),
        ImmersiveDebug("Enable immersive", ViewType.CheckBox),
        ImageViewDebug("Enable ImageView Debug", ViewType.CheckBox),
        ImageViewReuse("Enable ImageView Reuse", ViewType.CheckBox),
        ImmersiveVideoVertical("immersive video vertical",ViewType.CheckBox),
        CricketAlwaysPull("Enable Cricket Always Pull", ViewType.CheckBox),
        IMSetting("enter IM setting page", ViewType.TextView),
        TestEffect("Enable Test Effect", ViewType.CheckBox),
        UgcDisableImageCompress("UGC disable image compression", ViewType.CheckBox),
        UgcUseNewLubanComputeSize("UGC use new Luban compute size", ViewType.CheckBox),
        AndroidGroupInnerCode("InnerCode-0619-21:30", ViewType.TextView),
        PRINT_APK_INJECT_INFO("Print Apk Inject Info", ViewType.TextView),
        FileStructure("Print Apk file structure", ViewType.TextView),
        USE_VOICE_SEARCH("Use Voice Search", ViewType.CheckBox),
        USE_UGC_PRELOAD("Use UGC Preload", ViewType.CheckBox),
        USE_DEFAULT_EFFECTS_CONFIG("Use default mv preload config(first check Use UGC Preload)", ViewType.CheckBox),
        UGC_ENTRANCE_TYPE("UGC Entrance Type(-1,0,1,2)", ViewType.EditText),
        DebugGoogleMap("Debug with google map",ViewType.TextView),
        FORCE_SHOW_MV("Force show MV", ViewType.CheckBox),
        IMAGE_PRELOAD_DOWNLOAD_DURATION_THRESHOLD("Preload Original Image Threshold", ViewType.Spinner);

        public final ViewType viewType;
        public int arrayId = -1;
        public String[] spinnerItems = null;
        public String[] spinnerItems1 = null;
        public String[] spinnerItems2 = null;
        public final String debugText;

        Item(String text, ViewType viewType) {
            this.debugText = text;
            this.viewType = viewType;
        }

        Item(String text, int arrayId, ViewType viewType) {
            this.debugText = text;
            this.viewType = viewType;
            this.arrayId = arrayId;
        }

        Item(String text, String[] spinnerItems, ViewType viewType) {
            this.debugText = text;
            this.viewType = viewType;
            this.spinnerItems = spinnerItems;
        }

        public void setArrayId(int arrayId) {
            this.arrayId = arrayId;
        }

        public void setSpinnerItems(String[] spinnerItems) {
            this.spinnerItems = spinnerItems;
        }

        public void setSpinnerItems1(String[] spinnerItems) {
            this.spinnerItems = spinnerItems;
        }

        public void setSpinnerItems2(String[] spinnerItems) {
            this.spinnerItems = spinnerItems;
        }
    }

    protected Item[] mItems;

    public DebugAdapter(Context context) {
        mContext = context;
        initItems();
        mItems = new Item[]{
                Item.DisableStreamPreload,
                Item.DebugGoogleMap,
                Item.DisableConfigurableChannel,
                Item.EnableBackupDNS,
                Item.VideoPreloadRadical,
                Item.VideoUseUrlWithDataLoader,
                Item.EnableStreamProtobuf,
                Item.BINDPAYTM,
                Item.MultiLanguageEnable,
                Item.ImageTranslateEnable,
                Item.MockAndroidId,
                Item.LittleAppTest,
                Item.QRCodeH5Jumper,
                Item.TestANR,
                Item.TestOOM,
                Item.ENABLE_X2C,
                Item.ENABLE_ASYNC_LAYOUT,
                Item.TestFinalizeTimeOut,
                Item.ForceUseOldUgc,
                Item.ForceJumpToPost,
                Item.ForceQuickUpload,
                Item.DebugVeVideoEditor,
                Item.Separator,
                Item.CatMan,
                Item.DOUBLE_GAP,
                Item.MediaDownload,
                Item.CrazyVideoDownload,
                Item.EnableNewCard,
                Item.EnableMainFeedNewCard,
                Item.EnableNewMediaViewer,
                Item.Separator,
                Item.LIVE_PERMISSION,
                Item.SHOW_LIVE_GUIDE,
                Item.LIVE_DEBUG_H5,
                Item.LIVE_SHOW_DEBUG_INFO,
                Item.LIVE_WATCH,
                Item.EnableImageRequestDebug,
                Item.LanguageDialogStyle,
                Item.Location,
                Item.LocationPage,
                Item.Longitude,
                Item.Latitude,
                Item.NearbyStyle,
                Item.FeedBack,
                Item.UETool,
                Item.TranslationTool,
                Item.UseNewLoginStyle,
                Item.UseVideoPreload,
                Item.AlwaysLoginAsNewUser,
                Item.RepostDebug,
                Item.DisableAppLogEncryption,
                Item.EnableScreenShot,
                Item.DebugConsole,
                Item.ShowVideoBitrateLayout,//是否展示视频码率信息布局
                Item.ShowArticleWebType,
                Item.FabricDebug,
                Item.ResetUser,
                Item.CommunityDebug,
                Item.AutoCleanInShortDelay,
                Item.InAppBilling,
                Item.MigrateNrSavedArticle,
                Item.WakeUpTimes,
                Item.HTTP1Only,
                Item.GetAppLogConfig,
                Item.GcmDebug,
                Item.DynamicsDebug,
                Item.TTNetDebug,
                Item.TTVideoUseTTNet,
                Item.ImageLoaderUseTTNet,
                Item.FpsMeter,
//                Item.GoogleLogin,
                Item.Separator,
                Item.NativeAdPriorityOption,
                Item.NativeAdPriorityEdit,
                Item.InterstitialAdPriorityOption,
                Item.InterstitialPriorityEdit,
                Item.AdProviderId,
                Item.AdPreloadEdit,
                Item.AdStyleEdit,
                Item.Separator,
                Item.UseHttp,
                Item.AppUsage,
                Item.AlwaysShowLoginPopup,
                Item.AlwaysGetAppSettings,
                Item.ForceSwitchSession,
                Item.AlwaysSendSampleHttp,
                Item.ApplyMdDesignOnTabLayout,
                Item.AlwaysShowVideoTabTip,
                Item.AlwaysShowBottomTabRefreshTip,
                Item.AlwaysJumpToComment,
                Item.FixOkHttpProxy,
//                Item.AddShareDestination,
                Item.AlwaysShowVideoErrorContent,
                Item.AlwaysShowPullToRefreshGuide,
                Item.UseTextureViewRenderView,
                Item.UseAsyncMediaPlayer,
                Item.SelectMediaPlayerSwitch,
                Item.ShowMediaPlayerUsed,
                Item.TestAppCurrActiveEvent,
                Item.LocalPushTest,
                Item.DoNotBindAd,
                Item.ShowTwoLineRelativeNews,
                Item.ShowAddToDebug,
                Item.ShowFeedItemTime,
                Item.SwipToNextPage,
                Item.SwipToRelated,
                Item.Separator,
                Item.NativeCrash,
                Item.CollectNativeCrash,
//                Item.ShortcutBadge,
                Item.PushDetailBackStrategy,
                Item.TestAppConfig,
                Item.TryNetChannelSelect,
                Item.TestPreferenceProperty,
                Item.AsyncEvent,
                Item.DegreeYoutubeLeech,
                Item.BlockThreshold,
                Item.StartSearch,
                Item.DemandTest,
                Item.MagicBrowser,
                Item.ScriptUpdate,
                Item.ImmersiveDebug,
                Item.ImmersiveVideoVertical,
                Item.UgcChallengeDebug,
                Item.ImageViewDebug,
                Item.ImageViewReuse,
                Item.CricketAlwaysPull,
                Item.IMSetting,
                Item.TestEffect,
                Item.UgcDisableImageCompress,
                Item.UgcUseNewLubanComputeSize,
                Item.AndroidGroupInnerCode,
                Item.PRINT_APK_INJECT_INFO,
                Item.Separator,
                Item.FileStructure,
                Item.USE_VOICE_SEARCH,
                Item.USE_UGC_PRELOAD,
                Item.USE_DEFAULT_EFFECTS_CONFIG,
                Item.UGC_ENTRANCE_TYPE,
                Item.FORCE_SHOW_MV,
                Item.IMAGE_PRELOAD_DOWNLOAD_DURATION_THRESHOLD
        };
        List<Item> list = new ArrayList<>();
        Collections.addAll(list, mItems);
        mItems = filterData(ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).useMediaPlayerDebug(), Item.SelectMediaPlayerType, list.indexOf(Item.SelectMediaPlayerSwitch) + 1);
    }

    private void initItems() {
    }

    public void setGoogleAccountClient(GoogleAccountClient client) {
        mGoogleAccountClient = client;
    }

    @Override
    public int getItemViewType(int position) {
        return mItems[position].viewType.ordinal();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewTypeIndex) {
        ViewType viewType = ViewType.values()[viewTypeIndex];
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType.layoutId, parent, false);
        return ViewHolder.newViewHolder(view, viewType);
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof CheckBoxViewHolder) {
            CheckBoxViewHolder viewHolder = (CheckBoxViewHolder) holder;
            viewHolder.setOnCheckedChangeListener(null);
            viewHolder.mCheckbox.setEnabled(true);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = mItems[position];
        holder.setItem(item);
        switch (item.viewType) {
            case EditText:
                if (holder instanceof EditTextViewHolder) {
                    bindEditTextViewHolder((EditTextViewHolder) holder, item);
                }
                break;
            case CheckBox:
                if (holder instanceof CheckBoxViewHolder) {
                    bindCheckBoxViewHolder((CheckBoxViewHolder) holder, item);
                }
                break;
            case TextView:
                if (holder instanceof TextViewViewHolder) {
                    bindTextViewViewHolder((TextViewViewHolder) holder, item);
                }
                break;
            case Separator:
                if (holder instanceof SeparatorViewHolder) {
                    bindSeparatorViewHolder((SeparatorViewHolder) holder, item);
                }
                break;
            case Spinner:
                if (holder instanceof SpinnerViewHolder) {
                    bindSpinnerViewHolder((SpinnerViewHolder) holder, item);
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void bindCheckBoxViewHolder(final CheckBoxViewHolder holder, final Item item) {
        final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
        switch (item) {
            case AutoCleanInShortDelay:
                holder.mCheckbox.setChecked(ClaymoreServiceLoader.loadFirstOrNull(IJobModel.class).getCleanCacheInShortDelay().getValue());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        ClaymoreServiceLoader.loadFirstOrNull(IJobModel.class).getCleanCacheInShortDelay().setValue(isChecked);
                    }
                });
                break;
            case GoogleLogin:
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            try {
                                mGoogleAccountClient.authorize();
                            } catch (GooglePlayServicesNotAvailableException e) {
                                Logger.d(TAG, "try signIn", e);
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
                                Logger.d(TAG, "try signOut", e);
                            }
                        }
                    }
                });
                break;
            case ShowArticleWebType:
                holder.mCheckbox.setChecked(setting.isShowArticleWebTypeEnable());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setShowArticleWebStyle(isChecked);
                        DebugUIUtil.INSTANCE.setShowArticleWebType(isChecked);
                    }
                });

                break;
            case NativeAdPriorityOption:
                holder.mCheckbox.setChecked(setting.isNativeAdPriorityEnable());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setNativeAdPriorityEnable(isChecked);
//                        AdCenter.getInstance(mContext).setDebugPriorityEnable(isChecked);
                    }
                });
                break;
            case InterstitialAdPriorityOption:
                holder.mCheckbox.setChecked(setting.isNativeAdPriorityEnable());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setInterstitialAdPriorityEnable(isChecked);
                        AdSymphony.getInstance().getInterstitialAdManager().getFillStrategyManager().setDebugPriorityEnable(isChecked);
                    }
                });
                break;
            case AdProviderId:
                holder.mCheckbox.setChecked(setting.showAdProviderId());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setShowAdProviderId(isChecked);
                    }
                });
                break;
            case UseHttp:
                holder.mCheckbox.setChecked(setting.isUseHttp());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setUseHttp(isChecked);
                        ClaymoreServiceLoader.loadFirstOrNull(ITTNetModuleInitHelper.class).setUseHttp(isChecked);
                    }
                });
                break;
            case FpsMeter:
                holder.mCheckbox.setChecked(FpsMeterView.isShowing());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            FpsMeterView.show(mContext, 200);
                        } else {
                            FpsMeterView.hide();
                        }
                    }
                });
                break;
            case AlwaysShowLoginPopup:
                holder.mCheckbox.setChecked(setting.shouldAlwaysShowLoginPopup());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setAlwaysShowLoginPopup(isChecked);
                    }
                });
                break;
            case AlwaysGetAppSettings:
                holder.mCheckbox.setChecked(setting.shouldAlwaysGetAppSettings());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setAlwaysGetAppSettings(isChecked);
                    }
                });
                break;
            case AlwaysSendSampleHttp:
                holder.mCheckbox.setChecked(setting.shouldAlwaysSendSampleHttp());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setAlwaysSendSampleHttp(isChecked);
                    }
                });
                break;
            case FixOkHttpProxy:
                holder.mCheckbox.setChecked(setting.shouldFixOkHttpProxy());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setShouldFixOkHttpProxy(isChecked);
                    }
                });
                break;
            case ApplyMdDesignOnTabLayout:
                holder.mCheckbox.setChecked(setting.shouldApplyMdDesignOnTabLayout());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setApplyMdDesignOnTabLayout(isChecked);
                    }
                });
                break;
            case AlwaysShowVideoTabTip:
                holder.mCheckbox.setChecked(setting.shouldAlwaysShowVideoTabTip());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setAlwaysShowVideoTabTip(isChecked);
                    }
                });
                break;
            case AlwaysShowBottomTabRefreshTip:
                holder.mCheckbox.setChecked(setting.shouldAlwaysShowBottomTabRefreshTip());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setAlwaysShowBottomTabRefreshTip(isChecked);
                    }
                });
                break;
            case AlwaysJumpToComment:
                holder.mCheckbox.setChecked(setting.shouldAlwaysJumpToComment());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setAlwaysJumpToComment(isChecked);
                    }
                });
                break;
            case AlwaysShowPullToRefreshGuide:
                holder.mCheckbox.setChecked(setting.shouldAlwaysShowPullToRefreshGuide());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setAlwaysShowPullToRefreshGuide(isChecked);
                    }
                });
                break;
            case AlwaysShowVideoErrorContent:
                holder.mCheckbox.setChecked(setting.showVideoErrorContent());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setShowVideoErrorContent(isChecked);
                    }
                });
                break;
            case UseTextureViewRenderView:
                holder.mCheckbox.setChecked(setting.useSurfaceViewRenderView());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setUseSurfaceViewRenderView(isChecked);
                    }
                });
                break;
            case ShowMediaPlayerUsed:
                holder.mCheckbox.setChecked(setting.showMediaPlayerUsed());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setShowMediaPlayerUsed(isChecked);
                    }
                });
                break;
            case TestAppCurrActiveEvent:
                holder.mCheckbox.setChecked(setting.testAppActiveEvent());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setTestAppActiveEvent(isChecked);
                    }
                });
                break;
            case LocalPushTest:
                holder.mCheckbox.setChecked(setting.testLocalPush());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setLocalPushTestEnable(isChecked);
                    }
                });
                break;
            case DoNotBindAd:
                holder.mCheckbox.setChecked(setting.doNotBindAd());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setDoNotBindAd(isChecked);
                    }
                });
                break;
            case ShowTwoLineRelativeNews:
                holder.mCheckbox.setChecked(setting.showTwoLineRelativeNews());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setShowTwoLineRelativeNews(isChecked);
                    }
                });
                break;
            case UseAsyncMediaPlayer:
                holder.mCheckbox.setChecked(setting.useAsyncMediaPlayer());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setUseAsyncMediaPlayer(isChecked);
                    }
                });
                break;
            case ShowAddToDebug:
                holder.mCheckbox.setChecked(setting.showAddtoDebug());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setShowAddtoDebug(isChecked);
                    }
                });
                break;
            case ShowFeedItemTime:
                break;
            case SwipToNextPage:
                holder.mCheckbox.setChecked(setting.getEnableSwipeToNext());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setEnableSwipeToNext(isChecked);
                    }
                });
                break;
            case SwipToRelated:
                holder.mCheckbox.setChecked(setting.getSwipeToRelated());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setSwipeToRelated(isChecked);
                    }
                });
            case ShowVideoBitrateLayout://是否显示码率、分辨率信息
                holder.mCheckbox.setChecked(setting.showVideoBitrateLayout());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setShowVideoBitrateLayout(isChecked);
                    }
                });
                break;
            case SelectMediaPlayerSwitch:
                holder.mCheckbox.setChecked(setting.useMediaPlayerDebug());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setMediaPlayerDebug(isChecked);
                        List<Item> list = new ArrayList<>();
                        Collections.addAll(list, mItems);
                        mItems = filterData(isChecked, Item.SelectMediaPlayerType, list.indexOf(Item.SelectMediaPlayerSwitch) + 1);
                        notifyDataSetChanged();
                    }
                });
                break;
            case DegreeYoutubeLeech:
                holder.mCheckbox.setChecked(setting.degreeYoutubeLeech());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setDegreeYoutubeLeech(isChecked);
                    }
                });
                break;
            case HTTP1Only:
                holder.mCheckbox.setChecked(setting.useHttp1Only());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setUseHttp1Only(isChecked);
                    }
                });
                break;
            case ResetUser:
                holder.mCheckbox.setChecked(ClaymoreServiceLoader.loadFirstOrNull(IAppLogV3.class).isNewUserMode(mContext));
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        ClaymoreServiceLoader.loadFirstOrNull(IAppLogV3.class).setNewUserMode(mContext, isChecked);
                        resetUser();
                    }
                });
                break;
            case BuzzAlwaysShowIntro: {
                holder.mCheckbox.setChecked(setting.getAlwaysShowBuzzIntro());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setAlwaysShowBuzzIntro(isChecked);
                    }
                });
                break;
            }
            case BuzzIntroStyle: {
                holder.mCheckbox.setChecked(setting.getUseInvitationStyle());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setUseInvitationStyle(isChecked);
                    }
                });
                break;
            }
            case BuzzAlwaysSelectLanguage: {
                holder.mCheckbox.setChecked(setting.getBuzzAlwaysSelectLanguage());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setBuzzAlwaysSelectLanguage(isChecked);
                    }
                });
                break;
            }
            case DebugConsole: {
                holder.mCheckbox.setChecked(DebugConsole.isShown());
                Logger.d("Debug Console", "Debug Console setChecked " + DebugConsole.isShown());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            DebugConsole.show(mContext);
                            DebugConsole.log("Debug Console On");
                            Logger.d("Debug Console", "Debug Console On");
                        } else {
                            DebugConsole.hide();
                            DebugConsole.log("Debug Console Off");
                            Logger.d("Debug Console", "Debug Console Off");
                        }
                    }
                });
                break;
            }
            case EnableScreenShot: {
                holder.mCheckbox.setChecked(BuzzDebugModel.INSTANCE.getEnableScreenShot().getValue());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        BuzzDebugModel.INSTANCE.getEnableScreenShot().setValue(isChecked);
                    }
                });
                break;
            }
            case DisableAppLogEncryption: {
                holder.mCheckbox.setChecked(setting.getDisableAppLogEncryption());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setDisableAppLogEncryption(isChecked);
                    }
                });
                break;
            }

            case AlwaysLoginAsNewUser: {
                holder.mCheckbox.setChecked(setting.getAlwaysLoginAsNewUser());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setAlwaysLoginAsNewUser(isChecked);
                    }
                });
                break;
            }
            case UseNewLoginStyle: {
                holder.mCheckbox.setChecked(setting.getUseNewLoginStyle());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setUseNewLoginStyle(isChecked);
                    }
                });
                break;
            }

            case VideoUseUrlWithDataLoader: {
                holder.mCheckbox.setChecked(setting.getVideoUrlWithDataLoader());
                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    setting.setVideoUrlWithDataLoader(isChecked);
                });
                break;
            }

            case VideoPreloadRadical: {
                holder.mCheckbox.setChecked(setting.getEnablePreloadVideoRadical());
                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    setting.setEnablePreloadVideoRadical(isChecked);
                });
                break;
            }

            case UseVideoPreload: {
                holder.mCheckbox.setChecked(setting.getEnableVideoPreload());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setEnableVideoPreload(isChecked);
                    }
                });
                break;
            }

            case TTVideoUseTTNet: {
                holder.mCheckbox.setChecked(setting.getTTVideoUseTTNet());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setTTVideoUseTTNet(isChecked);
                    }
                });
                break;
            }

            case ImageLoaderUseTTNet: {
                holder.mCheckbox.setChecked(setting.getImageLoderUseTTnet());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setImageLoderUseTTnet(isChecked);
                    }
                });
                break;
            }

            case EnableImageRequestDebug: {
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        TTNetUrlListFetcher.Companion.setDEBUG(isChecked);
                    }
                });
                break;
            }
            case UgcChallengeDebug: {
                holder.mCheckbox.setChecked(setting.getEnableUgcChallenge());
                holder.setOnCheckedChangeListener((buttonView, isChecked) ->
                        setting.setEnableUgcChallenge(isChecked));
                break;
            }
            case ENABLE_ASYNC_LAYOUT: {
                holder.mCheckbox.setChecked(setting.enableAsyncInflate());
                holder.setOnCheckedChangeListener((buttonView, isChecked) ->
                        setting.setAsyncInflateEnable(isChecked));
                break;
            }
            case ENABLE_X2C: {
                holder.mCheckbox.setChecked(setting.enableX2C());
                holder.setOnCheckedChangeListener((buttonView, isChecked) ->
                        setting.setX2cEnable(isChecked));
                break;
            }
            case TestEffect: {
                holder.mCheckbox.setChecked(setting.getEnableTestEffect());
                holder.setOnCheckedChangeListener((buttonView, isChecked) ->
                        setting.setEnableTestEffect(isChecked));
                break;
            }
            case ImmersiveDebug: {
                holder.mCheckbox.setChecked(setting.getImmersiveDebugEnable());
                holder.setOnCheckedChangeListener((buttonView, isChecked) ->
                        setting.setImmersiveDebugEnable(isChecked));
                break;
            }
            case UgcDisableImageCompress: {
                holder.mCheckbox.setChecked(setting.getDisableImageCompress());
                holder.setOnCheckedChangeListener((buttonView, isChecked) ->
                        setting.setDisableImageCompress(isChecked)
                );
                break;
            }
            case UgcUseNewLubanComputeSize: {
                holder.mCheckbox.setChecked(setting.useNewLubanComputeSize());
                holder.setOnCheckedChangeListener((buttonView, isChecked) ->
                        setting.setUseNewLubanComputeSize(isChecked)
                );
                break;
            }
            case ImageViewDebug:{
                holder.mCheckbox.setChecked(setting.getEnableImageDebug());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setEnableImageDebug(isChecked);
                    }
                });
                break;
            }
            case ImageViewReuse: {
                holder.mCheckbox.setChecked(setting.getEnableImageReuse());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setEnableImageReuse(isChecked);
                    }
                });
                break;
            }
            case CricketAlwaysPull:{
                holder.mCheckbox.setChecked(CricketDebugConfig.INSTANCE.getAlwaysPull());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        CricketDebugConfig.INSTANCE.setAlwaysPull(isChecked);
                    }
                });
                break;
            }
            case ForceUseOldUgc: {
                holder.mCheckbox.setChecked(setting.getForceUseOldUgcTools());
                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    setting.setForceUseOldUgcTools(isChecked);
                });
                break;
            }
            case ForceJumpToPost: {
                holder.mCheckbox.setChecked(setting.getForceJumpToPost());
                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    setting.setForceJumpToPost(isChecked);
                });
                break;
            }
            case ForceQuickUpload: {
                holder.mCheckbox.setChecked(setting.getForceUseQuickUpload());
                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    setting.setForceUseQuickUpload(isChecked);
                });
                break;
            }
            case EnableNewCard: {
                holder.mCheckbox.setChecked(BuzzSPModel.INSTANCE.isUseImmersiveNewVideoCard().getValue());
                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    BuzzSPModel.INSTANCE.isUseImmersiveNewVideoCard().setValue(isChecked);
                });
                break;
            }
            case CrazyVideoDownload: {
                holder.mCheckbox.setChecked(setting.getCrazyVideoDownload());
                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    setting.setCrazyVideoDownload(isChecked);
                });
                break;
            }
            case EnableMainFeedNewCard: {
                holder.mCheckbox.setChecked(BuzzSPModel.INSTANCE.isUseMainFeedNewVideoCard().getValue());
                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    BuzzSPModel.INSTANCE.isUseMainFeedNewVideoCard().setValue(isChecked);
                });
                break;
            }
            case ImmersiveVideoVertical: {
                holder.mCheckbox.setChecked(BuzzSPModel.INSTANCE.isImmersiveVideoDirectVertical().getValue());
                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    BuzzSPModel.INSTANCE.isImmersiveVideoDirectVertical().setValue(isChecked);
                });
                break;
            }
            case EnableNewMediaViewer: {
                holder.mCheckbox.setChecked(BuzzSPModel.INSTANCE.isUseNewMediaViewer().getValue());
                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    BuzzSPModel.INSTANCE.isUseNewMediaViewer().setValue(isChecked);
                });
                break;
            }
            case LIVE_PERMISSION:
                MultiProcessSharedPrefModel.BooleanProperty canStartLive = ClaymoreServiceLoader.loadFirstOrNull(IHeloLiveModel.class).getCanStartLive();
                holder.mCheckbox.setChecked(canStartLive.getValue());
                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    canStartLive.setValue(isChecked);
                });
                break;
            case SHOW_LIVE_GUIDE:
                MultiProcessSharedPrefModel.BooleanProperty showed = ClaymoreServiceLoader.loadFirstOrNull(IHeloLiveModel.class).getShowedEntryGuideDialog();
                holder.mCheckbox.setChecked(!showed.getValue());
                holder.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                    showed.setValue(!isChecked);
                }));
                break;
            case LIVE_SHOW_DEBUG_INFO:
                MultiProcessSharedPrefModel.BooleanProperty show = ClaymoreServiceLoader.loadFirstOrNull(IHeloLiveModel.class).getShowDebugInfo();
                holder.mCheckbox.setChecked(show.getValue());
                holder.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                    show.setValue(isChecked);
                }));
                break;
            case USE_VOICE_SEARCH:
                holder.mCheckbox.setChecked(setting.getUseVoiceSearch());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setUseVoiceSearch(isChecked);
                    }
                });
                break;
            case USE_UGC_PRELOAD:
                holder.mCheckbox.setChecked(setting.getUgcPreload());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setUseUgcPreload(isChecked);
                    }
                });
                break;
            case USE_DEFAULT_EFFECTS_CONFIG:
                holder.mCheckbox.setChecked(setting.getUseDefaultPreloadEffectsConfig());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setUseDefaultPreloadEffectsConfig(isChecked);
                    }
                });
                break;
            case MultiLanguageEnable:
                holder.mCheckbox.setChecked(setting.getEnableMultiLanguage());
                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setting.setEnableMultiLanguage(isChecked);
                    }
                });
                break;
            case ImageTranslateEnable:
                holder.mCheckbox.setChecked(setting.getEnableImageTranslate());
                holder.setOnCheckedChangeListener((buttonView, isChecked) -> setting.setEnableImageTranslate(isChecked));
                break;
            case EnableStreamProtobuf:
                holder.mCheckbox.setChecked(setting.getEnableStreamProtobuf());
                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    setting.setEnableStreamProtobuf(isChecked);

                });
                break;
            case EnableBackupDNS:
                holder.mCheckbox.setChecked(setting.getEnableBackupDNS());
                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    setting.setEnableBackupDNS(isChecked);

                });
                break;
            case DisableConfigurableChannel:
                holder.mCheckbox.setChecked(setting.getDisableConfigurableChannel());
                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    setting.setDisableConfigurableChannel(isChecked);
                });
                break;
            case FORCE_SHOW_MV:
                holder.mCheckbox.setChecked(setting.getForceShowMV());
                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    setting.setForceShowMV(isChecked);
                });
                break;
            case DisableStreamPreload:
                holder.mCheckbox.setChecked(setting.getDisableStreamPreload());
                holder.setOnCheckedChangeListener((buttonView, isChecked) -> setting.setDisableStreamPreload(isChecked));
            default:
                break;
        }
    }

    private void resetUser() {
//        DataCleanManager.cleanApplicationData(mContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)).clearApplicationUserData();
            return;
        }

//        SharedPreferences sp = mContext.getSharedPreferences(AppLogConstants.getDeviceParamsSpName(), Context.MODE_PRIVATE);
//        sp.edit().clear()
//                .putString(KEY_OPENUDID, UDidUtils.createRandomUDID())
//                .putString(KEY_UDID, UDidUtils.createRandomUDID())
//                .putString(KEY_CLIENTUDID, UUID.randomUUID().toString())
//                .apply();
//        SharedPreferences sp2 = mContext.getSharedPreferences(
//                SP_APPLOG_STATS, Context.MODE_PRIVATE);
//        sp2.edit().clear().apply();

        // must clear cookies
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                CookieManagerCompat.getInstance().removeAllCookies(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(mContext.getApplicationContext());
            cookieSyncManager.startSync();
            CookieManager.getInstance().removeAllCookie();
            cookieSyncManager.stopSync();
        }

        ToastUtil.toast("Restart app to take effect", Toast.LENGTH_SHORT);

        // restart app
        // method 1
//      ((Activity)mContext).finish();
//      Intent intent = AppUtils.getLaunchIntentForPackage(mContext, BuildConfig.PKG_NAME);
//      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//      mContext.startActivity(intent);

        // method 2
//        Intent startIntent = new Intent(mContext,SplashActivity.class);
//        PackageManager pm = mContext.getPackageManager();
//        if(pm!=null)
//            startIntent = pm.getLaunchIntentForPackage(mContext.getPackageName());
//        AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//        PendingIntent restartIntent = PendingIntent.getActivity(
//                mContext.getApplicationContext(), 0, startIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000, restartIntent);
//
//        System.exit(0);
        ClaymoreServiceLoader.loadFirstOrNull(IMineService.class).getISwitchLanguageHelperUtil().goLaunchActivity(mContext, true, false);
    }

    private Item[] filterData(boolean add, Item item, int index) {
        List<Item> list = new ArrayList<>();
        Collections.addAll(list, mItems);
        if (add && !list.contains(item)) {
            list.add(index, item);
        } else if (!add && list.contains(item)) {
            list.remove(item);
        }
        return list.toArray(new Item[list.size()]);
    }

    protected void bindEditTextViewHolder(final EditTextViewHolder holder, final Item item) {
        switch (item) {
            case Longitude:
                holder.setText(ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getLongitude());
                break;
            case Latitude:
                holder.setText(ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getLatitude());
                break;
            default:
                holder.setText("");
        }
        holder.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                switch (item) {
                    case ShortcutBadge:
                        try {
                            ShortcutBadger.applyCount(mContext, Integer.parseInt(holder.getText()));
                        } catch (Exception e) {

                        }
                        break;
                    case AsyncEvent:
//                        try {
                        AsyncEvent.test(mContext);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        break;
                    case TestPreferenceProperty:
//                        DemoMultiProcessPrefModel.sInstance.test();
//                        DemoPreferenceModel.sInstance.testDemo(10000);
//                        DemoMultiProcessPrefModel.sInstance.testMultiProcess();
                        DemoMultiProcessPrefModel.sInstance.test();
                        break;
                    case TestAppConfig:
                        ClaymoreServiceLoader.loadFirstOrNull(IAppConfig.class).testRefreshAppConfig();
//                        TTNetModuleManager.INSTANCE.doRefresh(true);
                        break;
                    case PushDetailBackStrategy:
                        try {
                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setPushDetailBackStrategy(Integer.valueOf(holder.getText()));
                            ToastUtil.toast("Using push strategy: " + holder.getText(), Toast.LENGTH_SHORT);
                        } catch (Exception e) {
                            ToastUtil.toast( e.getMessage(), Toast.LENGTH_SHORT);
                        }
                        break;
                    case LIVE_WATCH:
                        String id = holder.getText().trim();
                        HeloLiveService service = ClaymoreServiceLoader.loadFirstOrNull(HeloLiveService.class);
                        Context context = mContext;
                        if (service != null && context != null) {
                            try {
                                long roomId = Long.parseLong(id);
                                service.joinLive(context, roomId, null, null, "debug", "debug", null);
                            } catch (Exception ignore) {
                            }
                        }
                        break;
                    case DOUBLE_GAP:
                        Long timeGap = Long.parseLong(holder.getText().trim());
                        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setDoubleClickGapTime(timeGap);
                        break;
                    case NativeCrash:
                        break;
                    case CollectNativeCrash:
                        break;
                    case BlockThreshold:
                        try {
                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setBlockThreshold(Integer.parseInt(holder.getText()));
                        } catch (Exception e) {
                            ToastUtil.toast(e.getMessage(), Toast.LENGTH_SHORT);
                        }
                        break;
                    case MagicBrowser:
                        try {
                            ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setMagicUrl(holder.getText());
                        } catch (Exception e) {
                            ToastUtil.toast(e.getMessage(), Toast.LENGTH_SHORT);
                        }
                        ArouterManager.Companion.getInst().routeUniversal(mContext, holder.getText(), null, false, null);
                        break;
                    case ScriptUpdate:
                        String url;
                        if (!AppUtils.isHttpUrl(url = holder.getText().trim())) {
                            ToastUtil.toast("Please input a valid patch url to continue!", Toast.LENGTH_LONG);
                            return;
                        }
                        DebugJavaScriptUtil.Companion.getInstance().doPatch(mContext, url);
                        break;
                    case LanguageDialogStyle:
                        try {
                            String s = holder.getText().trim();
                            if (!TextUtils.isEmpty(s)) {
                                int digit = Integer.parseInt(s);
                                BuzzSPModel.INSTANCE.getLanguageStyle().setValue(digit);
                            }
                        } catch (Exception e) {
                        }
                        break;

                    case Longitude:
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
                        break;
                    case Latitude:
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
                        break;
                    case MockAndroidId:
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
                        break;
                    case UGC_ENTRANCE_TYPE:
                        try {
                            int type = Integer.parseInt(holder.getText());
                            ClaymoreServiceLoader.loadFirst(IDebugSettings.class).setUgcEntranceType(type);
                        } catch (NumberFormatException e) {
                            ClaymoreServiceLoader.loadFirst(IDebugSettings.class).setUgcEntranceType(-1);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    protected void bindSpinnerViewHolder(final SpinnerViewHolder holder, final Item item) {
        switch (item) {
            case Location:
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
                break;

            case IMAGE_PRELOAD_DOWNLOAD_DURATION_THRESHOLD:
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

                break;

            case NearbyStyle:
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
                break;
        }
    }

    protected void bindTextViewViewHolder(final TextViewViewHolder holder, final Item item) {
        if (item == Item.WakeUpTimes) {
            holder.mText.setText("Wake times: " + ClaymoreServiceLoader.loadFirstOrNull(IJobModel.class).getWakeTimes().getValue());
        }
        holder.setOnClickListener(new View.OnClickListener() {
            IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);

            @Override
            public void onClick(View v) {
                switch (item) {
                    case DebugGoogleMap:
                        SmartRouter.buildRoute(mContext,"//buzz/debug/gmap").open();
                        break;
                    case TestANR:
                        CrashTriggerConfig config =
                                new CrashTriggerConfig.Builder()
                                        .setAnrSleepTime(100 * 1000)
                                        .setShakeSensitivity(60)
                                        .create();

                        CrashTrigger.install(mContext, config);
                        ToastUtil.toast("shake to test ANR", Toast.LENGTH_SHORT);
                    case TestOOM:
                        OOMMaker.createOOM();
                        ToastUtil.toast("oom", Toast.LENGTH_SHORT);
                    case TestFinalizeTimeOut:
                        if (ArticleBaseBuildConfig.IS_LOCAL_TEST) {
                            FinalizeTimeOutObject.testFinalize();
                        }
                        break;
                    case DebugVeVideoEditor:
                    case RepostDebug:

                        break;
                    case WakeUpTimes:
                        holder.mText.setText("Wake times: " + ClaymoreServiceLoader.loadFirstOrNull(IJobModel.class).getWakeTimes().getValue());
                        break;
                    case NativeAdPriorityEdit:
                        Intent priorityIntent = new Intent(mContext, AdDebugActivity.class);
                        priorityIntent.putExtra(AdDebugActivity.KEY_FRAGMENT_TYPE, AdDebugActivity.FRAGMENT_TYPE_AD_PRIORITY);
                        priorityIntent.putExtra(AdDebugActivity.KEY_AD_TYPE, NewAdDebugPriorityFragment.AD_TYPE_NATIVE);
                        mContext.startActivity(priorityIntent);
                        break;
                    case InterstitialPriorityEdit:
                        Intent interstitialAdPriorityIntent = new Intent(mContext, AdDebugActivity.class);
                        interstitialAdPriorityIntent.putExtra(AdDebugActivity.KEY_FRAGMENT_TYPE, AdDebugActivity.FRAGMENT_TYPE_AD_PRIORITY);
                        interstitialAdPriorityIntent.putExtra(AdDebugActivity.KEY_AD_TYPE, NewAdDebugPriorityFragment.AD_TYPE_INTERSTITIAL);
                        mContext.startActivity(interstitialAdPriorityIntent);
                        break;
                    case AdPreloadEdit:
                        Intent preloadIntent = new Intent(mContext, AdDebugActivity.class);
                        preloadIntent.putExtra(AdDebugActivity.KEY_FRAGMENT_TYPE, AdDebugActivity.FRAGMENT_TYPE_PRELOAD);
                        mContext.startActivity(preloadIntent);
                        break;
                    case AdStyleEdit:
                        Intent styleIntent = new Intent(mContext, AdDebugActivity.class);
                        styleIntent.putExtra(AdDebugActivity.KEY_FRAGMENT_TYPE, AdDebugActivity.FRAGMENT_TYPE_AD_STYLE);
                        mContext.startActivity(styleIntent);
                        break;
                    case AppUsage:
                        Intent usageIntent = new Intent(mContext, AdDebugActivity.class);
                        usageIntent.putExtra(AdDebugActivity.KEY_FRAGMENT_TYPE, AdDebugActivity.FRAGMENT_TYPE_USAGE);
                        mContext.startActivity(usageIntent);
                        break;
                    case GcmDebug:
                        Intent gcmIntent = new Intent(mContext, GcmDebugActivity.class);
                        mContext.startActivity(gcmIntent);
                        break;
                    case DynamicsDebug:
                        Intent dynamicsIntent = new Intent(mContext, DebugDynamicsActivity.class);
                        mContext.startActivity(dynamicsIntent);
                        break;

                    case TTNetDebug:
                        Intent debugNet = new Intent(mContext, DebugNetActivity.class);
                        mContext.startActivity(debugNet);
                        break;
                    case SelectMediaPlayerType:
                        AlertDialog.Builder builder = UIUtils.getThemedAlertDlgBuilder(mContext);
                        builder.setSingleChoiceItems(R.array.media_player_type, setting.getMediaPlayerType(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setting.setMediaPlayerType(which);
                            }
                        });
                        builder.setPositiveButton(R.string.confirm, null);
                        builder.setNegativeButton(R.string.cancel, null);
                        builder.show();
                        break;
                    case StartSearch: {
                        break;
                    }
                    case DemandTest: {
                        AppLogVerifyClient.init(ArticleBaseBuildConfig.APP_ID, "", true);
                        String user = AppLogVerifyClient.getUserOrNull(mContext);
                        AppLogVerifyClient.beginVerifyAppLog((Activity) mContext, user);
                        break;
                    }

                    case UETool: {
                        if (!isShowUETools) {
                            isShowUETools = UETool.showUETMenu();
                        } else {
                            isShowUETools = false;
                            UETool.dismissUETMenu();
                        }
                        break;
                    }
                    case LIVE_DEBUG_H5:
                        if (!ClaymoreServiceLoader.loadFirstOrNull(IHeloLiveManager.class).isFeatureInstalled()) {
                            ToastUtil.toast("ugc feature not installed", Toast.LENGTH_SHORT);
                            return;
                        }
                        ClaymoreServiceLoader.loadFirstOrNull(IHeloLiveManager.class).debugH5(mContext);
                        break;
                    case FeedBack: {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(FeedbackConstants.BUNDLE_HAS_NEW_FEEDBACK, false);
                        bundle.putString(FeedbackConstants.KEY_APPKEY, AbsConstants.FEEDBACK_APPKEY);
                        bundle.putBoolean(FeedbackConstants.BUNDLE_MY_OPTION_ONLY, true);
                        FeedbackServiceManager.getFeedBackActionImpl().jumpToFeedBackPage(mContext, bundle);
                        break;
                    }
                    case Music: {
//                        Intent intent = new Intent(mContext, MusicTestActivity.class);
//                        mContext.startActivity(intent);
                        break;
                    }
                    case TranslationTool: {
                        if (mContext instanceof Activity) {
                            ((Activity) mContext).finish();
                        }

                        ITranslationService service = ClaymoreServiceLoader.loadFirstOrNull(ITranslationService.class);
                        if (service != null) {
                            if (service.isMenuShown()) {
                                service.setEnableTranslationOnTextChanged(false);
                                service.dismissMenu();
                                service.destroy();
                            } else {
                                service.setEnableTranslationOnTextChanged(true);
                                service.showMenu(mContext);
                                service.start();
                            }
                        }
                        break;
                    }
                    case LocationPage: {
                        if (mContext instanceof Activity) {
                            ((Activity) mContext).startActivity(new Intent(mContext, LocationTestMainActivity.class));
                        }
                        break;
                    }
                    case PRINT_APK_INJECT_INFO: {
                        String apkInjectInfo = ApkInjectWrapper.INSTANCE.getApplicationInjectInfo(BaseApplication.getInst());
                        if (apkInjectInfo == null) {
                            apkInjectInfo = "";
                        }
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                                .setMessage(apkInjectInfo)
                                .create();
                        alertDialog.show();
                        break;
                    }
                    case FileStructure: {
                        if (mContext instanceof Activity) {
                            mContext.startActivity(new Intent(mContext, FileStructureTestActivity.class));
                        }
                        break;
                    }
                    case MediaDownload: {
                        if (mContext instanceof Activity) {
                            SmartRouter.buildRoute(mContext,"//buzz/mediadownload_act").open();
                        }
                        break;
                    }
                    case IMSetting:{
                        SmartRouter.buildRoute(mContext,"//buzz/im/debug").open();
                    }
                    case LittleAppTest: {
                        ClaymoreServiceLoader.loadFirstOrNull(IAppBrandService.class).openDebug(mContext);
                        break;
                    }
                    case QRCodeH5Jumper: {
                        Intent intent = new Intent(mContext, QrCodeScannerActivity.class);
                        mContext.startActivity(intent);
                        break;
                    }
                    case BINDPAYTM:{
                        ClaymoreServiceLoader.loadFirstOrNull(IPolarisDialogService.class).showBindPayTmDialog();
                        break;
                    }
                    default:
                        break;
                }
            }
        });
    }

    protected void bindSeparatorViewHolder(final SeparatorViewHolder holder, final Item item) {

    }

    @Override
    public int getItemCount() {
        return mItems.length;
    }

    public abstract static class ViewHolder extends RecyclerView.ViewHolder {
        protected Context mContext;
        protected Item mItem;
        protected final View mRootView;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mRootView = itemView;
        }

        public static ViewHolder newViewHolder(View view, ViewType viewType) {
            switch (viewType) {
                case EditText:
                    return new EditTextViewHolder(view);
                case CheckBox:
                    return new CheckBoxViewHolder(view);
                case TextView:
                    return new TextViewViewHolder(view);
                case Separator:
                    return new SeparatorViewHolder(view);
                case Spinner:
                    return new SpinnerViewHolder(view);
                default:
                    break;
            }
            return null;
        }

        public void setItem(Item item) {
            mItem = item;
            onItemSet();
        }

        public Item getItem() {
            return mItem;
        }

        public abstract void onItemSet();
    }


    public static class EditTextViewHolder extends ViewHolder {

        private final AppCompatEditText mEditText;
        private final Button mTestButton;

        public EditTextViewHolder(View itemView) {
            super(itemView);
            mEditText = (AppCompatEditText) itemView.findViewById(R.id.debug_edit_text);
            mTestButton = (Button) itemView.findViewById(R.id.debug_btn);
        }


        @Override
        public void onItemSet() {
            mEditText.setHint(mItem.debugText);
        }

        public String getText() {
            return mEditText.getText().toString();
        }

        public void setText(String text) {
            mEditText.setText(text);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            mTestButton.setOnClickListener(listener);
        }
    }

    public static class CheckBoxViewHolder extends ViewHolder {
        public final CheckBox mCheckbox;

        public CheckBoxViewHolder(View itemView) {
            super(itemView);
            mCheckbox = (CheckBox) itemView.findViewById(R.id.debug_checked_text_view);
        }

        @Override
        public void onItemSet() {
            mCheckbox.setText(mItem.debugText);
        }

        public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
            mCheckbox.setOnCheckedChangeListener(listener);
        }
    }

    public static class TextViewViewHolder extends ViewHolder {
        private final TextView mText;

        public TextViewViewHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.text);
        }

        @Override
        public void onItemSet() {
            mText.setText(mItem.debugText);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            itemView.setOnClickListener(listener);
        }
    }

    public static class SpinnerViewHolder extends ViewHolder {
        private final TextView mText;
        private final LinearLayout mSpinnerContainer;
        private List<ArrayAdapter<String>> adapters = new ArrayList<>();
        private List<List<String>> items = new ArrayList<>();
        private List<Spinner> spinners = new ArrayList<>();

        public SpinnerViewHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.title);
            mSpinnerContainer = (LinearLayout) itemView.findViewById(R.id.spinner_container);
        }

        public void clearSpinnerItems() {
            mSpinnerContainer.removeAllViews();
        }

        public void addSpinnerItem(List<String> spinnerItems, OnItemSelectListener listener) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = (int) UIUtils.dip2Px(mContext, 5);
            params.bottomMargin = (int) UIUtils.dip2Px(mContext, 5);
            Spinner spinner = new Spinner(mContext);
            mSpinnerContainer.addView(spinner, params);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, spinnerItems);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            adapters.add(adapter);
            items.add(spinnerItems);
            spinners.add(spinner);
            if (listener != null) {
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (spinnerItems.size() - 1 >= position && position >= 0) {
                            listener.onItemSelect(position, spinnerItems.get(position));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        }

        /**
         * will select item before set listener
         * @param spinnerItems
         * @param listener
         * @param selectedIndex
         */
        public void addSpinnerItemAndSelect(List<String> spinnerItems, OnItemSelectListener listener, int selectedIndex) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = (int) UIUtils.dip2Px(mContext, 5);
            params.bottomMargin = (int) UIUtils.dip2Px(mContext, 5);
            Spinner spinner = new Spinner(mContext);
            mSpinnerContainer.addView(spinner, params);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, spinnerItems);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            adapters.add(adapter);
            items.add(spinnerItems);
            spinners.add(spinner);
            spinner.setSelection(selectedIndex);
            if (listener != null) {
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (spinnerItems.size() - 1 >= position && position >= 0) {
                            listener.onItemSelect(position, spinnerItems.get(position));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        }

        public void updateItems(int index, List<String> items) {
            if (adapters == null || index < 0 ||
                    index >= adapters.size() ||
                    items == null || items.size() <= 0) {
                return;
            }
            ArrayAdapter<String> adapter = adapters.get(index);
            adapter.clear();
            adapter.addAll(items);
            adapter.notifyDataSetChanged();
        }

        /**
         * Spinner设置已选item
         *
         * @param selectedItem
         */
        public void setSelectedItem(int index, String selectedItem) {
            if (index < 0 || index >= spinners.size()) {
                return;
            }
            List<String> item = items.get(index);
            Spinner spinner = spinners.get(index);
            if (item == null || spinner == null) {
                return;
            }
            int size = item.size();
            for (int i = 0; i < size; i++) {
                if (TextUtils.equals(item.get(i), selectedItem)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }

        /**
         * Spinner设置已选item
         */
        public void setSelection(int index, int itemIndex) {
            if (index < 0 || index >= spinners.size()) {
                return;
            }
            List<String> item = items.get(index);
            Spinner spinner = spinners.get(index);
            if (item == null || spinner == null) {
                return;
            }
            spinner.setSelection(itemIndex);
        }

        @Override
        public void onItemSet() {
            mText.setText(mItem.debugText);
        }
    }

    public static class SeparatorViewHolder extends ViewHolder {
        public SeparatorViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onItemSet() {

        }
    }

    public interface OnItemSelectListener {
        void onItemSelect(int position, String selectItem);
    }
}
