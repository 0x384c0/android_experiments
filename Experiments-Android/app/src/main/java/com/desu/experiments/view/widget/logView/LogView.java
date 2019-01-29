package com.desu.experiments.view.widget.logView;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

public class LogView extends RecyclerView {


    RecyclerLogAdapter recyclerLogAdapter;
    List<String> logList;

    public LogView(Context context, AttributeSet attrs) {
        super(context, attrs);

        logList = new ArrayList<>();
        recyclerLogAdapter = new RecyclerLogAdapter(new ArrayList<>());
        setAdapter(recyclerLogAdapter);
        setLayoutManager(new LinearLayoutManager(context));
        setItemAnimator(null);
    }

    public void logWithThreadName(String logMsg) {
        addLogToRecyclerView(logMsg + " --" + Thread.currentThread() + "--");
    }

    public void log(String logMsg) {
        addLogToRecyclerView(logMsg);
    }

    public void log(ArrayList<String> logMsg) {
        new Handler(Looper.getMainLooper()).post(() -> {
            recyclerLogAdapter.rewriteStrings(logMsg);
            scrollToPosition(0);
        });
    }

    void addLogToRecyclerView(String logMsg) {
        System.out.println(logMsg);
        new Handler(Looper.getMainLooper()).post(() -> {
            logList.add(0, logMsg);
            recyclerLogAdapter.setStrings(logList);
            scrollToPosition(0);
        });
    }
}
