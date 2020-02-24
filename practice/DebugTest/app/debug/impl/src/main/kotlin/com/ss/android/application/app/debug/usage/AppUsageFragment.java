package com.ss.android.application.app.debug.usage;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageStats;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bytedance.i18n.business.debug.R;
import com.bytedance.i18n.business.framework.legacy.service.usage.IAppUsageHelper;
import com.bytedance.i18n.claymore.ClaymoreServiceLoader;
import com.ss.android.framework.async.WeakHandler;
import com.ss.android.framework.page.ArticleAbsFragment;
import com.ss.android.network.threadpool.SSThreadPoolProvider;
import com.ss.android.uilib.toast.ToastUtil;
import com.ss.android.utils.app.AppUtils;


/**
 * AppUsageFragment
 * Created by zhaoshe on 2016/11/21.
 */
public class AppUsageFragment extends ArticleAbsFragment implements WeakHandler.IHandler {
    private static final int STATS_FINISH = 1;
    private static final int STATS_FAILED = 2;
    private static final int EXTRA_FINISH = 3;
    private static final int EXTRA_FAILED = 4;
    private Handler mHandler = new WeakHandler(this);

    private SparseArray<TextView> mViewMap = new SparseArray<>();

    private ProgressDialog mProgressDialog;
    private int mShowCount;

    private long mRamSize;
    private long mRomSize;
    private long mSdSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getContext();
        mRamSize = ClaymoreServiceLoader.loadFirstOrNull(IAppUsageHelper.class).getRamSize(context);
        mRomSize = ClaymoreServiceLoader.loadFirstOrNull(IAppUsageHelper.class).getRomSize(context);
        mSdSize = ClaymoreServiceLoader.loadFirstOrNull(IAppUsageHelper.class).getExtStorageSize(context);
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_usage_fragment, container, false);

        mViewMap.put(R.id.code_size, (TextView) view.findViewById(R.id.code_size));
        mViewMap.put(R.id.cache_size, (TextView) view.findViewById(R.id.cache_size));
        mViewMap.put(R.id.data_size, (TextView) view.findViewById(R.id.data_size));
        mViewMap.put(R.id.ex_code_size, (TextView) view.findViewById(R.id.ex_code_size));
        mViewMap.put(R.id.ex_cache_size, (TextView) view.findViewById(R.id.ex_cache_size));
        mViewMap.put(R.id.ex_data_size, (TextView) view.findViewById(R.id.ex_data_size));
        mViewMap.put(R.id.extra, (TextView) view.findViewById(R.id.extra));
        ((TextView) view.findViewById(R.id.ram_size)).setText(ClaymoreServiceLoader.loadFirstOrNull(IAppUsageHelper.class).formatSize(getContext(), mRamSize));
        ((TextView) view.findViewById(R.id.rom_size)).setText(ClaymoreServiceLoader.loadFirstOrNull(IAppUsageHelper.class).formatSize(getContext(), mRomSize));
        ((TextView) view.findViewById(R.id.sd_size)).setText(ClaymoreServiceLoader.loadFirstOrNull(IAppUsageHelper.class).formatSize(getContext(), mSdSize));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshData();
    }

    public void refreshData() {
        showLoading(2);
        ClaymoreServiceLoader.loadFirstOrNull(IAppUsageHelper.class).getStats(getContext(), new IPackageStatsObserver.Stub() {
            @Override
            public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) {
                mHandler.sendMessage(Message.obtain(mHandler, succeeded ? STATS_FINISH : STATS_FAILED, 0, 0, pStats));
            }
        });
        SSThreadPoolProvider.backgroundSubmit(new Runnable() {
            @Override
            public void run() {
                String extra = parseExtra();
                mHandler.sendMessage(Message.obtain(mHandler, EXTRA_FINISH, 0, 0, extra));
            }
        });
    }

    private void showLoading(int count) {
        mShowCount += count;
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("loading...");
        }

        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissLoading(int count) {
        mShowCount -= count;
        if (mShowCount < 0) {
            mShowCount = 0;
        }
        if (mShowCount == 0 && mProgressDialog != null && mProgressDialog.isShowing()) {
            AppUtils.safeDismissDialog(mProgressDialog);
        }
    }

    private String parseExtra() {
        return "";
    }

    @Override
    public void handleMsg(Message msg) {
        if (!isAdded()) {
            return;
        }
        dismissLoading(1);

        switch (msg.what) {
            case STATS_FINISH:
                PackageStats pStats = (PackageStats) msg.obj;
                Context context = getContext();
                mViewMap.get(R.id.code_size).setText(ClaymoreServiceLoader.loadFirstOrNull(IAppUsageHelper.class).formatSize(context, pStats.codeSize));
                mViewMap.get(R.id.cache_size).setText(ClaymoreServiceLoader.loadFirstOrNull(IAppUsageHelper.class).formatSize(context, pStats.cacheSize));
                mViewMap.get(R.id.data_size).setText(ClaymoreServiceLoader.loadFirstOrNull(IAppUsageHelper.class).formatSize(context, pStats.dataSize));
                mViewMap.get(R.id.ex_code_size).setText(ClaymoreServiceLoader.loadFirstOrNull(IAppUsageHelper.class).formatSize(context, pStats.externalCodeSize));
                mViewMap.get(R.id.ex_cache_size).setText(ClaymoreServiceLoader.loadFirstOrNull(IAppUsageHelper.class).formatSize(context, pStats.externalCacheSize));
                mViewMap.get(R.id.ex_data_size).setText(ClaymoreServiceLoader.loadFirstOrNull(IAppUsageHelper.class).formatSize(context, pStats.externalDataSize));
                break;
            case STATS_FAILED:
                ToastUtil.toast("get stats failed", Toast.LENGTH_LONG);
                break;
            case EXTRA_FINISH:
                mViewMap.get(R.id.extra).setText((String) msg.obj);
                break;
            case EXTRA_FAILED:
                ToastUtil.toast("get details failed", Toast.LENGTH_LONG);
                break;
        }
    }
}
