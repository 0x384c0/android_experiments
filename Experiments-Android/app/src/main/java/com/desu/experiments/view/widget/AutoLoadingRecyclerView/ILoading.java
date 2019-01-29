package com.desu.experiments.view.widget.AutoLoadingRecyclerView;

import java.util.List;

import rx.Observable;

/**
 * Created by kani on 10/30/15.
 */
public interface ILoading<T> {

    Observable<List<T>> getLoadingObservable(int page);

}