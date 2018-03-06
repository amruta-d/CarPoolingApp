package com.twilio.mchopra.demoapp.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.EditText;
import android.widget.TimePicker;

import com.twilio.mchopra.demoapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddAvailbilityActivity extends AppCompatActivity implements View.OnClickListener {

    private SimpleDateFormat dateFormatter, dateFormatter1;
    private Button mBtnSelectDate, mBtnFromTime, mBtnToTime, mBtnSetAvailability;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerFromDialog, timePickerToDialog;
    private String availabilityDateStr, availabilityFromTimeStr,availabilityToTimeStr, etFromAddStr;
    private EditText mEtFromAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_availbility);
        setTitle("Add availability");
        init();
        dateFormatter = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        dateFormatter1 = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        mBtnSelectDate.setOnClickListener(this);
        mBtnFromTime.setOnClickListener(this);
        mBtnToTime.setOnClickListener(this);
        mBtnSetAvailability.setOnClickListener(this);
        setDate();
        setFromTime();
        setToTime();


    }

    private void init() {
        mBtnSelectDate = (Button) findViewById(R.id.btn_add_date);
        mBtnFromTime = (Button) findViewById(R.id.et_add_from_time);
        mBtnToTime = (Button) findViewById(R.id.et_add_to_time);
        mEtFromAddress = (EditText) findViewById(R.id.et_add_from_add);
        mBtnSetAvailability = (Button) findViewById(R.id.btn_set_Availability);
    }

    private void setDate() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mBtnSelectDate.setText(dateFormatter.format(newDate.getTime()));
                availabilityDateStr = dateFormatter1.format(newDate.getTime());
                Log.v("date", dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setFromTime() {
        Calendar newCalendar = Calendar.getInstance();
        timePickerFromDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                availabilityFromTimeStr = hourOfDay + ":" + minute;
                mBtnFromTime.setText(availabilityFromTimeStr);

            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE),true);

    }

    private void setToTime() {
        Calendar newCalendar = Calendar.getInstance();
        timePickerToDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                availabilityToTimeStr = hourOfDay + ":" + minute;
                mBtnToTime.setText(availabilityToTimeStr);

            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE),true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_date:
                datePickerDialog.show();
                break;
            case R.id.et_add_from_time:
                timePickerFromDialog.show();
                break;
            case R.id.et_add_to_time:
                timePickerToDialog.show();
                break;
            case R.id.btn_set_Availability:
                etFromAddStr = mEtFromAddress.getText().toString();

                //call task to create
                //get radio button value

        }


    }
}
