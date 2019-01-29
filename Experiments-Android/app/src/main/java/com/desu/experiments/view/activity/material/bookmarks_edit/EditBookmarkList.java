package com.desu.experiments.view.activity.material.bookmarks_edit;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;

import com.desu.experiments.Constants;
import com.desu.experiments.R;
import com.desu.experiments.databinding.ActivityEditBookmarkListBinding;
import com.desu.experiments.view.adapter.EditBookmarkAdapter;
import com.desu.experiments.view.widget.EndlessRecyclerOnScrollListener;
import com.desu.experiments.viewModel.material.bookmarks_edit.EditBookmarkListViewModel;
import com.konifar.fab_transformation.FabTransformation;
import com.quinny898.library.persistentsearch.SearchBox;

public class EditBookmarkList extends AppCompatActivity {

    ActivityEditBookmarkListBinding binding;
    EditBookmarkListViewModel viewModel;
    EndlessRecyclerOnScrollListener endlessScrollListener;
    boolean isTransforming;

    SearchBox search;
    FloatingActionButton fab;
    View viewTransformed;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_bookmark_list);
        binding.setEditBookmarkListViewModel(new EditBookmarkListViewModel());
        viewModel = binding.getEditBookmarkListViewModel();

        setSupportActionBar(binding.toolbarTransformed);
        actionBar = getSupportActionBar();
        setupTransformedFab();
        setupRecyclerView();
        setupSwipeRefreshLayout();
        setupFloatingSearchView();
        search(Constants.SEARCH_DEFAULT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        /*
        Drawable drawable = menu.findItem(R.id.menu_fav).getIcon();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.white));
        menu.findItem(R.id.menu_fav).setIcon(drawable);
        */
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        search.getOnActivityResultVoice(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    void setupTransformedFab() {
        fab = binding.fab;
        viewTransformed = binding.viewTransformed;

        fab.setOnClickListener(view ->
                FabTransformation.with(fab)
                        .duration(100)
                        .transformTo(viewTransformed));
        viewTransformed.setVisibility(View.INVISIBLE);
        viewTransformed.setOnClickListener(view ->
                FabTransformation.with(fab)
                        .transformFrom(viewTransformed));

    }
    void setupRecyclerView() {
        RecyclerView recyclerView = binding.recyclerviewPrivateBookmarks;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.getViewTreeObserver().addOnScrollChangedListener(() -> {
                    if (fab.getVisibility() != View.VISIBLE && !isTransforming) {
                        FabTransformation.with(fab)
                                .setListener(new FabTransformation.OnTransformListener() {
                                    @Override
                                    public void onStartTransform() {
                                        isTransforming = true;
                                    }

                                    @Override
                                    public void onEndTransform() {
                                        isTransforming = false;
                                    }
                                })
                                .transformFrom(viewTransformed);
                    }
                }
        );
        recyclerView.setAdapter(new EditBookmarkAdapter(this));
        endlessScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                viewModel.getImages(viewModel.currentQuery, current_page);
            }
        };
        recyclerView.addOnScrollListener(endlessScrollListener);

    }
    void setupSwipeRefreshLayout() {
        SwipeRefreshLayout refreshLayout = binding.swipeRefreshLayout;

        refreshLayout.setProgressViewOffset(false, 30, 200);
        refreshLayout.setColorSchemeResources(R.color.blue, R.color.green, R.color.yellow, R.color.red);
        refreshLayout.setOnRefreshListener(() -> {
            search(viewModel.currentQuery);
            viewModel.setRefreshing(true);
//            // говорим о том, что собираемся начать
//            Toast.makeText(this, R.string.refresh_started, Toast.LENGTH_SHORT).show();
//            // начинаем показывать прогресс
//            refreshLayout.setRefreshing(true);
//            // ждем 3 секунды и прячем прогресс
//            refreshLayout.postDelayed(() -> {
//                refreshLayout.setRefreshing(false);
//                // говорим о том, что собираемся закончить
//                Toast.makeText(EditBookmarkList.this, R.string.refresh_finished, Toast.LENGTH_SHORT).show();
//            }, 6000);
        });

    }
    void setupFloatingSearchView() {
        search = binding.searchbox;
        search
                .enableVoiceRecognition(this)
                .setSearchListener(this::search)
                .setDefaultText("Images search")
                .withMenuItemId(R.id.action_search)
                .withToolBar(binding.toolbarTransformed)
                .withOverlay(binding.overlay)
                .withBackButton();


    }

    void search(String query) {
        actionBar.setTitle(query);
        endlessScrollListener.reset();
        viewModel.getImages(query, Constants.FIRST_PAGE);
    }
}
