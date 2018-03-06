package com.twilio.mchopra.demoapp.Activity;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.app.DatePickerDialog.OnDateSetListener;

import com.twilio.mchopra.demoapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddAvailbilityActivity extends AppCompatActivity implements View.OnClickListener {

    private SimpleDateFormat dateFormatter;
    private Button mBtnSelectDate;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_availbility);
        setTitle("Add availbility");
        init();
        dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        mBtnSelectDate.setOnClickListener(this);

        setDate();


    }

    private void init() {
        mBtnSelectDate = (Button) findViewById(R.id.btn_add_date);
    }

    private void setDate() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
//                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
                Log.v("date", dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View v) {
        datePickerDialog.show();
    }
}
