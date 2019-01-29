package com.desu.experiments.view.widget.AutoLoadingRecyclerView;

/**
 * Created by kani on 10/30/15.
 */
public class AutoLoadingRecyclerViewExceptions extends RuntimeException {

    public AutoLoadingRecyclerViewExceptions() {
        super("Exception in AutoLoadingRecyclerView");
    }

    public AutoLoadingRecyclerViewExceptions(String detailMessage) {
        super(detailMessage);
    }
}