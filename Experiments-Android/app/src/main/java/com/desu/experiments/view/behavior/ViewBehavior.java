package com.desu.experiments.view.behavior;


import android.content.Context;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.SnackbarLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class ViewBehavior extends CoordinatorLayout.Behavior<View> {

    public ViewBehavior(Context context, AttributeSet attrs) {
    }

    private static final boolean SNACKBAR_BEHAVIOR_ENABLED = Build.VERSION.SDK_INT >= 11;
    private float mFabTranslationY;

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        System.out.println("parent      ------------------- " + parent);
        System.out.println("child       ------------------- " + child);
        System.out.println("dependency  ------------------- " + dependency);
        System.out.println("-------------------------------------------------------------- " + (dependency instanceof SnackbarLayout));

        return SNACKBAR_BEHAVIOR_ENABLED && dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
//        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
//        child.setTranslationY(translationY);
//        System.out.println("------------------------------- " + translationY + " ------------------------------- ");


        if (dependency instanceof Snackbar.SnackbarLayout) {
            updateFabTranslationForSnackbar(parent, child, dependency);
        }
        return false;
    }


    private void updateFabTranslationForSnackbar(CoordinatorLayout parent,
                                                 View fab, View snackbar) {
        if (fab.getVisibility() != View.VISIBLE) {
            return;
        }

        final float targetTransY = getFabTranslationYForSnackbar(parent, fab);
        if (mFabTranslationY == targetTransY) {
            // We're already at (or currently animating to) the target value, return...
            return;
        }

        mFabTranslationY = targetTransY;
        final float currentTransY = ViewCompat.getTranslationY(fab);
        final float dy = currentTransY - targetTransY;

        if (Math.abs(dy) > (fab.getHeight() * 0.667f)) {
            // If the FAB will be travelling by more than 2/3 of it's height, let's animate
            // it instead
            ViewCompat.animate(fab)
                    .translationY(targetTransY)
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setListener(null);
        } else {
            // Make sure that any current animation is cancelled
            ViewCompat.animate(fab).cancel();
            // Now update the translation Y
            ViewCompat.setTranslationY(fab, targetTransY);
        }
    }

    private float getFabTranslationYForSnackbar(CoordinatorLayout parent,
                                                View fab) {
        float minOffset = 0;
        final List<View> dependencies = parent.getDependencies(fab);
        for (int i = 0, z = dependencies.size(); i < z; i++) {
            final View view = dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
                minOffset = Math.min(minOffset,
                        ViewCompat.getTranslationY(view) - view.getHeight());
            }
        }

        return minOffset;
    }
}