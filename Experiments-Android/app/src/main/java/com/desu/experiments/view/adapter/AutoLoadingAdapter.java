package com.desu.experiments.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desu.experiments.databinding.ItemCardBinding;
import com.desu.experiments.model.AutoLoadingModel;
import com.desu.experiments.viewModel.item.ItemCardViewModel;
import com.desu.experiments.view.widget.AutoLoadingRecyclerView.AutoLoadingData;
import com.desu.experiments.view.widget.AutoLoadingRecyclerView.AutoLoadingRecyclerViewAdapter;

public class AutoLoadingAdapter extends AutoLoadingRecyclerViewAdapter<AutoLoadingData<AutoLoadingModel>> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCardBinding binding = ItemCardBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        AutoLoadingModel model = getItem(position).attributes;
        ItemCardViewModel itemCardViewModel = new ItemCardViewModel();
        itemCardViewModel.setTitle(model.title);
        itemCardViewModel.setSubtitle(model.subTitle);
        holder.getBinding().setItemCardViewModel(itemCardViewModel);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemCardBinding getBinding() {
            return binding;
        }

        ItemCardBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
