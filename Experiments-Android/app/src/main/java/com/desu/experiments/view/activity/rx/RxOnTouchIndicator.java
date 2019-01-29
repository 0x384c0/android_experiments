package com.desu.experiments.view.activity.rx;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.desu.experiments.R;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.view.ViewAttachEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

public class RxOnTouchIndicator extends AppCompatActivity {

    private ViewGroup viewGroup;

    private TextView touchCountIndicator;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rx_on_touch_indicator);
        touchCountIndicator = (TextView) findViewById(R.id.touchCountIndicator);

        PublishSubject<MotionEvent> touchPublishSubject = PublishSubject.create();

        viewGroup = (ViewGroup) findViewById(android.R.id.content);
        viewGroup.setOnTouchListener((v, event) -> {
            touchPublishSubject.onNext(event);
            return true;
        });


/*
        //before lambda
        touchPublishSubject
                .takeUntil(RxView.attachEvents(viewGroup)
                                .filter(new Func1<ViewAttachEvent, Boolean>() {
                                    @Override
                                    public Boolean call(ViewAttachEvent e) {
                                        return e.kind() == ViewAttachEvent.Kind.DETACH;
                                    }
                                })
                )
                .doOnNext(new Action1<MotionEvent>() {
                    @Override
                    public void call(MotionEvent motionEvent) {
                        RxOnTouchIndicator.this.showTouch(motionEvent);
                    }
                })
                .map(new Func1<MotionEvent, MotionEvent>() {
                    @Override
                    public MotionEvent call(MotionEvent motionEvent) {
                        return MotionEvent.obtain(motionEvent);
                    }
                })
                .buffer(3L, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<MotionEvent>>() {
                    @Override
                    public void call(List<MotionEvent> motionEvents) {
                        RxOnTouchIndicator.this.showTouchCount(motionEvents);
                    }
                });
*/
        //after lambda
        touchPublishSubject
                .takeUntil(RxView.attachEvents(viewGroup)
                                .filter(e -> e.kind() == ViewAttachEvent.Kind.DETACH)
                )
                .doOnNext(RxOnTouchIndicator.this::showTouch)
                .map(MotionEvent::obtain)
                .buffer(3L, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(RxOnTouchIndicator.this::showTouchCount);


    }

    private void showTouch(MotionEvent motionEvent) {
        for (int i = 0; i < motionEvent.getPointerCount(); ++i) {
            float x = motionEvent.getX(i);
            float y = motionEvent.getY(i);

            ImageView touchIndicator = new ImageView(RxOnTouchIndicator.this);
            touchIndicator.setImageResource(R.drawable.touch);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(10, 10);
            params.leftMargin = (int) (x - 5);
            params.topMargin = (int) (y - 5);
            viewGroup.addView(touchIndicator, params);

            touchIndicator
                    .animate()
                    .alpha(0F)
                    .scaleXBy(25F)
                    .scaleYBy(25F)
                    .setDuration(1000L)
                    .setListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            viewGroup.removeView(touchIndicator);
                        }
                    });
        }
    }

    private void showTouchCount(List<MotionEvent> motionEvents) {
        Observable
                .from(motionEvents)
                .doOnNext(MotionEvent::recycle)
                .reduce(0, (accumulator, motionEvent) -> accumulator + motionEvent.getPointerCount())
                .subscribe(pointerCount -> {
                    System.out.println("" + motionEvents.size() + " - " + pointerCount);
                    if (touchCountIndicator != null) {
                        touchCountIndicator.setText("" + motionEvents.size() + " - " + pointerCount);
                        touchCountIndicator
                                .animate()
                                .alpha(0F)
                                .scaleXBy(15F)
                                .scaleYBy(15F)
                                .setDuration(1000L)
                                .setListener(new AnimatorListenerAdapter() {

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        touchCountIndicator.setText(null);
                                        touchCountIndicator.setAlpha(1F);
                                        touchCountIndicator.setScaleX(1F);
                                        touchCountIndicator.setScaleY(1F);
                                    }
                                });
                    } else System.out.println("NULL");
                });
    }
}
