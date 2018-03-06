package com.twilio.mchopra.demoapp.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.twilio.mchopra.demoapp.R;
import com.twilio.mchopra.demoapp.Storage.TwilioRiderSharedPreferences;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;

public class AddARideActivity extends AppCompatActivity implements View.OnClickListener {

    private SimpleDateFormat dateFormatter;
    private Button mBtnSelectDate;
    private DatePickerDialog datePickerDialog;
    private Button mBtnRequestRide;
    private EditText fromZip;
    private TimePickerDialog timePickerFromDialog;
    private String availabilityFromTimeStr;
    private Button mBtnFromTime;
    private String hourToUse;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_aride);
        setTitle("Request a ride");
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        init();
        dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        mBtnSelectDate.setOnClickListener(this);
        mBtnRequestRide.setOnClickListener(this);
        mBtnFromTime.setOnClickListener(this);

        setDate();
        setToTime();
    }

    private void init() {

        mBtnSelectDate = (Button) findViewById(R.id.btn_add_date_add_ride);
        mBtnFromTime = (Button) findViewById(R.id.btn_add_time_add_ride);
        mBtnRequestRide = (Button) findViewById(R.id.btn_request_ride);
        fromZip = (EditText) findViewById(R.id.address_text);
        radioGroup = (RadioGroup) findViewById(R.id.rideTo);
    }

    private void setDate() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mBtnSelectDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setToTime() {
        Calendar newCalendar = Calendar.getInstance();
        timePickerFromDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hourToUse = String.valueOf(hourOfDay);
                if(minute<10)
                    availabilityFromTimeStr = hourOfDay + ":0" + minute;
                else
                    availabilityFromTimeStr = hourOfDay + ":" + minute;
                mBtnFromTime.setText(availabilityFromTimeStr);

            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE),true);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_add_date_add_ride:
                datePickerDialog.show();
                break;
            case R.id.btn_add_time_add_ride:
                timePickerFromDialog.show();
                break;
            case R.id.btn_request_ride:
                // Create task;
                if( radioGroup.getCheckedRadioButtonId() == R.id.ride_to_sf){
                    new RequestRide().execute(fromZip.getText().toString(), "94105", hourToUse);
                } else {
                    new RequestRide().execute(fromZip.getText().toString(), "94040", hourToUse);
                }
                break;

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void  onBackPressed() {
        if(isTaskRoot()){
            Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intentMain);
        } else {
            finish();
        }

    }

    public class RequestRide extends AsyncTask<String, Void, String> {
        TwilioRiderSharedPreferences sharedPreferences = new TwilioRiderSharedPreferences(getApplicationContext());

        @Override
        protected String doInBackground(String... params) {
            String taskSid = null;
            try {

                Properties props = new Properties();
                AssetManager assetManager = getApplicationContext().getAssets();
                InputStream inputStreamProps = assetManager.open("server.properties");
                props.load(inputStreamProps);
                HttpURLConnection urlConnection;

                String urlStr = props.getProperty("SERVER_BASE_URL") + "/request_ride";
                URL url = new URL(urlStr);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("from", params[0]);
                jsonObject.put("to", params[1]);
                jsonObject.put("phone", sharedPreferences.getLoggedInUserData().getPhoneNo());
                jsonObject.put("hour", params[2]);
                jsonObject.put("name", sharedPreferences.getLoggedInUserData().getName());

                urlConnection.connect();

                DataOutputStream postBody = new DataOutputStream(urlConnection.getOutputStream());
                postBody.writeBytes(jsonObject.toString());
                postBody.flush();
                postBody.close();

                System.out.println("Response Code" + urlConnection.getResponseCode());

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                JSONObject jsonObjectResponse = new JSONObject(buffer.toString());
                taskSid = jsonObjectResponse.getString("task_sid");

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ERROR", e.getMessage());
            }
            return taskSid;
        }

        @Override
        protected void onPostExecute(String taskSid) {
            sharedPreferences.setTaskSid(taskSid);

        }
    }
}
