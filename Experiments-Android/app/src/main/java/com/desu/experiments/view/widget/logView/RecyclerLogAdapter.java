package com.desu.experiments.view.widget.logView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.desu.experiments.databinding.ItemLogBinding;

import java.util.List;


public class RecyclerLogAdapter extends RecyclerView.Adapter<LogViewHolder> {
    public void setStrings(List<String> strings) {
        this.strings = strings;
        notifyItemInserted(0);
    }

    public void rewriteStrings(List<String> strings) {
        this.strings = strings;
        notifyDataSetChanged();
    }

    List<String> strings;


    public RecyclerLogAdapter(List<String> dataArgs) {
        strings = dataArgs;
    }

    @Override
    public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemLogBinding binding = ItemLogBinding.inflate(inflater, parent, false);
        return new LogViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(LogViewHolder holder, int position) {
        ItemLogUser itemLogUser = new ItemLogUser();
        itemLogUser.setLogString(strings.get(position));
        holder.getBinding().setItemLogUser(itemLogUser);
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

}
