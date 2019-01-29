package com.desu.experiments.view.activity.toolbars;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.desu.experiments.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ToolBarCollapsingIcon extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {

    static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    static final int ALPHA_ANIMATIONS_DURATION = 200;

    boolean mIsTheTitleVisible = false;
    boolean mIsTheTitleContainerVisible = true;

    @Bind(R.id.lol_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.main_textview_title)
    TextView mTitle;
    @Bind(R.id.main_linearlayout_title)
    LinearLayout mTitleContainer;
    @Bind(R.id.main_appbar)
    AppBarLayout mAppBarLayout;
    @Bind(R.id.main_imageview_placeholder)
    ImageView mImageparallax;
    @Bind(R.id.main_framelayout_title)
    FrameLayout mFrameParallax;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_collapsing_icon);

        ButterKnife.bind(this);



        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        mAppBarLayout.addOnOffsetChangedListener(this);
        initParallaxValues();
    }


    private void initParallaxValues() {

        CollapsingToolbarLayout.LayoutParams petDetailsLp =
                (CollapsingToolbarLayout.LayoutParams) mImageparallax.getLayoutParams();

        CollapsingToolbarLayout.LayoutParams petBackgroundLp =
                (CollapsingToolbarLayout.LayoutParams) mFrameParallax.getLayoutParams();

        petDetailsLp.setParallaxMultiplier(0.9f);
        petBackgroundLp.setParallaxMultiplier(0.3f);

        mImageparallax.setLayoutParams(petDetailsLp);
        mFrameParallax.setLayoutParams(petBackgroundLp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_collapsing_icon, menu);

        Drawable drawable = menu.findItem(R.id.menu_fav).getIcon();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.white));
        menu.findItem(R.id.menu_fav).setIcon(drawable);
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {

        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);

    }

    private void handleToolbarTitleVisibility(float percentage) {

        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {

        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {

            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {

        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
}
