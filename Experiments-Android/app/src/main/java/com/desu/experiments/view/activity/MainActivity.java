package com.desu.experiments.view.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.desu.experiments.Constants;
import com.desu.experiments.R;
import com.desu.experiments.model.TabModel;
import com.desu.experiments.view.adapter.MultipleLayoutAdapter;
import com.desu.experiments.view.adapter.ViewPagerAdapter;
import com.desu.experiments.view.adapter.items.ListViewCardItem;
import com.desu.experiments.view.adapter.items.ListViewHeaderItem;
import com.desu.experiments.view.adapter.items.ListViewItem;
import com.desu.experiments.view.adapter.items.ListViewTextIconItem;
import com.desu.experiments.view.adapter.items.ListViewTextItem;
import com.desu.experiments.view.fragment.ListFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @Bind(R.id.tabanim_toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabanim_viewpager)
    ViewPager viewPager;
    @Bind(R.id.tabanim_tabs)
    TabLayout tabLayout;

    @Bind(R.id.nav_view)
    NavigationView mNavigationView;

    @Bind(R.id.nav_drawer)
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                showToast(Constants.tabTitle[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setupDrawer();
        setupChromeMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chrome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showChromeMenu(findViewById(R.id.action_settings));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < Constants.tabTitle.length; i++) {

            TabModel tabModel = new TabModel();
            for (int j = 0; j < Constants.tabActivities.length; j++) {
                if (Constants.tabActivities[j].tab == i) {
                    tabModel.testActivityTitle.add(Constants.tabActivities[j].title);
                    tabModel.testActivitySubTitle.add(Constants.tabActivities[j].subTitle);
                    tabModel.activities.add(Constants.tabActivities[j].activityClass);
                }
            }

            Bundle bundle = new Bundle();
            bundle.putSerializable(ListFragment.TAB_MODEL, tabModel);//Serializable, the Master of Simplicity; Parcelable, the Speed King
            bundle.putInt(ListFragment.COLOR, ContextCompat.getColor(this, R.color.teal));
            bundle.putInt(ListFragment.POSITION, i);
            ListFragment listFragment = new ListFragment();
            listFragment.setArguments(bundle);


            adapter.addFrag(listFragment,
                    Constants.tabTitle[i]
            );

        }
        viewPager.setAdapter(adapter);
    }

    void setupDrawer() {
        Menu menu = mNavigationView.getMenu();
        for (int i = 0; i < Constants.tabTitle.length; i++) {
            menu.add(0, i, 0, Constants.tabTitle[i]);
        }
        mNavigationView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            TabLayout.Tab tab = tabLayout.getTabAt(menuItem.getItemId());
            if (tab != null) {
                tab.select();
            }
            drawerLayout.closeDrawers();
            return true;
        });


        if (toolbar != null) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_menu_24dp);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, Color.WHITE);

            toolbar.setNavigationIcon(drawable);
            toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        }
    }


    ListPopupWindow listPopupWindow;

    void setupChromeMenu() {

        // Create array list for items
        ArrayList<ListViewItem> listViewItems = new ArrayList<>();
        // Populate our list with items
        listViewItems.add(new ListViewTextItem("Simple text item 4"));
        listViewItems.add(new ListViewHeaderItem("Header one"));
        listViewItems.add(new ListViewTextItem("Simple text item 1"));
        listViewItems.add(new ListViewTextIconItem("Icon & text item 1", R.drawable.ic_add_24dp));
        listViewItems.add(new ListViewTextIconItem("Icon & text item 2", R.drawable.ic_favorite_outline_24dp));
        listViewItems.add(new ListViewCardItem("Title", "Sub title"));
        listViewItems.add(new ListViewHeaderItem("Header two"));
        listViewItems.add(new ListViewTextItem("Simple text item 3"));
        listViewItems.add(new ListViewTextIconItem("Icon & text item 3", R.drawable.ic_menu_24dp));
        // Create our custom adapter
        MultipleLayoutAdapter multipleLayoutAdapter = new MultipleLayoutAdapter(getApplicationContext(), listViewItems);


        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAdapter(multipleLayoutAdapter);
        listPopupWindow.setDropDownGravity(Gravity.END);
        listPopupWindow.setWidth(dpToPx(235)); // note: don't use pixels, use a dimen resource
    }
    void showChromeMenu(View anchorView) {
        listPopupWindow.setAnchorView(anchorView);
        listPopupWindow.show();
        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate_from_top), 0.3f);
        listPopupWindow.getListView().setLayoutAnimation(lac);
    }
    int dpToPx(int dp) {
        float density = getApplicationContext().getResources()
                .getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
