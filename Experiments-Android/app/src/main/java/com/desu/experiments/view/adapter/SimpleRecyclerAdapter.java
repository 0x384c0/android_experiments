package com.desu.experiments.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.desu.experiments.R;

import java.util.ArrayList;
import java.util.List;


public class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.VersionViewHolder> {
    List<String> titleList;
    Boolean isHomeList = false;

    List<String> homeActivitiesList = new ArrayList<>();
    List<String> homeActivitiesSubList = new ArrayList<>();
    Context context;
    OnItemClickListener clickListener;


    public SimpleRecyclerAdapter(List<String> titleList, List<String> subTitleList, Context context) {
        isHomeList = true;
        this.context = context;

        homeActivitiesList = titleList;
        homeActivitiesSubList = subTitleList;
    }


    public SimpleRecyclerAdapter(List<String> titleList, Context context) {
        isHomeList = false;
        this.titleList = titleList;
        this.context = context;

    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VersionViewHolder versionViewHolder, int i) {
        if (isHomeList) {
            versionViewHolder.title.setText(homeActivitiesList.get(i));
            versionViewHolder.subTitle.setText(homeActivitiesSubList.get(i));
        } else {
            versionViewHolder.title.setText(titleList.get(i));
        }
    }

    @Override
    public int getItemCount() {
        if (isHomeList)
            return homeActivitiesList == null ? 0 : homeActivitiesList.size();
        else
            return titleList == null ? 0 : titleList.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardItemLayout;
        TextView title;
        TextView subTitle;

        public VersionViewHolder(View itemView) {
            super(itemView);

            cardItemLayout = (CardView) itemView.findViewById(R.id.cardlist_item);
            title = (TextView) itemView.findViewById(R.id.item_title);
            subTitle = (TextView) itemView.findViewById(R.id.item_subtitle);

            if (isHomeList) {
                itemView.setOnClickListener(this);
            } else {
                subTitle.setVisibility(View.GONE);
            }

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


}
