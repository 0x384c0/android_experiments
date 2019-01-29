package com.desu.experiments.view.widget.AutoLoadingRecyclerView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class AutoLoadingRecyclerView<T> extends RecyclerView {

    private static final String TAG = "AutoLoadingRecyclerView";
    private static final int LIMIT = 25;

    private PublishSubject<Integer> scrollLoadingChannel = PublishSubject.create();
    private PublishSubject<List<T>> loadingItems = PublishSubject.create();
    private Subscription loadNewItemsSubscription;
    private Subscription subscribeToLoadingChannelSubscription;
    private int page;
    private ILoading<T> iLoading;
    private AutoLoadingRecyclerViewAdapter<T> autoLoadingRecyclerViewAdapter;
    private boolean withPagination;
    private boolean flagFirstRequest;

    public AutoLoadingRecyclerView(Context context) {
        super(context);
        init();
    }

    public AutoLoadingRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoLoadingRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setPagination(boolean pagination) {
        this.withPagination = pagination;
    }

    /**
     * required method
     * call after init all parameters in AutoLoadedRecyclerView
     */
    public void startLoading() {
        page = 1;
        flagFirstRequest = false;
        loadNewItems(page);
    }

    private void init() {
        withPagination = true;
        flagFirstRequest = false;
        startScrollingChannel();
    }

    private void startScrollingChannel() {
        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int position = getLastVisibleItemPosition();
                int updatePosition = getAdapter().getItemCount() - 1;
                if (position >= updatePosition)
                    scrollLoadingChannel.onNext(page);
            }
        });
    }

    private int getLastVisibleItemPosition() {
        Class recyclerViewLMClass = getLayoutManager().getClass();
        if (recyclerViewLMClass == LinearLayoutManager.class || LinearLayoutManager.class.isAssignableFrom(recyclerViewLMClass)) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
            return linearLayoutManager.findLastVisibleItemPosition();
        } else if (recyclerViewLMClass == StaggeredGridLayoutManager.class || StaggeredGridLayoutManager.class.isAssignableFrom(recyclerViewLMClass)) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] into = staggeredGridLayoutManager.findLastVisibleItemPositions(null);
            List<Integer> intoList = new ArrayList<>();
            for (int i : into) {
                intoList.add(i);
            }
            return Collections.max(intoList);
        }
        throw new AutoLoadingRecyclerViewExceptions("Unknown LayoutManager class: " + recyclerViewLMClass.toString());
    }

    public int getPage() {
        if (page <= 0) {
            throw new AutoLoadingRecyclerViewExceptions("page must be initialised! And page must be more than zero!");
        }
        return page;
    }

    /**
     * required method
     */
    public void setPage(int page) {
        this.page = page;
    }

    @Deprecated
    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof AutoLoadingRecyclerViewAdapter) {
            super.setAdapter(adapter);
        } else {
            throw new AutoLoadingRecyclerViewExceptions("Adapter must be implement IAutoLoadedAdapter");
        }
    }


    /**
     * required method
     */
    public void setAdapter(AutoLoadingRecyclerViewAdapter<T> autoLoadingRecyclerViewAdapter) {
        if (autoLoadingRecyclerViewAdapter == null) {
            throw new AutoLoadingRecyclerViewExceptions("Null adapter. Please initialise adapter!");
        }
        this.autoLoadingRecyclerViewAdapter = autoLoadingRecyclerViewAdapter;
        super.setAdapter(autoLoadingRecyclerViewAdapter);
    }

    public AutoLoadingRecyclerViewAdapter<T> getAdapter() {
        if (autoLoadingRecyclerViewAdapter == null) {
            throw new AutoLoadingRecyclerViewExceptions("Null adapter. Please initialise adapter!");
        }
        return autoLoadingRecyclerViewAdapter;
    }

    public void setLoadingObservable(ILoading<T> iLoading) {
        this.iLoading = iLoading;
    }

    public ILoading<T> getLoadingObservable() {
        if (iLoading == null) {
            throw new AutoLoadingRecyclerViewExceptions("Null LoadingObservable. Please initialise LoadingObservable!");
        }
        return iLoading;
    }

    private void subscribeToLoadingChannel() {
        Subscriber<Integer> toLoadingChannelSubscriber = new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "subscribeToLoadingChannel error", e);
            }

            @Override
            public void onNext(Integer page) {
                unsubscribe();
                loadNewItems(page);
            }
        };
        subscribeToLoadingChannelSubscription = scrollLoadingChannel
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(toLoadingChannelSubscriber);
    }

    private void loadNewItems(final int page) {
        Subscriber<List<T>> loadNewItemsSubscriber = new Subscriber<List<T>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "loadNewItems error", e);
                subscribeToLoadingChannel();
                loadingItems.onError(e);
            }

            @Override
            public void onNext(List<T> ts) {
                AutoLoadingRecyclerViewAdapter adapter = getAdapter();
                if (ts.size() > 0) {
                    for (T item : ts) {
                        int pos = adapter.getItems().indexOf(item);
                        if (pos >= 0) {
                            adapter.getItems().remove(pos);
                            adapter.getItems().add(pos, item);
                            adapter.notifyItemChanged(pos);
                        } else {
                            adapter.getItems().add(item);
                            adapter.notifyItemInserted(adapter.getItemCount() - 1);
                        }
                    }
                    AutoLoadingRecyclerView.this.page++;
                    subscribeToLoadingChannel();
                } else if (subscribeToLoadingChannelSubscription != null)
                    subscribeToLoadingChannelSubscription.unsubscribe();
                loadingItems.onNext(adapter.getItems());
                if (!withPagination) flagFirstRequest = true;
            }
        };
        if (withPagination || (!withPagination && !flagFirstRequest))
            loadNewItemsSubscription = getLoadingObservable().getLoadingObservable(page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(loadNewItemsSubscriber);
    }

    /**
     * required method
     * call in OnDestroy(or in OnDestroyView) method of Activity or Fragment
     */
    public void onDestroy() {
        scrollLoadingChannel.onCompleted();
        loadingItems.onCompleted();
        if (subscribeToLoadingChannelSubscription != null && !subscribeToLoadingChannelSubscription.isUnsubscribed()) {
            subscribeToLoadingChannelSubscription.unsubscribe();
        }
        if (loadNewItemsSubscription != null && !loadNewItemsSubscription.isUnsubscribed()) {
            loadNewItemsSubscription.unsubscribe();
        }
    }

    public void clearItems() {
        getAdapter().getItems().clear();
        getAdapter().notifyDataSetChanged();
    }

    public Observable<List<T>> getLoadingItems() {
        return loadingItems.asObservable();
    }

}