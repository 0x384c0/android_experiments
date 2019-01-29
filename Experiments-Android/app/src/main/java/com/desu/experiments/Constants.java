package com.desu.experiments;

import com.desu.experiments.model.GoogleApi.ImagesResponse;
import com.desu.experiments.model.GoogleApi.Item;
import com.desu.experiments.model.TabActivityModel;
import com.desu.experiments.view.activity.material.ConstraintsActivity;
import com.desu.experiments.view.activity.material.CoordinatorLay;
import com.desu.experiments.view.activity.material.bookmarks_edit.EditBookmarkList;
import com.desu.experiments.view.activity.material.content_transition.ActivityA;
import com.desu.experiments.view.activity.material.sharedElements.ActivitySharedA;
import com.desu.experiments.view.activity.others.LambdaExpressions;
import com.desu.experiments.view.activity.others.ServiceActivity;
import com.desu.experiments.view.activity.rx.RxAsyncTask;
import com.desu.experiments.view.activity.rx.RxClickEvents;
import com.desu.experiments.view.activity.rx.RxFormValidation;
import com.desu.experiments.view.activity.rx.RxInputTextDebounce;
import com.desu.experiments.view.activity.rx.RxOnTouchIndicator;
import com.desu.experiments.view.activity.rx.RxRetrofit;
import com.desu.experiments.view.activity.rx.RxSchedulers;
import com.desu.experiments.view.activity.toolbars.ToolBarCollapsingIcon;
import com.desu.experiments.view.activity.toolbars.ToolBarFaded;
import com.desu.experiments.view.activity.toolbars.ToolBarFadedLollipop;

public class Constants {

    public static final String
            GOOGLE_API_CX = "013770233867257397766:e7fh4k86up8",
            GOOGLE_API_KEY = "AIzaSyD-qVniTDTaTWgqkBpbCb38LsLR-Rxj9e4",
            GOOGLE_API_IMAGES_URL = "https://www.googleapis.com/customsearch/",
            SEARCH_DEFAULT = "Google";
    public static final boolean ECONOMY_GOOGLE_API = false;
    public static final int
            FIRST_PAGE = 1,

            MESSAGE_ONE = 1,
            MESSAGE_TWO = 2,
            MESSAGE_THREE = 3,
            MESSAGE_FOUR = 4,

            TOOLBARS = 0,
            MATERIAL = 1,
            RX = 2,
            OTHERS = 3;

    public static ImagesResponse getDefaultBookmarkModel() {
        ImagesResponse editBookmarkModel = new ImagesResponse();
        if (editBookmarkModel.items.size() == 0)
            for (int i = 0; i < 30; i++) {
                Item item = new Item();
                item.title = "TITLE " + i;
                item.displayLink = "displayLink " + i;
                item.kind = "kind " + i;
                editBookmarkModel.items.add(item);
            }
        return editBookmarkModel;
    }

    public static final String[] data = {
            "Поле 1",
            "Поле 2",
            "Поле 3",
            "Поле 4",
            "Поле 5",
            "Поле 6",
            "Поле 7"
    };
    public static final String[] tabTitle = {
            "Toolbars",
            "Material",
            "RX",
            "Others"
    };

    public static final TabActivityModel[] tabActivities = {
            new TabActivityModel(TOOLBARS,  ToolBarFaded.class,             "Faded bar",            "Faded bar"),
            new TabActivityModel(TOOLBARS,  ToolBarFadedLollipop.class,     "Faded bar lollipop",   "Faded bar lollipop"),
            new TabActivityModel(TOOLBARS,  ToolBarCollapsingIcon.class,    "Collapsing bar",       "Collapsing bar"),
            new TabActivityModel(MATERIAL,  ConstraintsActivity.class,      "Constraints",          "Constraints, animations"),
            new TabActivityModel(MATERIAL,  CoordinatorLay.class,           "Coordinator lay",      "Coordinator lay, menu-to arrow toolbar, icon tint, range bar"),
            new TabActivityModel(MATERIAL,  ActivitySharedA.class,          "Shared Elements",      "Shared Elements"),
            new TabActivityModel(MATERIAL,  ActivityA.class,                "Content transition",   "Content transition"),
            new TabActivityModel(MATERIAL,  EditBookmarkList.class,         "Bookmarks Edit",       "Explanded list, content transition, SwipeRefreshLayout, close on overScroll"),
            new TabActivityModel(RX,        RxFormValidation.class,         "Form Validation",      "Form Validation, Double binding, pasSay, regex"),
            new TabActivityModel(RX,        RxInputTextDebounce.class,      "Input text debounce",  "Input text debounce"),
            new TabActivityModel(RX,        RxOnTouchIndicator.class,       "RxOnTouchIndicator",   "Accumulate calls"),
            new TabActivityModel(RX,        RxClickEvents.class,            "On click events",      "On click events, buffer, flat map"),
            new TabActivityModel(RX,        RxRetrofit.class,               "Retrofit",             "Retrofit"),
            new TabActivityModel(RX,        RxAsyncTask.class,              "RX asyncTask",         "AsyncTask on RX"),
            new TabActivityModel(RX,        RxSchedulers.class,             "Schedulers",           "Schedulers"),
            new TabActivityModel(OTHERS,    LambdaExpressions.class,        "Lambda Expressions",   "Lambda exp, Stream api, patterns: loan"),
            new TabActivityModel(OTHERS,    ServiceActivity.class,          "Service",              "Foreground Service")
    };

}
