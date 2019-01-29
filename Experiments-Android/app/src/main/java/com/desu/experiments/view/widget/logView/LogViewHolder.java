package com.desu.experiments.view.widget.logView;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.desu.experiments.databinding.ItemLogBinding;

public class LogViewHolder extends RecyclerView.ViewHolder {

    public ItemLogBinding getBinding() {
        return binding;
    }

    ItemLogBinding binding;

    public LogViewHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
}