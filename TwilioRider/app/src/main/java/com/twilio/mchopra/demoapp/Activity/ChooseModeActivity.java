package com.twilio.mchopra.demoapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.twilio.mchopra.demoapp.R;
import com.twilio.mchopra.demoapp.Storage.TwilioRiderSharedPreferences;

public class ChooseModeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mRideButton, mDriveButton, mCarPoolButton;
    private TwilioRiderSharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mode);
        init();

        mRideButton.setOnClickListener(this);
        mDriveButton.setOnClickListener(this);
        mCarPoolButton.setOnClickListener(this);
    }

    private void init(){
        sharedPreferences = new TwilioRiderSharedPreferences(this);
        mRideButton = (Button) findViewById(R.id.button_ride_mode);
        mDriveButton = (Button) findViewById(R.id.button_drive_mode);
        mCarPoolButton = (Button) findViewById(R.id.button_carpool);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_ride_mode:
                mRideButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mRideButton.setTextColor(getResources().getColor(R.color.white));
                mDriveButton.setBackgroundColor(getResources().getColor(R.color.white));
                mDriveButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                sharedPreferences.setAppMode("ride");
                mCarPoolButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mCarPoolButton.setEnabled(true);
                break;
            case R.id.button_drive_mode:
                mDriveButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mDriveButton.setTextColor(getResources().getColor(R.color.white));
                sharedPreferences.setAppMode("drive");
                mRideButton.setBackgroundColor(getResources().getColor(R.color.white));
                mRideButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                mCarPoolButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mCarPoolButton.setEnabled(true);
                break;
            case R.id.button_carpool:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
