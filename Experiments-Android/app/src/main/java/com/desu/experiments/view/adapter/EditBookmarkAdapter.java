package com.desu.experiments.view.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desu.experiments.R;
import com.desu.experiments.databinding.ItemBookmarkBinding;
import com.desu.experiments.model.GoogleApi.Item;
import com.desu.experiments.view.activity.material.bookmarks_edit.EditBookmarkActivity;
import com.desu.experiments.viewModel.item.ItemBookmarkViewModel;

import java.util.ArrayList;

public class EditBookmarkAdapter extends RecyclerView.Adapter<EditBookmarkAdapter.ViewHolder> {

    private static final int EXPANDED_POSITION_INVALID = -1;

    public void setItems(ArrayList<Item> itemsIn) {
        items = itemsIn;
        notifyDataSetChanged();
    }
    public void itemsWasInserted(ArrayList<Item> newItems, int positionStart, int itemCount) {
        items = newItems;
        notifyItemRangeInserted(positionStart, itemCount);
    }

    private ArrayList<Item> items;
    private int mExpandedPosition = -1;
    private Activity mActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        ItemBookmarkBinding binding;
        public ItemBookmarkBinding getBinding() {
            return binding;
        }
    }

    public EditBookmarkAdapter(Activity activity) {
        mActivity = activity;
        setItems(new ArrayList<>());
    }
    public EditBookmarkAdapter(ArrayList<Item> items, Activity activity) {
        setItems(items);
        mActivity = activity;
    }

    @Override
    public EditBookmarkAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemBookmarkBinding binding = ItemBookmarkBinding.inflate(inflater, viewGroup, false);

        final ViewHolder viewHolder = new ViewHolder(binding.getRoot());

        viewHolder.itemView.setOnClickListener(this::expandCard);
        viewHolder.itemView.setOnLongClickListener(view -> {
            expandCard(view);
            return true;
        });
        viewHolder.itemView.setTag(viewHolder);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //System.out.println("onBindViewHolder(mExpandedPosition) --------- " + position);
        if (items != null) {
            Item item = items.get(position);


            ItemBookmarkViewModel viewModel = new ItemBookmarkViewModel();

            viewModel.setTextViewTitle(item.title);
            viewModel.setTextViewDomain(item.title);

            viewModel.setTextViewFullUrl(item.displayLink);
            viewModel.setTextViewDateSaved(item.kind);

            if (position == mExpandedPosition) {
                holder.getBinding().linearlayoutDetail.setVisibility(View.VISIBLE);
                holder.getBinding().imageviewEdit.setOnClickListener(v -> startEditBookmarkActivity(holder.getBinding().cardView1));
                holder.getBinding().imageviewShare.setOnClickListener(v -> startShareIntent());
                holder.getBinding().imageviewDelete.setOnClickListener(v -> deleteBookmark());
            } else {
                holder.getBinding().linearlayoutDetail.setVisibility(View.GONE);
            }

            holder.getBinding().setItemBookmark(viewModel);
        }
    }
    @Override
    public int getItemCount() {
        return (items != null) ? items.size() : 0;
    }

    private void startShareIntent() {
    }
    private void startEditBookmarkActivity(View view) {

        Bundle bundle = new Bundle();
        //open explanded card details
        if (items != null) {
            Item item = items.get(mExpandedPosition);

            bundle.putSerializable(EditBookmarkActivity.BUNDLE_ITEM, item);

            Intent intent = new Intent(mActivity, EditBookmarkActivity.class);
            intent.putExtra(EditBookmarkActivity.INTENT_BUNDLE_ITEM, bundle);
            intent.putExtra(EditBookmarkActivity.INTENT_BOOKMARK, mExpandedPosition);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String transitionName = mActivity.getString(R.string.trans_cardview);
                ActivityOptions transitionActivityOptions =
                        ActivityOptions.makeSceneTransitionAnimation(mActivity, view, transitionName);
                mActivity.startActivity(intent, transitionActivityOptions.toBundle());
            } else {
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }
    }
    private void deleteBookmark() {
    }

    private void expandCard(View view) {

        try {
            ViewHolder holder = (ViewHolder) view.getTag();
            expandCard(holder.getLayoutPosition());
        } catch (Exception ignored) {
        }
    }
    private void expandCard(int position) {

        // Check for an expanded view, collapse if you find one
        if (mExpandedPosition != EXPANDED_POSITION_INVALID) {
            int prev = mExpandedPosition;
            notifyItemChanged(prev);
        }

        // If the position clicked is the same of the expanded view then collapse it
        if (mExpandedPosition == position) {
            mExpandedPosition = EXPANDED_POSITION_INVALID;
        } else {
            mExpandedPosition = position;
            notifyItemChanged(mExpandedPosition);
        }
    }
}