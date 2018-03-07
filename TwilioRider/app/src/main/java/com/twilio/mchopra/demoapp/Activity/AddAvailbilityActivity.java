package com.twilio.mchopra.demoapp.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.app.DatePickerDialog.OnDateSetListener;
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

public class AddAvailbilityActivity extends AppCompatActivity implements View.OnClickListener {

    private SimpleDateFormat dateFormatter, dateFormatter1;
    private Button mBtnSelectDate, mBtnFromTime, mBtnToTime, mBtnSetAvailability;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerFromDialog, timePickerToDialog;
    private String availabilityDateStr, availabilityFromTimeStr,availabilityToTimeStr, etFromAddStr;
    private EditText mEtFromAddress;
    private RadioGroup radioGroup;
    private String hourToUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_availbility);
        setTitle("Add availability");
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
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

    private void init() {
        mBtnSelectDate = (Button) findViewById(R.id.btn_add_date);
        mBtnFromTime = (Button) findViewById(R.id.et_add_from_time);
        mBtnToTime = (Button) findViewById(R.id.et_add_to_time);
        mEtFromAddress = (EditText) findViewById(R.id.et_add_from_add);
        mBtnSetAvailability = (Button) findViewById(R.id.btn_set_Availability);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
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
                if(minute<10)
                    availabilityFromTimeStr = hourOfDay + ":0" + minute;
                else
                    availabilityFromTimeStr = hourOfDay + ":" + minute;
                hourToUse = String.valueOf(hourOfDay);

                mBtnFromTime.setText(availabilityFromTimeStr);

            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE),true);

    }

    private void setToTime() {
        Calendar newCalendar = Calendar.getInstance();
        timePickerToDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(minute<10)
                    availabilityToTimeStr = hourOfDay + ":0" + minute;
                else
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
                if (radioGroup.getCheckedRadioButtonId() == R.id.radio_sf) {
                    new UpdateAvailability(this).execute("ready", hourToUse);
                } else {
                    new UpdateAvailability(this).execute("ready", hourToUse);
                }
        }
    }


    public class UpdateAvailability extends AsyncTask<String, Void, String> {
        TwilioRiderSharedPreferences sharedPreferences = new TwilioRiderSharedPreferences(getApplicationContext());
        Context context;

        private UpdateAvailability(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String status = null;
            try {

                Properties props = new Properties();
                AssetManager assetManager = getApplicationContext().getAssets();
                InputStream inputStreamProps = assetManager.open("server.properties");
                props.load(inputStreamProps);
                HttpURLConnection urlConnection;

                String urlStr = props.getProperty("SERVER_BASE_URL") + "/driver_available";
                URL url = new URL(urlStr);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", sharedPreferences.getWorkerId());
                jsonObject.put("status", params[0]);
                jsonObject.put("hour", params[1]);

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
                status = jsonObjectResponse.getString("status");

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ERROR", e.getMessage());
            }
            return "Ready to accept rides around "+params[1] + ":00";
        }

        @Override
        protected void onPostExecute(String message) {

            sharedPreferences.setAvailabilityMessage(message);
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
    }

}
