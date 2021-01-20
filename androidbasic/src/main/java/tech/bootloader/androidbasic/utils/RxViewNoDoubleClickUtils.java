package tech.bootloader.androidbasic.utils;

import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.Observer;

public class RxViewNoDoubleClickUtils {
    /**
     * 防止重复点击
     *
     * @param target 目标view
     * @param listener 监听器
     */
    public static void preventRepeatedClick(final View target, final View.OnClickListener listener) {
        RxView.clicks(target).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Observer<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Object object) {
                listener.onClick(target);
            }
        });
    }


}
