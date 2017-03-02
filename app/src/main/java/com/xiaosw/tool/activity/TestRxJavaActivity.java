package com.xiaosw.tool.activity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.library.utils.LogUtil;
import com.xiaosw.tool.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class TestRxJavaActivity extends BaseAppCompatActivity {

    /** @see TestRxJavaActivity#getClass().getSimpleName() */
    private static final String TAG = "TestRxJavaActivity";

    @BindView(R.id.acet_content)
    AppCompatEditText acet_content;

    @BindView(R.id.bt_test_rxjava)
    Button bt_test_rxjava;

    private long mStartTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_rx_java);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle(TAG);
        RxView.clicks(bt_test_rxjava)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
//                    LogUtil.e(TAG, "call: " + (System.currentTimeMillis() - mStartTime));
                    mStartTime = System.currentTimeMillis();
                    Observable
                        .create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                LogUtil.e(TAG, "OnSubscribe call: threadId = " + Thread.currentThread().getId());
                                subscriber.onNext("abcd");
                                subscriber.onCompleted();
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(String s) {

                            }
                        });
//                        .subscribe(new Action1<String>() {
//                            @Override
//                            public void call(String s) {
//                                LogUtil.e(TAG, "Action1 call: threadId = " + Thread.currentThread().getId() + ",result = " + s);
//                            }
//                        });
                }
            });

        RxTextView.textChanges(acet_content)
            .subscribe(new Action1<CharSequence>() {
                @Override
                public void call(CharSequence charSequence) {
                    LogUtil.e(TAG, "call: " + charSequence);
                }
            });

    }
}
