package com.desu.experiments.view.activity.material.sharedElements;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.desu.experiments.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ActivitySharedB extends AppCompatActivity {

    @Bind(R.id.button_in)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_b);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            ViewCompat.setTransitionName(button, "selectedButton");
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transition_a));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.anim.hold, R.anim.slide_out_bottom);
    }
}
