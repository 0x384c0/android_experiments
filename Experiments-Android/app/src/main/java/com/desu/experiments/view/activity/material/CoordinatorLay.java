package com.desu.experiments.view.activity.material;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;
import com.desu.experiments.R;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CoordinatorLay extends AppCompatActivity {

    Snackbar snackbar;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;
    @Bind(R.id.toolbar_with_arrow)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout_with_arrow)
    DrawerLayout drawerLayout;
    @Bind(R.id.range_bar)
    RangeBar rangeBar;
    @Bind(R.id.left)
    TextView leftTextView;
    @Bind(R.id.right)
    TextView rightTextView;
    ActionBarDrawerToggle toggle;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.app_name,
                R.string.app_name);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);

        snackbar = Snackbar.make(findViewById(R.id.coordinator_root), "", Snackbar.LENGTH_LONG);
        floatingActionButton.setOnClickListener(view -> {
            if (!snackbar.isShown()) {
                snackbar = Snackbar.make(findViewById(R.id.coordinator_root), "Snackbar", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else snackbar.dismiss();
        });
        setupRangeBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_collapsing_icon, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    public void onClick(View view) {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 400.0f, 0.0f, 0.0f);
        animation.setDuration(1000);  // animation duration
        animation.setRepeatCount(1);  // animation repeat count
        animation.setRepeatMode(2);   // repeat animation (left to right, right to left )
        TextView view1 = (TextView) findViewById(R.id.CoordinatorLayoutTextView);
        view1.startAnimation(animation);

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        Drawable drawable = menu.findItem(R.id.menu_fav).getIcon();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        menu.findItem(R.id.menu_fav).setIcon(drawable);
    }


    void setupRangeBar() {

        String[] YEARS = new String[]{
                "1970г.",
                "1980г.",
                "1990г.",
                "2000г.",
                "2001г.",
                "2002г.",
                "2003г.",
                "2004г.",
                "2005г.",
                "2006г.",
                "2007г.",
                "2008г.",
                "2009г.",
                "2010г.",
                "2011г.",
                "2012г.",
                "2013г.",
                "2014г.",
                "2015г."
        };
        rangeBar.setTickEnd(YEARS.length - 1);


        rangeBar.setOnRangeBarChangeListener((rangeBar1, leftPinIndex, rightPinIndex, leftPinValue, rightPinValue) -> {
            leftTextView.setText("От " + YEARS[leftPinIndex]);
            rightTextView.setText("До " + YEARS[rightPinIndex]);
        });

        rangeBar.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d("TouchTest", "Touch down");
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d("TouchTest", "Touch up");
            }
            return false;
        });
                rangeBar.setFormatter(s -> YEARS[Integer.parseInt(s)]);

    }
}
