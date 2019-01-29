package com.desu.experiments.view.widget.AutoLoadingRecyclerView;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kani on 10/30/15.
 */
public abstract class AutoLoadingRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> listElements = new ArrayList<>();

    public void addNewItems(List<T> items) {
        listElements.addAll(items);
    }

    public void addNewItem(T item) {
        listElements.add(item);
    }

    public List<T> getItems() {
        return listElements;
    }

    public T getItem(int position) {
        return listElements.get(position);
    }

    @Override
    public int getItemCount() {
        return listElements.size();
    }
}