package com.desu.experiments.view.activity.material;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.LinearLayout;

import com.desu.experiments.R;

public class ConstraintsActivity extends AppCompatActivity {

    int
            view1Height,
            view1Width,
            view3Height,
            view2TopMargin,
            view4LeftMargin,

            densityDpi;
    View
            view1,
            view2,
            view3,
            view4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraints);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        view1 = findViewById(R.id.frameLayout1);
        view2 = findViewById(R.id.frameLayout2);
        view3 = findViewById(R.id.frameLayout3);
        view4 = findViewById(R.id.frameLayout4);

        densityDpi = getResources().getDisplayMetrics().densityDpi / 160;
        view1Height = view1.getLayoutParams().height;
        view1Width = view1.getLayoutParams().width;
        view2TopMargin = ((ConstraintLayout.LayoutParams) view2.getLayoutParams()).topMargin / densityDpi;
        view3Height = view3.getLayoutParams().height;
        view4LeftMargin = ((ConstraintLayout.LayoutParams) view4.getLayoutParams()).leftMargin / densityDpi;

        fab.setOnClickListener(view -> {
            ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
            animator.addUpdateListener(valueAnimator -> {
                Float val = (Float) valueAnimator.getAnimatedValue();
                view1.setAlpha(val);
                view1.getLayoutParams().height = (int) (val * view1Height);
                view1.getLayoutParams().width = (int) (val * view1Width);
                view1.requestLayout();

                view3.getLayoutParams().height = (int) (val * view3Height);
                view3.requestLayout();
            });
            animator.setDuration(1000);
            animator.start();

            ValueAnimator bounceAnimator = ValueAnimator.ofFloat(0, 1);
            bounceAnimator.addUpdateListener(valueAnimator -> {
                Float val = (Float) valueAnimator.getAnimatedValue();

                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view2.getLayoutParams();
                layoutParams.topMargin = (int) (val * view2TopMargin) * densityDpi;
                view2.setRotation(val * 360);
                view2.requestLayout();

                layoutParams = (ConstraintLayout.LayoutParams) view4.getLayoutParams();
                layoutParams.leftMargin = (int) (val * view4LeftMargin) * densityDpi;
                view4.requestLayout();
            });
            bounceAnimator.setDuration(3000);
            bounceAnimator.setInterpolator(new BounceInterpolator());
            bounceAnimator.start();
        });
    }
}
