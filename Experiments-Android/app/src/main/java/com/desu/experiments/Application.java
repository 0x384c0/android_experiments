package com.desu.experiments;

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.facebook.drawee.backends.pipeline.Fresco;

public class Application extends android.app.Application {
    static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        Fresco.initialize(this);
        context = this;
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }


    public static Context getContext() {
        return context;
    }
}
