package com.desu.experiments.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.desu.experiments.R;
import com.desu.experiments.model.TabModel;
import com.desu.experiments.view.adapter.SimpleRecyclerAdapter;


public class ListFragment extends Fragment {
    public static final String
            COLOR = "color",
            POSITION = "position",
            TAB_MODEL = "tabModel";

    int color;
    int position;
    SimpleRecyclerAdapter adapter;
    Intent intent;
    TabModel tabModel;

    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        this.color = getArguments().getInt(COLOR);
        this.position = getArguments().getInt(POSITION);
        this.tabModel = (TabModel) getArguments().getSerializable(TAB_MODEL);


        System.out.println("onCreateView ListFragment -------------------");
        System.out.println("tabModel.testActivityTitle -------------------" + tabModel.testActivityTitle);
        View view = inflater.inflate(R.layout.dummy_fragment, container, false);

        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
        frameLayout.setBackgroundColor(color);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new SimpleRecyclerAdapter(tabModel.testActivityTitle, tabModel.testActivitySubTitle, getContext());
        recyclerView.setAdapter(adapter);

        adapter.SetOnItemClickListener((view1, position1) -> {
            try {
                intent = new Intent(getContext(), tabModel.activities.get(position1));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Undefined Click!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }
}