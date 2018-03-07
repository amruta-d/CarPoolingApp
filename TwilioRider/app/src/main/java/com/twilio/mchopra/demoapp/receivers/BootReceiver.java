package com.twilio.mchopra.demoapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.twilio.mchopra.demoapp.Storage.TwilioRiderSharedPreferences;

public class BootReceiver extends BroadcastReceiver {

    TwilioRiderSharedPreferences sharedPreferences;

    AlarmReceiver alarm = new AlarmReceiver();
    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = new TwilioRiderSharedPreferences(context);
        String appMode = sharedPreferences.getAppMode();
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            alarm.setAlarm(context, appMode);
        }
    }
}
