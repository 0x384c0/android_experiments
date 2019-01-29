package com.desu.experiments.view.activity.rx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.desu.experiments.R;
import com.desu.experiments.view.widget.logView.LogView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxSchedulers extends AppCompatActivity {

    @Bind(R.id.progress_operation_running)
    ProgressBar _progress;
    @Bind(R.id.log_list)
    LogView logView;
    private Subscription _subscription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_schedulers);
        ButterKnife.bind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (_subscription != null) {
            _subscription.unsubscribe();
        }
    }

    public void startLongOperation(View v) {
        _progress.setVisibility(View.VISIBLE);
        logView.logWithThreadName("Button Clicked --------------");

        _subscription = _getObservable()//
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(_getObserver());                             // Observer
    }

    private Observable<Boolean> _getObservable() {
        return Observable.just(true).map(aBoolean -> {
            logView.logWithThreadName("Within Observable");
            _doSomeLongOperation_thatBlocksCurrentThread();
            return aBoolean;
        });
    }

    /**
     * Observer that handles the result through the 3 important actions:
     * <p/>
     * 1. onCompleted
     * 2. onError
     * 3. onNext
     */
    private Observer<Boolean> _getObserver() {
        return new Observer<Boolean>() {

            @Override
            public void onCompleted() {
                logView.logWithThreadName("On complete =============");
                _progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                logView.logWithThreadName(String.format("Boo! Error %s", e.getMessage()));
                _progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNext(Boolean bool) {
                logView.logWithThreadName(String.format("onNext with return value \"%b\"", bool));
            }
        };
    }

    // -----------------------------------------------------------------------------------
    // Method that help wiring up the example (irrelevant to RxJava)


    // -----------------------------------------------------------------------------------
    // Method that help wiring up the example (irrelevant to RxJava)

    private void _doSomeLongOperation_thatBlocksCurrentThread() {
        logView.logWithThreadName("performing long operation");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
