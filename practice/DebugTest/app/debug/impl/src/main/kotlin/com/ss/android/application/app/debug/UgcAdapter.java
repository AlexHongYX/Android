//package com.ss.android.application.app.debug;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.CompoundButton;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//
//import com.bytedance.crashtrigger.CrashTrigger;
//import com.bytedance.crashtrigger.config.CrashTriggerConfig;
//import com.bytedance.i18n.appbrandservice.IAppBrandService;
//import com.bytedance.i18n.business.debug.R;
//import com.bytedance.i18n.business.framework.legacy.service.constants.AbsConstants;
//import com.bytedance.i18n.business.framework.legacy.service.constants.ArticleBaseBuildConfig;
//import com.bytedance.i18n.business.framework.legacy.service.setting.IDebugSettings;
//import com.bytedance.i18n.business.framework.push.service.IJobModel;
//import com.bytedance.i18n.claymore.ClaymoreServiceLoader;
//import com.bytedance.memory.test.OOMMaker;
//import com.bytedance.router.SmartRouter;
//import com.ss.android.application.app.core.BaseApplication;
//import com.ss.android.application.app.debug.ad.AdDebugActivity;
//import com.ss.android.application.app.debug.ad.NewAdDebugPriorityFragment;
//import com.ss.android.application.app.debug.data.BuzzCitiesProvider;
//import com.ss.android.application.app.debug.data.CityInfo;
//import com.ss.android.application.app.debug.dynamics.DebugDynamicsActivity;
//import com.ss.android.application.app.debug.file.FileStructureTestActivity;
//import com.ss.android.application.app.debug.gcm.GcmDebugActivity;
//import com.ss.android.application.app.debug.location.LocationTestMainActivity;
//import com.ss.android.application.app.debug.net.DebugNetActivity;
//import com.ss.android.application.app.debug.qrscanner.QrCodeScannerActivity;
//import com.ss.android.application.app.feedback.FeedbackServiceManager;
//import com.ss.android.application.app.feedback.constants.FeedbackConstants;
//import com.ss.android.buzz.live.IHeloLiveManager;
//import com.ss.android.buzz.ug.polaris.service.IPolarisDialogService;
//import com.ss.android.buzz.util.FinalizeTimeOutObject;
//import com.ss.android.i18n.apkinjector.impl.v2.ApkInjectWrapper;
//import com.ss.android.module.verify_applog.AppLogVerifyClient;
//import com.ss.android.uilib.toast.ToastUtil;
//import com.ss.android.uilib.utils.UIUtils;
//import com.ss.android.widget.translation.ITranslationService;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.List;
//
//public class UgcAdapter extends DebugStandardAdapter {
//
//    public UgcAdapter(List<DebugDataModel> list, Context context){
//        super(list,context);
//    }
//
//    @Override
//    public void bindCheckBoxViewHolder(@NotNull CheckBoxViewHolder holder, @NotNull DebugDataModel item) {
//        final IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
//        switch (item.getText()) {
//            case "Force Jump To Post": {
//                holder.mCheckbox.setChecked(setting.getForceJumpToPost());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                    setting.setForceJumpToPost(isChecked);
//                });
//                break;
//            }
//            case "ForceQuickUpload": {
//                holder.mCheckbox.setChecked(setting.getForceUseQuickUpload());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                    setting.setForceUseQuickUpload(isChecked);
//                });
//                break;
//            }
//            case "Enable UgcChallenge": {
//                holder.mCheckbox.setChecked(setting.getEnableUgcChallenge());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) ->
//                        setting.setEnableUgcChallenge(isChecked));
//                break;
//            }
//            case "Enable Test Effect": {
//                holder.mCheckbox.setChecked(setting.getEnableTestEffect());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) ->
//                        setting.setEnableTestEffect(isChecked));
//                break;
//            }
//            case "Use UGC Preload":
//                holder.mCheckbox.setChecked(setting.getUgcPreload());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setUseUgcPreload(isChecked);
//                    }
//                });
//                break;
//            case "Use default mv preload config(first check Use UGC Preload)":
//                holder.mCheckbox.setChecked(setting.getUseDefaultPreloadEffectsConfig());
//                holder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        setting.setUseDefaultPreloadEffectsConfig(isChecked);
//                    }
//                });
//                break;
//            case "Use Old Ugc Tools": {
//                holder.mCheckbox.setChecked(setting.getForceUseOldUgcTools());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                    setting.setForceUseOldUgcTools(isChecked);
//                });
//                break;
//            }
//            case "UGC disable image compression": {
//                holder.mCheckbox.setChecked(setting.getDisableImageCompress());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) ->
//                        setting.setDisableImageCompress(isChecked)
//                );
//                break;
//            }
//            case "UGC use new Luban compute size": {
//                holder.mCheckbox.setChecked(setting.useNewLubanComputeSize());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) ->
//                        setting.setUseNewLubanComputeSize(isChecked)
//                );
//                break;
//            }
//            case "Force show MV":
//                holder.mCheckbox.setChecked(setting.getForceShowMV());
//                holder.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                    setting.setForceShowMV(isChecked);
//                });
//                break;
//        }
//    }
//
//    @Override
//    public void bindEditTextViewHolder(@NotNull EditTextViewHolder holder, @NotNull DebugDataModel item) {
//        switch (item.getText()) {
//            case "UGC Entrance Type(-1,0,1,2)":
//                try {
//                    int type = Integer.parseInt(holder.getText());
//                    ClaymoreServiceLoader.loadFirst(IDebugSettings.class).setUgcEntranceType(type);
//                } catch (NumberFormatException e) {
//                    ClaymoreServiceLoader.loadFirst(IDebugSettings.class).setUgcEntranceType(-1);
//                }
//                break;
//        }
//    }
//
//    @Override
//    public void bindTextViewViewHolder(@NotNull TextViewViewHolder holder, @NotNull DebugDataModel item) {
//        holder.setOnClickListener(new View.OnClickListener() {
//            IDebugSettings setting = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class);
//
//            @Override
//            public void onClick(View v) {
//                switch (item.getText()) {
//                    case "Music": {
////                        Intent intent = new Intent(mContext, MusicTestActivity.class);
////                        mContext.startActivity(intent);
//                        break;
//                    }
//                }
//            }
//        });
//    }
//}
