package com.twilio.mchopra.demoapp.Activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.twilio.mchopra.demoapp.R;
import com.twilio.mchopra.demoapp.Storage.TwilioRiderSharedPreferences;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import android.os.Handler;
import android.widget.Toast;

public class ChooseModeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mRideButton, mDriveButton, mCarPoolButton;
    private TwilioRiderSharedPreferences sharedPreferences;
    private String appModeStr, workerIdStr;
//    AlarmReceiver alarm;
//    private Handler handler = new Handler();
//    private Runnable runnableCode = new Runnable() {
//        @Override
//        public void run() {
//            // Do something here on the main thread
//            Log.d("Handlers", "Called on main thread");
////            Toast.makeText(this,"sdjbsdjh",Toast.LENGTH_SHORT);
//            // Repeat this the same runnable code block again another 2 seconds
//            // 'this' is referencing the Runnable object
//            handler.postDelayed(this, 2000);
//        }
//    }; // Start the initial runnable task by posting through the handler
//         handler.post(runnableCode);


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
//        alarm =  new AlarmReceiver();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_ride_mode:
                mRideButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mRideButton.setTextColor(getResources().getColor(R.color.white));
                mDriveButton.setBackgroundColor(getResources().getColor(R.color.white));
                mDriveButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                appModeStr = "ride";
                mCarPoolButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mCarPoolButton.setEnabled(true);
                sharedPreferences.setWorkerId("");
                break;
            case R.id.button_drive_mode:
                mDriveButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mDriveButton.setTextColor(getResources().getColor(R.color.white));
                appModeStr = "drive";
                mRideButton.setBackgroundColor(getResources().getColor(R.color.white));
                mRideButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                mCarPoolButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mCarPoolButton.setEnabled(true);

                GetWorkerDetails workerDetailsTask = new GetWorkerDetails();
                workerDetailsTask.execute(sharedPreferences.getLoggedInUserData().getName());

                break;
            case R.id.button_carpool:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                sharedPreferences.setAppMode(appModeStr);
//                if("drive".equals(appModeStr))
//                      handler.post(runnableCode);

                break;
        }
    }



    public class GetWorkerDetails extends AsyncTask<String, Void, String> {
        TwilioRiderSharedPreferences sharedPreferences = new TwilioRiderSharedPreferences(getApplicationContext());

        @Override
        protected String doInBackground(String... params) {
            String workerId = null;
            try {

                Properties props = new Properties();
                AssetManager assetManager = getApplicationContext().getAssets();
                InputStream inputStreamProps = assetManager.open("server.properties");
                props.load(inputStreamProps);
                HttpURLConnection urlConnection;

                String urlStr = props.getProperty("SERVER_BASE_URL") + "/get_worker";
                URL url = new URL(urlStr);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", params[0]);

                urlConnection.connect();

                DataOutputStream postBody = new DataOutputStream(urlConnection.getOutputStream());
                postBody.writeBytes(jsonObject.toString());
                postBody.flush();
                postBody.close();

                System.out.println("AAAA" + urlConnection.getResponseCode());

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
                workerId = jsonObjectResponse.getString("id");
                Log.d("workerId", workerId);
                sharedPreferences.setWorkerId(workerId);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ERROR", e.getMessage());
            }
            return workerId;
        }

//        @Override
//        protected void onPostExecute(String workerId) {
//
//            workerIdStr = workerId;
//            sharedPreferences.setWorkerId(workerId);
//
//        }
    }


}
