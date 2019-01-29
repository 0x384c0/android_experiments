package com.desu.experiments.view.activity.toolbars;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.desu.experiments.R;
import com.desu.experiments.model.AutoLoadingModel;
import com.desu.experiments.util.CustomLogger;
import com.desu.experiments.view.adapter.AutoLoadingAdapter;
import com.desu.experiments.view.widget.AutoLoadingRecyclerView.AutoLoadingData;
import com.desu.experiments.view.widget.AutoLoadingRecyclerView.AutoLoadingRecyclerView;
import com.desu.experiments.view.widget.AutoLoadingRecyclerView.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

public class ToolBarFadedLollipop extends AppCompatActivity {
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.anim_toolbar)
    Toolbar toolbar;
    @Bind(R.id.header)
    ImageView header;
    @Bind(R.id.recycler_view)
    AutoLoadingRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_faded_lolipop);
        ButterKnife.bind(this);
        setupHeader(R.drawable.city_ny);
        setupToolbar("Animate toolbar");
        setupRecyclerView();
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
    protected void onDestroy() {
        recyclerView.onDestroy();
        super.onDestroy();
    }

    void setupHeader(int drawable) {
        header.setImageDrawable(ContextCompat.getDrawable(this, drawable));
        try {
            Bitmap bitmap = header.getDrawingCache();
            //среднее из картинки
            //noinspection Convert2Lambda
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @SuppressWarnings("ResourceType")
                @Override
                public void onGenerated(Palette palette) {
                    int mutedColor = palette.getMutedColor(R.color.colorPrimary);
                    collapsingToolbar.setContentScrimColor(mutedColor);
                    collapsingToolbar.setStatusBarScrimColor(R.color.black_trans20);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void setupToolbar(String title) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        collapsingToolbar.setTitle(title);
    }
    void setupRecyclerView() {
        GridLayoutManager recyclerViewLayoutManager = new GridLayoutManager(this, 1);
        recyclerViewLayoutManager.supportsPredictiveItemAnimations();
        AutoLoadingAdapter adapter = new AutoLoadingAdapter();
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setPagination(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLoadingObservable(this::getObservable);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, (view, position) -> {
            System.out.println(CustomLogger.getLinePointer() + position);
            AutoLoadingData<AutoLoadingModel> data = (AutoLoadingData<AutoLoadingModel>) recyclerView.getAdapter().getItem(position);
            AutoLoadingModel autoLoadingModel = data.attributes;
            System.out.println(autoLoadingModel.meta);
        }));

        recyclerView.clearItems();
        recyclerView.startLoading();
    }

    Observable<List> getObservable(int page) {
        List<AutoLoadingData<AutoLoadingModel>> data = new ArrayList<>();
        int itemsOnPage = 20;
        for (int i = 0; i < itemsOnPage; i++) {
            AutoLoadingModel autoLoadingModel = new AutoLoadingModel();
            AutoLoadingData<AutoLoadingModel> autoLoadingData = new AutoLoadingData<>();
            autoLoadingData.id = i + page * itemsOnPage;
            autoLoadingData.type = "type";
            autoLoadingModel.title = "Id - " + i;
            autoLoadingModel.subTitle = "Page - " + page;
            autoLoadingModel.meta = "list ID - " + ((page-1) * itemsOnPage + i);
            autoLoadingData.attributes = autoLoadingModel;
            data.add(autoLoadingData);
        }
        return Observable.create(subscriber -> subscriber.onNext(data));
    }
}