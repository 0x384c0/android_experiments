package com.desu.experiments.model;

/**
 * Created by andrew on 26.09.16.
 */

public class TabActivityModel {

    public TabActivityModel(int tab, Class activityClass, String title, String subTitle) {
        this.tab = tab;
        this.activityClass = activityClass;
        this.title = title;
        this.subTitle = subTitle;
    }

    public int tab;
    public Class activityClass;
    public String
            title,
            subTitle;


}
