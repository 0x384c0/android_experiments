package com.desu.experiments.view.activity.toolbars;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.desu.experiments.R;
import com.manuelpeinado.fadingactionbar.view.ObservableScrollable;
import com.manuelpeinado.fadingactionbar.view.OnScrollChangedCallback;
import com.readystatesoftware.systembartint.SystemBarTintManager;


public class ToolBarFaded extends AppCompatActivity implements OnScrollChangedCallback {

    private Toolbar mToolbar;
    private Drawable mActionBarBackgroundDrawable;
    private View mHeader;
    private int mLastDampedScroll;
    private int mInitialStatusBarColor;
    private int mFinalStatusBarColor;
    private SystemBarTintManager mStatusBarManager;
    public ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_faded);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mActionBarBackgroundDrawable = mToolbar.getBackground();
        mToolbar.setTitle("App");
        setSupportActionBar(mToolbar);

        mStatusBarManager = new SystemBarTintManager(this);
        mStatusBarManager.setStatusBarTintEnabled(true);

        mInitialStatusBarColor = ContextCompat.getColor(this, R.color.background_material_dark);
        mFinalStatusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark);

        mHeader = findViewById(R.id.header);

        ObservableScrollable scrollView = (ObservableScrollable) findViewById(R.id.scrollview);
        scrollView.setOnScrollChangedCallback(this);


        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        onScroll(-1, 0);
    }


    public void onScroll(int l, int scrollPosition) {
        int headerHeight = mHeader.getHeight() - mToolbar.getHeight();
        float ratio = 0;
        if (scrollPosition > 0 && headerHeight > 0)
            ratio = (float) Math.min(Math.max(scrollPosition, 0), headerHeight) / headerHeight;

        updateActionBarTransparency(ratio);
        updateStatusBarColor(ratio);
        updateParallaxEffect(scrollPosition);
    }

    private void updateActionBarTransparency(float scrollRatio) {
        int newAlpha = (int) (scrollRatio * 255);
        mActionBarBackgroundDrawable.setAlpha(newAlpha);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            mToolbar.setBackground(mActionBarBackgroundDrawable);
        else //noinspection deprecation
            mToolbar.setBackgroundDrawable(mActionBarBackgroundDrawable);

    }

    private void updateStatusBarColor(float scrollRatio) {
        int r = interpolate(Color.red(mInitialStatusBarColor), Color.red(mFinalStatusBarColor), 1 - scrollRatio);
        int g = interpolate(Color.green(mInitialStatusBarColor), Color.green(mFinalStatusBarColor), 1 - scrollRatio);
        int b = interpolate(Color.blue(mInitialStatusBarColor), Color.blue(mFinalStatusBarColor), 1 - scrollRatio);
        mStatusBarManager.setTintColor(Color.rgb(r, g, b));
    }

    private void updateParallaxEffect(int scrollPosition) {
        float damping = 0.5f;
        int dampedScroll = (int) (scrollPosition * damping);
        int offset = mLastDampedScroll - dampedScroll;
        mHeader.offsetTopAndBottom(-offset);

        mLastDampedScroll = dampedScroll;
    }

    private int interpolate(int from, int to, float param) {
        return (int) (from * param + to * (1 - param));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "No Settings", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateActionBarTransparency(1);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            // onScroll(0, lastScrollPosition);
            onScroll(-1, 0);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            // onScroll(0, lastScrollPosition);
            onScroll(-1, 0);
        }
    }
}
