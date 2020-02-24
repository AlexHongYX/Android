package com.ss.android.application.app.debug.fpsmeter;

import android.os.Build;
import androidx.annotation.RequiresApi;
import android.view.Choreographer;

import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by yihuaqi on 11/23/16.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class FpsMeter implements Choreographer.FrameCallback{
    private Choreographer mChoreographer;
    private long mInterval = 1000;
    private PublishSubject<Double> mFpsProducer;
    public FpsMeter() {
        mChoreographer = Choreographer.getInstance();
        mFpsProducer = PublishSubject.create();
    }

    public void start() {
        mChoreographer.postFrameCallback(this);
    }

    public void stop() {
        mChoreographer.removeFrameCallback(this);
    }

    public void setInterval(long span) {
        mInterval = span;
    }

    public Subscription subscribeFpsMeter(final Callback callback) {
        return mFpsProducer.subscribeOn(Schedulers.immediate())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Double>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Double aDouble) {
                    callback.onFpsCalculated(aDouble);
                }
            });
    }

    private long mRenderCount;
    private long mFrameStartTime;

    @Override
    public void doFrame(long frameTimeNanos) {
        mChoreographer.postFrameCallback(this);

        long currentFrameTime = TimeUnit.NANOSECONDS.toMillis(frameTimeNanos);
        if (mFrameStartTime == 0) {
            mFrameStartTime = currentFrameTime;
            return;
        }
        if (frameTimeNanos > 0) {
            long timeSpan = currentFrameTime - mFrameStartTime;
            mRenderCount++;

            if (timeSpan > mInterval) {
                double fps = mRenderCount * 1000 / (double) timeSpan;
                mFrameStartTime = currentFrameTime;
                mRenderCount = 0;
                mFpsProducer.onNext(fps);
            }
        }
    }

    public interface Callback {
        void onFpsCalculated(double fps);
    }
}
