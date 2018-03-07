package com.twilio.mchopra.demoapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.twilio.mchopra.demoapp.R;
import com.twilio.mchopra.demoapp.Storage.TwilioRiderSharedPreferences;

import java.util.Objects;


public class SplashActivity extends AppCompatActivity {
    private TwilioRiderSharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = new TwilioRiderSharedPreferences(this);
        final boolean isUserLoggedIn = authenticate();
        final boolean isAppModeSet = isAppModeSet();
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (isUserLoggedIn) {
                        if (isAppModeSet) {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(SplashActivity.this, ChooseModeActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);

                    }
                }
            }
        };
        timerThread.start();


    }

    private boolean authenticate() {
        return sharedPreferences.getUserLoggedIn();
    }

    private boolean isAppModeSet() {

        String mode = sharedPreferences.getAppMode();
        if (Objects.equals(mode, ""))
            return false;
        else if (Objects.equals(mode, "ride") || Objects.equals(mode, "drive"))
            return true;

        return false;
    }
}
