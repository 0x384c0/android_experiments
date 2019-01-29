package com.desu.experiments.view.widget.AutoLoadingRecyclerView;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class AutoLoadingData<T> implements Serializable, Comparable<T> {

    public long id;
    public String type;
    public T attributes;

    @Override
    public String toString() {
        return attributes.toString();
    }

    @Override
    public int compareTo(@NonNull T t) {
        return attributes.toString().compareTo(t.toString());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof AutoLoadingData)
            return type.equals(((AutoLoadingData) object).type) && id == ((AutoLoadingData) object).id;
        return super.equals(object);
    }
}
