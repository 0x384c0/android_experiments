package com.desu.experiments.viewModel.material.bookmarks_edit;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.desu.experiments.BR;
import com.desu.experiments.Constants;
import com.desu.experiments.model.GoogleApi.ImagesResponse;
import com.desu.experiments.model.GoogleApi.Item;
import com.desu.experiments.retrofit.DaggerGoogleApiDIComponent;
import com.desu.experiments.retrofit.DaggerLocalHostDIComponent;
import com.desu.experiments.retrofit.api.GoogleApi;
import com.desu.experiments.util.ArrayListWithInsertLogger;

import java.util.ArrayList;

import rx.Observer;
import rx.schedulers.Schedulers;

public class EditBookmarkListViewModel extends BaseObservable {

    //NETWORK
    public String currentQuery;
    //UI
    boolean refreshing = false;
    ArrayListWithInsertLogger<Item> items = new ArrayListWithInsertLogger<>(new ArrayList<>());
    ArrayList<String> searchables = new ArrayList<>();
    GoogleApi googleApi;

    public EditBookmarkListViewModel() {
        super();
        addSearchable(Constants.SEARCH_DEFAULT);
        //noinspection ConstantConditions
        googleApi = Constants.ECONOMY_GOOGLE_API ?
                DaggerLocalHostDIComponent.builder().build().maker().getGoogleApi() :
                DaggerGoogleApiDIComponent.create().maker().getGoogleApi();

    }
    @Bindable
    public ArrayList<String> getSearchables() {
        return searchables;
    }
    @SuppressWarnings("unused")
    public void setSearchables(ArrayList<String> searchables) {
        this.searchables = searchables;
        notifyPropertyChanged(BR.searchables);
    }
    public void addSearchable(String searchable) {
        this.searchables.add(searchable);
        notifyPropertyChanged(BR.searchables);
    }
    @Bindable
    public ArrayListWithInsertLogger<Item> getItems() {
        return items;
    }
    public void setItems(ArrayList<Item> items) {
        this.items.setArrayList(items);
        notifyPropertyChanged(BR.items);
    }
    public void addItems(ArrayList<Item> items) {
        this.items.addAllToArrayList(items);
        notifyPropertyChanged(BR.items);
    }
    @Bindable
    public boolean isRefreshing() {
        return refreshing;
    }
    public void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
        notifyPropertyChanged(BR.refreshing);
    }
    public void getImages(final String query, final int page) {
        currentQuery = query;
        if (page == Constants.FIRST_PAGE && !searchables.contains(query))
            addSearchable(currentQuery);
        googleApi.get(query, page)
                .subscribeOn(Schedulers.io())
                .subscribe(getObserver(page, query));

    }
    private Observer<ImagesResponse> getObserver(int page, String query) {
        return new Observer<ImagesResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                //noinspection PointlessBooleanExpression
                if (!Constants.ECONOMY_GOOGLE_API) {
                    System.out.println("-------------- ERROR ");
                    e.printStackTrace();
                }

                ArrayList<Item> itemsTmp = Constants.getDefaultBookmarkModel().items;
                for (Item item : itemsTmp) item.title += " page " + page + " " + query;
                System.out.println(itemsTmp);
                if (page == Constants.FIRST_PAGE)
                    setItems(itemsTmp);
                else
                    addItems(itemsTmp);
                setRefreshing(false);
            }

            @Override
            public void onNext(ImagesResponse Response) {
                System.out.println(Response.items.get(1).displayLink + "----------" + Thread.currentThread());

                if (page == Constants.FIRST_PAGE)
                    setItems(Response.items);
                else
                    addItems(Response.items);
                setRefreshing(false);
            }
        };
    }
}
