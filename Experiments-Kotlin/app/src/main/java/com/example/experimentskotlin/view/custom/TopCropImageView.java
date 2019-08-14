package com.example.experimentskotlin.view.custom;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by chris on 7/27/16.
 */
public class TopCropImageView extends AppCompatImageView {

    public TopCropImageView(Context context) {
        super(context);
        setScaleType(ScaleType.MATRIX);
    }

    public TopCropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.MATRIX);
    }

    public TopCropImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        recomputeImgMatrix();
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        recomputeImgMatrix();
        return super.setFrame(l, t, r, b);
    }

    private int lastViewWidth = 0;
    private int lastViewHeight = 0;
    private void recomputeImgMatrix() {
        if (getDrawable() == null) return;
        final int viewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        final int viewHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        if (lastViewWidth != viewWidth && viewWidth != 0 && lastViewHeight != viewHeight && viewHeight != 0) {

            float scale;
            final int drawableWidth = getDrawable().getIntrinsicWidth();
            final int drawableHeight = getDrawable().getIntrinsicHeight();

            if (drawableWidth * viewHeight > drawableHeight * viewWidth) {
                scale = (float) viewHeight / (float) drawableHeight;
            } else {
                scale = (float) viewWidth / (float) drawableWidth;
            }

            final Matrix matrix = getImageMatrix();
            matrix.setScale(scale, scale);
            setImageMatrix(matrix);

            lastViewWidth = viewWidth;
            lastViewHeight = viewHeight;
        }
    }

}