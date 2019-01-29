package com.desu.experiments.view;

import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.desu.experiments.R;
import com.desu.experiments.model.GoogleApi.Item;
import com.desu.experiments.util.ArrayListWithInsertLogger;
import com.desu.experiments.view.adapter.EditBookmarkAdapter;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;

public class CustomBindings {

    @BindingAdapter("bind:source")
    public static void setSource(RecyclerView recyclerView, ArrayListWithInsertLogger<Item> source) {
        EditBookmarkAdapter adapter = (EditBookmarkAdapter) recyclerView.getAdapter();
        if (source.isItemsWasInserted()){
            adapter.itemsWasInserted(source.getArrayList(), source.getPositionStart(), source.getItemCount());
        }
        else
            adapter.setItems(source.getArrayList());
        source.resetLog();
    }

    @BindingAdapter("bind:set_refreshing")
    public static void setRefreshing(SwipeRefreshLayout swipeRefreshLayout, boolean isRefreshing) {
        swipeRefreshLayout.setRefreshing(isRefreshing);
    }

    @BindingAdapter("bind:searchables")
    public static void setSearchables(SearchBox searchBox, ArrayList<String> searchables) {
        ArrayList<SearchResult> searchablesTmp = new ArrayList<>();
        int x = 0;
        for (String string : searchables) {
            SearchResult option = new SearchResult(string,
                    ContextCompat.getDrawable(searchBox.getContext(), R.drawable.ic_history_24dp));
            searchablesTmp.add(option);
            x++;
        }
        searchBox.setSearchables(searchablesTmp);
    }
}
