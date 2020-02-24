package com.ss.android.application.app.debug.fpsmeter;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bytedance.i18n.business.debug.R;

import java.util.Locale;

import rx.Subscription;

/**
 * Created by yihuaqi on 11/23/16.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class FpsMeterView {

    private static final FpsMeterView sInstance = new FpsMeterView();
    private FpsMeter mFpsMeter;
    private Subscription mSubscription;
    private WindowManager mWM;
    private View mFpsView;
    private TextView mFpsTextView;
    private boolean mIsPrepared;
    private WindowManager.LayoutParams mParams;
    private boolean mIsViewAdded;

    public static void show(Context context, long interval) {
        sInstance.prepare(context.getApplicationContext());
        sInstance.setInterval(interval);
        sInstance.play();
    }

    public static void hide() {
        sInstance.stop();
    }

    public static boolean isShowing() {
        return sInstance.mIsPrepared && sInstance.mIsViewAdded;
    }

    public FpsMeterView() {

    }

    private void setInterval(long interval) {
        if (mIsPrepared) {
            mFpsMeter.setInterval(interval);
        }
    }

    private void subscribe() {
        if (mIsPrepared) {
            unsubscribe();
            mSubscription = mFpsMeter.subscribeFpsMeter(new FpsMeter.Callback() {
                @Override
                public void onFpsCalculated(double fps) {
                    if (mIsPrepared && mIsViewAdded && mFpsTextView != null) {
                        mFpsTextView.setText(String.format(Locale.getDefault(),"%.2f", fps));
                    }
                }
            });
        }
    }

    private void unsubscribe() {
        if (mIsPrepared) {
            if (mSubscription != null && !mSubscription.isUnsubscribed()) {
                mSubscription.unsubscribe();
            }
            mSubscription = null;
        }
    }

    public void prepare(Context context) {
        if (!mIsPrepared) {
            mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            LayoutInflater inflater = LayoutInflater.from(context);
            mFpsView = inflater.inflate(R.layout.debug_fps_view, new RelativeLayout(context));
            mFpsTextView = (TextView) mFpsView.findViewById(R.id.debug_fps_tv);
            mIsPrepared = true;
            mParams = new WindowManager.LayoutParams();
            mParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            mParams.format = PixelFormat.TRANSLUCENT;
            mFpsMeter = new FpsMeter();
        }
    }

    public void play() {
        if (mIsPrepared && !mIsViewAdded) {
            mFpsMeter.start();
            subscribe();
            mWM.addView(mFpsView, mParams);
            mIsViewAdded = true;
        }
    }

    public void stop() {
        if (mIsPrepared && mIsViewAdded) {
            mFpsMeter.stop();
            unsubscribe();
            mWM.removeView(mFpsView);
            mIsViewAdded = false;
        }
    }

}
