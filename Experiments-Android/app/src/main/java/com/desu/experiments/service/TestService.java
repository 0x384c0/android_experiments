package com.desu.experiments.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.desu.experiments.Constants;
import com.desu.experiments.R;
import com.desu.experiments.view.activity.others.ServiceActivity;


public class TestService extends Service {
    int
            NOTIFICATION_ID = 1,
            SLEEP_TIME = 5000;
    boolean stopThreadFlag = false;
    Thread thread;
    static TestService testService;

    int
            incrementBy = 1,
            counter = 0;
    public final static String
            TITLE = "SERVICE_TITLE",
            TEXT = "SERVICE_TEXT",
            INCREMENT_BY_EXTRA = "incrementBy",
            COUNTER_EXTRA = "counter",
            STOP_EXTRA = "stopExtra",
            EVENT_NAME = "in-service-message";

    @Override
    public void onCreate() {
        super.onCreate();
        testService = this;
        Log.e("TestService", "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.e("TestService", "onDestroy");
        testService = null;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        thread.interrupt();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("TestService", "onBind");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String
                text = intent.getStringExtra(TEXT),
                title = intent.getStringExtra(TITLE);

        Log.e("TestService", "onStartCommand");
        // срабатывает, когда сервис запущен методом startService(Intent intent)
        // (intent можно использовать для передачи данных из Activity)
        //startId - счетчик вызовов startService пока сервис запущен (сбрасывается после остановки сервиса методами stopService, stopSelf и пр.)

        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter(EVENT_NAME));
        //important in manifest - android:launchMode="singleTask"
        Notification notification;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, ServiceActivity.class), 0);//if (sdkVer < Build.VERSION_CODES.JELLY_BEAN) phone will reboot without contentIntent
        notification = builder.setContentIntent(contentIntent)
                .setContentTitle(title)
                .setContentText(text)//getApplication().getResources().getString(R.string.service_started))
                .setSmallIcon(getNotificationIcon())
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .build();
        startForeground(NOTIFICATION_ID, notification);

        thread = new Thread() {
            @Override
            public void run() {
                LongTask();
            }
        };
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }
    private int getNotificationIcon() {
        return R.drawable.ic_favorite_outline_24dp;
    }
    public static TestService getInstanse() {
        return testService;
    }

    public void setIncrementBy(int incrementBy) {
        this.incrementBy = incrementBy;
    }

    void LongTask() {
        try {
            int[] messagesID = {
                    Constants.MESSAGE_ONE,
                    Constants.MESSAGE_TWO,
                    Constants.MESSAGE_THREE,
                    Constants.MESSAGE_FOUR
            };
            int[] messagesRes = {
                    R.string.message1,
                    R.string.message2,
                    R.string.message3,
                    R.string.message4
            };



            while (!stopThreadFlag) {
                for (int i = 0; i < 4; i++) {
                    if (stopThreadFlag) break;
                    counter += incrementBy;
                    broadcastMessage(messagesID[i], getApplicationContext().getString(messagesRes[i]), counter);
                    Thread.sleep(SLEEP_TIME);
                }
            }
            stopSelf();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void broadcastMessage(int messageIndicator, String message, int counter) {
        Log.e("Service----", "broadcastMessage");
        Intent intent = new Intent(ServiceActivity.EVENT_NAME);
        intent.putExtra("messageNbr", messageIndicator);
        intent.putExtra("message", message);
        intent.putExtra(COUNTER_EXTRA, counter);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("TestService", "onReceive");
            stopThreadFlag = intent.getBooleanExtra(STOP_EXTRA, false);
            if (!stopThreadFlag)
                incrementBy = intent.getIntExtra(INCREMENT_BY_EXTRA, 1);
        }
    };
}