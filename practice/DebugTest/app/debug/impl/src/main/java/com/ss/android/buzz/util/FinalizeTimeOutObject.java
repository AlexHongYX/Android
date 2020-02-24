package com.ss.android.buzz.util;

import com.ss.android.network.threadpool.SSThreadPoolProvider;
import com.ss.android.utils.kit.Logger;

import org.jetbrains.annotations.TestOnly;

/**
 * Created by @malingyi on 2019-08-02.
 *
 * @Note please don't hesitate to connect: malinyi@bytedance.com if you have any
 * problems or advices, welcome, welcome, welcome.
 * @Warning: only for test!
 */
public class FinalizeTimeOutObject {

    @Override
    @TestOnly
    protected void finalize() throws Throwable {
        super.finalize();
        Logger.d("FinalizeTimeOutObject", "finalize,  this = " + this);
        Thread.sleep(120000);
    }

    @TestOnly
    public static void testFinalize() {
        SSThreadPoolProvider.commonSubmit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 150000; i++) {
                    FinalizeTimeOutObject finalizeObject = new FinalizeTimeOutObject();
                    finalizeObject.toString();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
