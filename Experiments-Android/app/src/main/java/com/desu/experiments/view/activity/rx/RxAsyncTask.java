package com.desu.experiments.view.activity.rx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.desu.experiments.R;
import com.desu.experiments.model.stringBoolean;
import com.desu.experiments.view.widget.logView.LogView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class RxAsyncTask extends AppCompatActivity {

    @Bind(R.id.rx_progressBar)
    ProgressBar progressBar;
    @Bind(R.id.rx_asyn_textView)
    TextView textView;
    @Bind(R.id.log_list)
    LogView logView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_asynctask);
        ButterKnife.bind(this);
        rxPublishCreate();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        _subscriptions.clear();
    }

    private CompositeSubscription _subscriptions = new CompositeSubscription();

    public void rxAsyncOnClick(View view) {
        _subscriptions.add(
                rxAsync_getObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(rxAsync_getSubscriber())
        );
    }

    Observable<stringBoolean> rxAsync_getObservable() {
        return Observable.create(
                new Observable.OnSubscribe<stringBoolean>() {
                    @Override
                    public void call(Subscriber<? super stringBoolean> sub) {
                        rxAsync_doInBackground(sub);
                    }
                }
        );
    }
    Subscriber<stringBoolean> rxAsync_getSubscriber() {
        return new Subscriber<stringBoolean>() {
            @Override
            public void onNext(stringBoolean s) {
                if (s.b) rxAsync_onPostExecute(s.s);
                else rxAsync_onProgressUpdate(s.s);
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                rxAsync_Exception(e);
            }
        };
    }

    void rxAsync_doInBackground(Subscriber<? super stringBoolean> sub) {
        logView.logWithThreadName("rxAsync_doInBackground");
        sub.onNext(new stringBoolean("rxAsync_onProgressUpdate", false));
        sub.onNext(new stringBoolean("rxAsync_onProgressUpdate", false));
        sub.onNext(new stringBoolean("rxAsync_onProgressUpdate", false));
        sub.onNext(new stringBoolean("rxAsync_onPostExecute", true));
        sub.onCompleted();
    }
    void rxAsync_onProgressUpdate(String s) {
        logView.logWithThreadName(s);
    }
    void rxAsync_onPostExecute(String s) {
        logView.logWithThreadName(s);
    }
    void rxAsync_Exception(Throwable e) {
        logView.log(e.toString());
    }


    public void rxRxOnClick(View view) {
        _subscriptions.add(
                rxRx_getObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(rxRx_getObserver())
        );// Observer
    }

    Observable<String> rxRx_getObservable() {
        return Observable
                .just("in")
                .map(this::rxRx_doInBackground);
    }
    Observer<String> rxRx_getObserver() {
        return new Observer<String>() {

            @Override
            public void onCompleted() {
                rxRx_onPostExecute();
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(String bool) {
                logView.log(bool);
            }
        };
    }

    String rxRx_doInBackground(String s) {

        try {
            logView.log("-------------------rxRx_doInBackground start---------------------\n");
            Thread.sleep(3000);
            s += " out";
            logView.log("-------------------rxRx_doInBackground end---------------------\n");
        } catch (InterruptedException ignored) {
        }
        return s;
    }
    void rxRx_onPostExecute() {
        logView.logWithThreadName("rxRx_onPostExecute");
    }


    Subscriber<? super Object> subscriber;
    boolean subscriberFinished = true;//TODO find RX way to Single subscriber

    void rxPublishCreate() {
        _subscriptions.add(
                Observable
                        .create(sub -> subscriber = sub)
                                //.first()
                                //.debounce(5, TimeUnit.SECONDS)
                        .observeOn(Schedulers.io())
                        .subscribe(s -> {
                            subscriberFinished = false;
                            rxPublish_doInBackground();
                            subscriberFinished = true;
                        })
        );
    }
    public void rxPublishOnClick(View view) {
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            if (subscriberFinished) subscriber.onNext(null);
        }
    }

    Observable<String> rxPublish_getUiObservable(String string) {
        return Observable.create(
                s -> {
                    logView.logWithThreadName("rxPublish_getUiObservable - UI Update");
                    if (progressBar.getVisibility() == View.VISIBLE) {
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    textView.setText(string);

                }
        );
    }

    void rxPublish_doInBackground() {
        logView.logWithThreadName("rxAsync_doInBackground - progress bar visible");
        _subscriptions.add(
                rxPublish_getUiObservable("1st")
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe()
        );

        logView.logWithThreadName("rxAsync_doInBackground - sleep");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logView.logWithThreadName("rxAsync_doInBackground - progress bar invisible");
        _subscriptions.add(
                rxPublish_getUiObservable("2nd")
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe()
        );

        logView.logWithThreadName("rxAsync_doInBackground - quit");
    }


//    public void doInBackground(doInBackgroundInterface doInBackgroundInterface) {
//        Observable.create(subscriber -> doInBackgroundInterface.method())
//                .subscribeOn(Schedulers.io())
//                .subscribe();
//    }
//
//    public interface doInBackgroundInterface {
//        void method();
//    }

}
