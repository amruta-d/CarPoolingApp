package com.twilio.mchopra.demoapp.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import com.twilio.mchopra.demoapp.services.DrivePollingService;
import com.twilio.mchopra.demoapp.services.RidePollingService;

import java.util.Calendar;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private PendingIntent rideAlarmIntent;
    private PendingIntent driveAlarmIntent;
    private int rideReqCode = 0;
    private int driveReqCode = 1;
    private String alarmType;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("in Alarm on Receive", "alarm received");
        if (intent != null) {
            alarmType = intent.getStringExtra("alarmType");

        }
        if (alarmType != null) {
            Log.v("alarmType", alarmType);
            if (alarmType.equals("rideDetails")) {
                Log.v("in if", "if statement");
                Intent rideDetails = new Intent(context, RidePollingService.class);
                startWakefulService(context, rideDetails);
            } else if (alarmType.equals("driveDetails")) {
                Intent rideDetails = new Intent(context, DrivePollingService.class);
                startWakefulService(context, rideDetails);
            }
//            else if (alarmType.equals("webCat")) {
//                Intent webHistoryService = new Intent(context, WebHistoryCheckService.class);
//                startWakefulService(context, webHistoryService);
//            } else if (alarmType.equals("parentAlarm")) {
//                Intent webHistoryService = new Intent(context, ParentCurrentLocationCheckService.class);
//                startWakefulService(context, webHistoryService);
//
//            }

        }


    }

    public void setAlarm(Context context, String alarmMode) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmMode.equals("ride")) {
            Log.v("in Alarm mode", "ride");
            //----------------ride Alarm --------------------------------
            //setting the intent for ride alarm
            Intent rideDetailsIntent = new Intent(context, AlarmReceiver.class);
            rideDetailsIntent.putExtra("alarmType", "rideDetails");
            alarmIntent = PendingIntent.getBroadcast(context, rideReqCode, rideDetailsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            //setting the time and frequency for the alarm
            Calendar rideDetailsServiceCalendar = Calendar.getInstance();
            rideDetailsServiceCalendar.setTimeInMillis(System.currentTimeMillis());
            rideDetailsServiceCalendar.set(Calendar.HOUR_OF_DAY, 20);
            rideDetailsServiceCalendar.set(Calendar.MINUTE, 00);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, rideDetailsServiceCalendar.getTimeInMillis(),
                    1000, alarmIntent);

//            //----------------Current Location Alarm --------------------------------
//            //setting the intent for current location alarm
//            Intent currLoc_intent = new Intent(context, AlarmReceiver.class);
//            currLoc_intent.putExtra("alarmType", "currLoc");
//            currentLocAlarmIntent = PendingIntent.getBroadcast(context, currentLocReqCode, currLoc_intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            //setting the time and frequency for the alarm
//            Calendar currLocServiceCalendar = Calendar.getInstance();
//            currLocServiceCalendar.setTimeInMillis(System.currentTimeMillis());
//            currLocServiceCalendar.set(Calendar.HOUR_OF_DAY, 8);
//            currLocServiceCalendar.set(Calendar.MINUTE, 01);
//            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, currLocServiceCalendar.getTimeInMillis(),
//                    1 * 60 * 1000, currentLocAlarmIntent);
//
//            //----------------Web Category Alarm --------------------------------
//            //setting the intent for website category alarm
//            Intent webCat_intent = new Intent(context, AlarmReceiver.class);
//            webCat_intent.putExtra("alarmType", "webCat");
//            webCatAlarmIntent = PendingIntent.getBroadcast(context, websiteCategoryCode, webCat_intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            //setting the time and frequency for the alarm
//            Calendar webCatServiceCalendar = Calendar.getInstance();
//            webCatServiceCalendar.setTimeInMillis(System.currentTimeMillis());
//            webCatServiceCalendar.set(Calendar.HOUR_OF_DAY, 8);
//            webCatServiceCalendar.set(Calendar.MINUTE, 02);
//            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, webCatServiceCalendar.getTimeInMillis(),
//                    10 * 60 * 1000, webCatAlarmIntent);

        }

        else if (alarmMode.equals("drive")) {
            Intent driveDetailsIntent = new Intent(context, AlarmReceiver.class);
            driveDetailsIntent.putExtra("alarmType", "driveDetails");
            alarmIntent = PendingIntent.getBroadcast(context, driveReqCode, driveDetailsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            //setting the time and frequency for the alarm
            Calendar driveDetailsServiceCalendar = Calendar.getInstance();
            driveDetailsServiceCalendar.setTimeInMillis(System.currentTimeMillis());
            driveDetailsServiceCalendar.set(Calendar.HOUR_OF_DAY, 20);
            driveDetailsServiceCalendar.set(Calendar.MINUTE, 00);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, driveDetailsServiceCalendar.getTimeInMillis(),
                     1000, alarmIntent);
        }

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

}
