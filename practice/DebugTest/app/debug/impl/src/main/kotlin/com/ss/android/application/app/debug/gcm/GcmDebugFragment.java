package com.ss.android.application.app.debug.gcm;

import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bytedance.i18n.business.debug.R;
import com.bytedance.i18n.business.framework.legacy.service.constants.MessageModelConstants;
import com.bytedance.i18n.business.framework.push.service.IJobManager;
import com.bytedance.i18n.business.framework.push.service.IJobModel;
import com.bytedance.i18n.claymore.ClaymoreServiceLoader;
import com.fcm.push.service.SSGcmListenerService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.ss.android.application.app.core.BaseApplication;
import com.ss.android.application.app.notify.model.GcmMessageModel;
import com.ss.android.framework.page.ArticleAbsFragment;
import com.ss.android.framework.sharedpref.MultiProcessSharedPrefModel;
import com.ss.android.pushmanager.thirdparty.IPushDepend;
import com.ss.android.uilib.toast.ToastUtil;
import com.ss.android.uilib.utils.UIUtils;
import com.ss.android.utils.GsonProvider;
import com.ss.android.utils.kit.Logger;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.bytedance.i18n.business.framework.legacy.service.constants.MessageModelConstants.DELAY_SHOW_NOT_HIDE;
import static com.bytedance.i18n.business.framework.legacy.service.constants.MessageModelConstants.DELAY_SHOW_STRATEGY_TYPE_ENABLE;

/**
 * Created by yihuaqi on 3/14/17.
 */

public class GcmDebugFragment extends ArticleAbsFragment {

    private Button mSendBtn;
    private Random mRandom;
    private Gson mGson;
    private CheckBox mHasImageCB;
    private CheckBox mNewStyleCB;
    private CheckBox mHasTitleCB;
    private CheckBox mDarkThemeCB;
    private CheckBox mShowLargeImageCB;
    private CheckBox mShowVideoTypeCB;
    private CheckBox mShowRightButtonCB;
    private CheckBox mEnableDelayShowCB;
    private CheckBox mInteractiveCB;
    private CheckBox mDebugShowPayloadCB;
    private CheckBox mDisablePushFilterCB;
    private CheckBox mShowFloatingWindowCB;
    private CheckBox mShowLockScreenCB;
    private CheckBox mDisableMultiReachFilterCB;
    private EditText mMaxLine;
    private TextView mFcmTokenTV;
    private EditText mLocalPushIntervalEdt;
    private EditText mLocalPushDivisorTestEdt;
    private Button mLocalPushBtn;
    private Button mLocalPushDivisorTestBtn;

    private static final int MESSAGE_DETAIL = 1;
    private static final int MESSAGE_COMMENT_DETAIL = 2;
    private static final int MESSAGE_COMMENT_DIGG = 3;

    private GcmDebugModel mModel = GcmDebugModel.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRandom = new Random();
        mGson = GsonProvider.getDefaultGson();
        View view = inflater.inflate(R.layout.gcm_debug_fragment, container, false);
        mSendBtn = (Button) view.findViewById(R.id.send_btn);
        mHasImageCB = (CheckBox) view.findViewById(R.id.debug_gcm_has_image);
        mNewStyleCB = (CheckBox) view.findViewById(R.id.debug_gcm_new_style);
        mHasTitleCB = (CheckBox) view.findViewById(R.id.debug_gcm_has_title);
        mDarkThemeCB = (CheckBox) view.findViewById(R.id.debug_dark_theme);
        mShowLargeImageCB = (CheckBox) view.findViewById(R.id.debug_gcm_show_large_image);
        mShowVideoTypeCB = (CheckBox) view.findViewById(R.id.show_video_type);
        mShowRightButtonCB = (CheckBox) view.findViewById(R.id.show_right_button);
        mEnableDelayShowCB = (CheckBox) view.findViewById(R.id.enable_delay_show);
        mInteractiveCB = (CheckBox) view.findViewById(R.id.interactive_push);
        mDebugShowPayloadCB = (CheckBox) view.findViewById(R.id.debug_show_payload);
        mDisablePushFilterCB = (CheckBox) view.findViewById(R.id.disable_push_filter);
        mShowFloatingWindowCB = (CheckBox) view.findViewById(R.id.show_floating_window);
        mShowLockScreenCB = (CheckBox) view.findViewById(R.id.show_lock_screen);
        mDisableMultiReachFilterCB = (CheckBox) view.findViewById(R.id.disable_multi_reach_filter);
        mMaxLine = (EditText) view.findViewById(R.id.debug_max_line);
        mFcmTokenTV = (TextView) view.findViewById(R.id.debug_fcm_token);
        mLocalPushIntervalEdt = (EditText) view.findViewById(R.id.local_pull_interval_edt);
        mLocalPushDivisorTestEdt = (EditText) view.findViewById(R.id.local_pull_debug_divisor_edt);
        mLocalPushBtn = (Button) view.findViewById(R.id.send_local_pull_btn);
        mLocalPushDivisorTestBtn = (Button) view.findViewById(R.id.set_local_push_debug_divisor);
        setupCheckbox(mHasImageCB, mModel.mHasImage);
        setupCheckbox(mNewStyleCB, mModel.mNewStyle);
        setupCheckbox(mHasTitleCB, mModel.mHasTitle);
        setupCheckbox(mDarkThemeCB, mModel.mDarkTheme);
        setupCheckbox(mShowVideoTypeCB, mModel.mShowVideoType);
        setupCheckbox(mShowRightButtonCB, mModel.mShowRightButton);
        setupCheckbox(mEnableDelayShowCB, mModel.mEnableDelayShow);
        setupCheckbox(mShowLargeImageCB, mModel.mShowLargeImage);
        setupCheckbox(mDebugShowPayloadCB, mModel.mShowPayload);
        setupCheckbox(mDisablePushFilterCB, mModel.mDisablePushFilter);
        setupCheckbox(mShowFloatingWindowCB, mModel.mShowFloatingWindow);
        setupCheckbox(mShowLockScreenCB, mModel.mShowLockScreen);
        setupCheckbox(mDisableMultiReachFilterCB, mModel.mDisableMultiReachFilter);
        setupEditText(mMaxLine, mModel.mMaxLine);
        mLocalPushIntervalEdt.setText("5");
        mLocalPushDivisorTestEdt.setText(String.valueOf(ClaymoreServiceLoader.loadFirstOrNull(IJobModel.class).getLocalPushDebugDivisor().getValue()));
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mLocalPushBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String value = mLocalPushIntervalEdt.getText().toString();
                    long interval = 0;
                    if (!TextUtils.isEmpty(value)) {
                        interval = Long.parseLong(value);
                    }
                    IJobManager jobManager = ClaymoreServiceLoader.loadFirstOrNull(IJobManager.class);
                    jobManager.cancelLocalPullTask();
                    jobManager.scheduleLocalPullTask(TimeUnit.SECONDS.toMillis(interval));
                } catch (Exception e) {

                }
            }
        });

        mInteractiveCB.setChecked(mModel.mInteractive.getValue() > 0);
        mInteractiveCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mModel.mInteractive.setValue(1);
                    AlertDialog.Builder builder = UIUtils.getThemedAlertDlgBuilder(getActivity());
                    builder.setSingleChoiceItems(R.array.interaction_type, mModel.mInteractive.getValue() - 1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mModel.mInteractive.setValue(++which);
                        }
                    });
                    builder.setPositiveButton(R.string.confirm, null);
                    builder.setNegativeButton(R.string.cancel, null);
                    builder.show();
                } else {
                    mModel.mInteractive.setValue(0);
                }

            }
        });

        mLocalPushDivisorTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String value = mLocalPushDivisorTestEdt.getText().toString();
                    if (!TextUtils.isEmpty(value)) {
                        Logger.d("MY_TEST", "mLocalPushIntervalEdt Set " + Integer.valueOf(value));
                        ClaymoreServiceLoader.loadFirstOrNull(IJobModel.class).getLocalPushDebugDivisor().setValue(Integer.valueOf(value));
                    }
//                            FirebaseInstanceId.getInstance().deleteInstanceId();

                } catch (Exception e) {
                    Logger.e("SSGcmListenerService", "", e);
                }
            }
        });


        String token = FirebaseInstanceId.getInstance().getToken();
        mFcmTokenTV.setText(token);
        Logger.i("MY_TEST", "\"to\":\"" + token + "\"");
        mFcmTokenTV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cm = (ClipboardManager)getContext().getSystemService(getContext().CLIPBOARD_SERVICE);
                cm.setText(mFcmTokenTV.getText());
                ToastUtil.toast("Token copied to clipboard", Toast.LENGTH_SHORT);
                return true;
            }
        });
        return view;
    }

    private GcmMessageModel generateGcmMessageModel() {
        GcmMessageModel result = new GcmMessageModel();
        GcmMessageModel.Extra extra = new GcmMessageModel.Extra();
        long groupId = 6468151064414650889L;
        result.sound = 0;
        result.imprId = 20000;
        result.openUrl = "sslocal://detail?log_extra={\"Impr ID\": 10000, \"push_type_test\": ugc_reply}&group_flags=0&group_id=6468151064414650889&item_id=6468151064414650889&groupid=6364791219851117059&aggr_type=0";
        if (mHasTitleCB.isChecked()) {
            result.title = "TopBuzz";
        } else {
            result.title = null;
            extra.useAppNameForEmptyTitle = 0;
        }
        result.type = "article";
        String text = "<b><b>\u67d0\u67d0\u67d0</b></b> replied your comment and said \"Hajji \ud83d\ude02\"";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mModel.mMaxLine.getValue(); i++) {
            sb.append(text);
            if (i != mModel.mMaxLine.getValue() - 1) {
                sb.append("\n");
            }
        }
        result.content = sb.toString();

        result.groupId = groupId;

        extra.badgeCount = 1;
        if (mHasImageCB.isChecked()) {
            extra.largeImageUrl = "http://p1.ipstatp.com/list/0058f1445eb0c041c0b8.webp";
            extra.smallImageUrl = "http://p1.ipstatp.com/list/0058f1445eb0c041c0b8.webp";
            if (mShowLargeImageCB.isChecked()) {
                extra.showLargeImage = true;
            }
        }
        extra.enableImprFilter = 0;
        if (mDarkThemeCB.isChecked()) {
            extra.bgColorStr = "#262626";
            extra.titleColorStr = "#999999";
            extra.textColorStr = "#FFFFFF";
        }
        if (mNewStyleCB.isChecked()) {
            extra.style = 1;
        }

        if (mShowFloatingWindowCB.isChecked()) {
            extra.isFloatingMode = true;
            extra.lightUpScreenDuration = 10.0f;
        }

        if (mInteractiveCB.isChecked()) {
            String replacedStr = "message_detail";
            String appendStr = "";
            switch (mModel.mInteractive.getValue()) {
                case MESSAGE_DETAIL:
                    replacedStr = "message_detail";
                    break;
                case MESSAGE_COMMENT_DETAIL:
                    replacedStr = "message_comment_detail";
                    appendStr = "&comment_id=6468159979655267082";
                    break;
                case MESSAGE_COMMENT_DIGG:
                    replacedStr = "message_comment_digg";
                    appendStr = "&comment_id=6468159979655267082";
                    break;
            }

            result.openUrl = result.openUrl.replace("detail", replacedStr);
            result.openUrl += "&push_view=interaction";
            result.openUrl += appendStr;
        }

        if (mShowVideoTypeCB.isChecked()) {
            extra.isVideo = 1;
            extra.duration = 200;
            extra.showVideoStyle = 1;
            extra.showOriginScaleImage = true;
        }

        if (mEnableDelayShowCB.isChecked()) {
            extra.delayShowStrategyType = DELAY_SHOW_STRATEGY_TYPE_ENABLE;
            extra.delayShowInterval = 10L;
            extra.delayShowTimeout = 120L;
            extra.delayShowType = DELAY_SHOW_NOT_HIDE;

        }

        if (mShowLockScreenCB.isChecked()) {
            extra.floatingWindowMode = MessageModelConstants.FLOATING_WINDOW_MODEL_LOCK_SCREEN_WINDOW;
            extra.lightUpScreenDuration = 10.0f;
        }

        if (mShowRightButtonCB.isChecked()) {
            extra.mShowRightButton = 1;
        }

        extra.mPushArticleClass = "class";
        extra.mPushArticleType = "type";
        extra.mediaId = mRandom.nextLong();
        result.extra = extra;
        result.id = mRandom.nextInt() / 10;

        result.openUrl = "sslocal://main?tab=Me";
        return result;
    }

    private void setupCheckbox(CheckBox cb, final MultiProcessSharedPrefModel.BooleanProperty p) {
        cb.setChecked(p.getValue());
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                p.setValue(isChecked);
            }
        });
    }

    private void setupEditText(EditText et, final MultiProcessSharedPrefModel.IntProperty p) {
        et.setText(String.valueOf(p.getValue()));
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Integer value = Integer.parseInt(s.toString());
                    p.setValue(value);
                } catch (NumberFormatException e) {
                    ToastUtil.toast("Please input an int", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    public static class GcmDebugModel extends MultiProcessSharedPrefModel {
        private static volatile GcmDebugModel sIntance = null;

        public static GcmDebugModel getInstance() {
            if (null == sIntance) {
                synchronized (GcmDebugFragment.class) {
                    if (null == sIntance) {
                        sIntance = new GcmDebugModel();
                    }
                }
            }
            return sIntance;
        }

        @Override
        protected String getPrefName() {
            return "gcm_debug_model";
        }

        @Override
        protected int getMigrationVersion() {
            return 0;
        }

        @Override
        protected void onMigrate(int version) {

        }

        public BooleanProperty mHasImage = new BooleanProperty("has_image", true);
        public BooleanProperty mNewStyle = new BooleanProperty("new_style", true);
        public BooleanProperty mHasTitle = new BooleanProperty("has_title", false);
        public BooleanProperty mDarkTheme = new BooleanProperty("dark_theme", false);
        public BooleanProperty mShowVideoType = new BooleanProperty("show_video_type", false);
        public BooleanProperty mEnableDelayShow = new BooleanProperty("enable_delay_show", true);
        public IntProperty mInteractive = new IntProperty("interactive", 0);
        public BooleanProperty mShowLargeImage = new BooleanProperty("show_large_image", true);
        public BooleanProperty mShowPayload = new BooleanProperty("show_payload", false);
        public BooleanProperty mDisablePushFilter = new BooleanProperty("disable_push_filter", false);
        public BooleanProperty mShowFloatingWindow = new BooleanProperty("show_floating_window", false);
        public BooleanProperty mShowLockScreen = new BooleanProperty("show_lock_screen", false);
        public BooleanProperty mShowRightButton = new BooleanProperty("show_right_button", false);
        public BooleanProperty mDisableMultiReachFilter = new BooleanProperty("disable_multi_reach_filter", false);
        public IntProperty mMaxLine = new IntProperty("max_line", 2);
    }
}
