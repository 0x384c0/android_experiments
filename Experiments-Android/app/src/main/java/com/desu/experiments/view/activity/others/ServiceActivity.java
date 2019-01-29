package com.desu.experiments.view.activity.others;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.desu.experiments.Constants;
import com.desu.experiments.R;
import com.desu.experiments.service.TestService;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ServiceActivity extends AppCompatActivity {
    public final static String EVENT_NAME = "in-activity-message";

    @Bind(R.id.message_presentation_label)
    TextView messagePresentationLabel;
    @Bind(R.id.message_nbr_label)
    TextView messageNumberLabel;
    @Bind(R.id.message_presentation)
    TextView messagePresentationText;
    @Bind(R.id.message_nbr)
    TextView messageNumberText;
    @Bind(R.id.counter)
    TextView counterTextView;
    @Bind(R.id.increment_by)
    TextView incrementBy;
    @Bind(R.id.service_title)
    EditText serviceTitle;
    @Bind(R.id.service_text)
    EditText serviceText;


    Intent longRunningService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        ButterKnife.bind(this);
        messagePresentationLabel.setText(R.string.message_presentation_text);
        messageNumberLabel.setText(R.string.message_number_text);
        longRunningService = new Intent(ServiceActivity.this, TestService.class);
        registerListener(null);
        incrementBy.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                incrementBy(null);
                //return true; // don't close after OK pressed
            }
            return false;
        });
    }

    @Override
    protected void onDestroy() {
        unregisterListener(null);
        super.onDestroy();
    }


    public void startService(View v) {

        longRunningService.putExtra(TestService.TEXT, serviceText.getText().toString());
        longRunningService.putExtra(TestService.TITLE, serviceTitle.getText().toString());
        if (!isServiceRunning(TestService.class))
            startService(longRunningService);
        else Log.d("ServiceActivity", "TestService already Running");
    }

    public void killService(View v) {
        stopService(longRunningService);
    }

    public void stopService(View v) {
        Intent intent = new Intent(TestService.EVENT_NAME);
        intent.putExtra(TestService.STOP_EXTRA, true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void registerListener(View v) {
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter(EVENT_NAME));
    }

    public void unregisterListener(View v) {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }

    public void incrementBy(View v) {
        int incrementer = Integer.parseInt(incrementBy.getText().toString());
        Intent intent = new Intent(TestService.EVENT_NAME);
        intent.putExtra(TestService.INCREMENT_BY_EXTRA, incrementer);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void stopIncrementViaInstanse(View v) {
        if (TestService.getInstanse() != null)
            TestService.getInstanse().setIncrementBy(0);
        else Toast.makeText(this,"Service not started",Toast.LENGTH_LONG).show();
    }


    BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final int messageNbr = intent.getIntExtra("messageNbr", 0);
            final String message = intent.getStringExtra("message");

            switch (messageNbr) {
                case Constants.MESSAGE_ONE: {
                    messageNumberText.setText("Message 1");

                    break;
                }
                case Constants.MESSAGE_TWO: {
                    messageNumberText.setText("Message 2");

                    break;
                }
                case Constants.MESSAGE_THREE: {
                    messageNumberText.setText("Message 3");

                    break;
                }
                case Constants.MESSAGE_FOUR: {
                    messageNumberText.setText("Message 4");

                    break;
                }
                default: {

                }
            }

            messagePresentationText.setText(message);
            String s = Integer.toString(intent.getIntExtra(TestService.COUNTER_EXTRA, 0));
            counterTextView.setText(s);
        }
    };

    boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
