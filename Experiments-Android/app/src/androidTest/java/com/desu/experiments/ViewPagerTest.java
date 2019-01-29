package com.desu.experiments;

import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;

import com.desu.experiments.view.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

public class ViewPagerTest extends ActivityInstrumentationTestCase2<MainActivity> {

    String TITLE = "Form Validation";

    public ViewPagerTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testAllTabDisplayedOnSwipe() {//need to disable all animations
        SystemClock.sleep(500);
        onView(withId(R.id.tabanim_viewpager)).perform(swipeLeft());
        SystemClock.sleep(1000);
        onView(withId(R.id.tabanim_viewpager)).perform(swipeLeft());
        SystemClock.sleep(1000);
        onView(allOf(withId(R.id.item_title), withText(TITLE))).perform(click());
        onView(withId(R.id.password)).perform(click());
        onView(withId(R.id.password)).perform(typeText("AAaa111!!a"));
        SystemClock.sleep(1000);
    }
}
