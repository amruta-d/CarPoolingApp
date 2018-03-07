package com.twilio.mchopra.demoapp.services;


import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.twilio.mchopra.demoapp.Activity.MainActivity;
import com.twilio.mchopra.demoapp.Storage.TwilioRiderSharedPreferences;

import java.util.Calendar;
import com.twilio.mchopra.demoapp.R;

public class RidePollingService extends IntentService {

    TwilioRiderSharedPreferences sharedPreferences;


    public RidePollingService() {
        super("RidePollingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v("inIntent", "handling intent");

        Context context = getApplicationContext();
        sharedPreferences = new TwilioRiderSharedPreferences(context);

        checkTime();

    }

    private void checkTime() {
        Long currentTime = Calendar.getInstance().getTimeInMillis();
        Log.v("inTime", "checking time");
        sendNotification(currentTime);
    }

    private void sendNotification(Long currentTime){

        Log.v("inNotification", "sending notification");

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification n  = new Notification.Builder(this)
                .setContentTitle("My App Notification test")
                .setContentText("Subject")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .addAction(R.mipmap.ic_launcher, "Call", pIntent)
                .addAction(R.mipmap.ic_launcher, "More", pIntent)
                .addAction(R.mipmap.ic_launcher, "And more", pIntent).build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);

    }
}
