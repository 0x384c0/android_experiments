package com.desu.experiments.view.activity.rx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.desu.experiments.R;
import com.desu.experiments.view.widget.logView.LogView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static java.lang.String.format;

public class RxInputTextDebounce extends AppCompatActivity {

    @Bind(R.id.log_list)
    LogView logView;
    @Bind(R.id.input_txt_debounce)
    EditText _inputSearchText;

    private Subscription _subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_input_text_debounce);

        ButterKnife.bind(this);
/*
        Scheduler scheduler = Schedulers.newThread();
        Observable<TextViewTextChangeEvent> textChangeObservable = RxTextView.textChangeEvents(_inputSearchText);
        Observable<TextViewTextChangeEvent> _observable = textChangeObservable
                .debounce(400, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());
        rxRx_subscription = _observable.subscribe(_getSearchObserver());
*/
        _subscription = RxTextView.textChangeEvents(_inputSearchText)//
                .debounce(400, TimeUnit.MILLISECONDS)// default Scheduler is Computation
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(_getSearchObserver());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (_subscription != null) {
            _subscription.unsubscribe();
        }
    }


    // -----------------------------------------------------------------------------------
    // Main Rx entities

    private Observer<TextViewTextChangeEvent> _getSearchObserver() {
        return new Observer<TextViewTextChangeEvent>() {
            @Override
            public void onCompleted() {
                System.out.println("--------- onComplete");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("--------- onError\n" + e + "\n---------");
            }

            @Override
            public void onNext(TextViewTextChangeEvent onTextChangeEvent) {
                logView.log(format("Searching for %s", onTextChangeEvent.text().toString()));
            }
        };
    }
}
