package com.desu.experiments.view.activity.rx;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.desu.experiments.R;
import com.desu.experiments.databinding.ActivityRxClickEventsBinding;
import com.desu.experiments.view.widget.logView.LogView;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxClickEvents extends AppCompatActivity {

    @Bind(R.id.btn_start_operation)
    Button _tapBtn;
    @Bind(R.id.log_list)
    LogView logView;

    private Subscription _subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRxClickEventsBinding activityRxClickEventsBinding = DataBindingUtil.setContentView(this, R.layout.activity_rx_click_events);
        ButterKnife.bind(this);
        RxClickEventsUser rxClickEventsUser = new RxClickEventsUser();
        activityRxClickEventsBinding.setRxClickEventsUser(rxClickEventsUser);


        rxClickEventsUser.setOnClickListener(this::flatMap);
        rxClickEventsUser.setText(getString(R.string.msg_demo_buffer));


    }

    @Override
    public void onStart() {
        super.onStart();
        _subscription = _getBufferedSubscription();
    }

    @Override
    public void onPause() {
        super.onPause();
        _subscription.unsubscribe();
    }


    // -----------------------------------------------------------------------------------
    // Main Rx entities
    private Subscription _getBufferedSubscription() {
        return RxView.clicks(_tapBtn)
                .map(onClickEvent -> {
                    logView.logWithThreadName("GOT A TAP");
                    return 1;
                })
                .buffer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Integer>>() {

                    @Override
                    public void onCompleted() {
                        // fyi: you'll never reach here
                        logView.logWithThreadName("----- onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        logView.logWithThreadName("Dang error! check your logs");
                    }

                    @Override
                    public void onNext(List<Integer> integers) {
                        logView.logWithThreadName("--------- onNext");
                        if (integers.size() > 0) {
                            logView.logWithThreadName(String.format("%d taps", integers.size()));
                        } else {
                            logView.logWithThreadName("--------- No taps received ");
                        }
                    }
                });
    }

    void flatMap(View v) {
        addSignatureToGreeting(helloWorld())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        query()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(Observable::from)
                .subscribe(s -> {
                    logView.logWithThreadName("MainActivity url: " + s);
                });
    }

    private Observable<String> addSignatureToGreeting(Observable<String> observable) {
        return observable.map(s -> s + " - Csaba");
    }

    private Subscriber<String> subscriber = new Subscriber<String>() {
        @Override
        public void onCompleted() {
            logView.logWithThreadName("in subscriber's onCompleted()");
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String s) {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    };

    Observable<String> helloWorld() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello World!");
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread());
    }

    Observable<List<String>> query() {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                List<String> urls = new ArrayList<>();
                urls.add("http://google.com");
                urls.add("http://trakt.tv");
                urls.add("http://theverge.com");

                subscriber.onNext(urls);
            }
        });
    }

}
