package com.desu.experiments.view.activity.rx;

import android.view.View;

public class RxClickEventsUser {
    String text;
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    View.OnClickListener onClickListener;
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

}
