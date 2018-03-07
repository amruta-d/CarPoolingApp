package com.twilio.mchopra.demoapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.twilio.mchopra.demoapp.R;
import com.twilio.mchopra.demoapp.Storage.TwilioRiderSharedPreferences;
import com.twilio.mchopra.demoapp.Storage.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar mToolBar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TwilioRiderSharedPreferences sharedPreferences;
    private FloatingActionButton mAddCarpoolInfo;
//    private RecyclerAdapter mAdapter;
    private TextView mNoUpcomingRides;
    private TextView upcomingRide;
    private RelativeLayout relLayDriverAvailability, relLayDriverPenTask, relLayoutDriverUpRide;
    private TextView relLayDriverAvailText2, relLayDriverPendingTaskText2, relLayDriverPendingTaskText3,
            relLayoutDriverUpcomingText2, relLayoutDriverUpcomingText3;
    private Button acceptPendingRide, rejectPendingRide;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = new TwilioRiderSharedPreferences(this);
        mAddCarpoolInfo = (FloatingActionButton) findViewById(R.id.fab_add_ride);
        mNoUpcomingRides = (TextView) findViewById(R.id.text_no_upcoming_rides);
        upcomingRide = (TextView) findViewById(R.id.upcoming_ride);
        upcomingRide.setVisibility(View.INVISIBLE);
        init();
        setTitle("Hello " + sharedPreferences.getLoggedInUserData().getName());
        Log.d("worker ID1", sharedPreferences.getWorkerId());


        if(sharedPreferences.getAppMode().equalsIgnoreCase("ride")) {

            if (sharedPreferences.getReservationSid() != null && sharedPreferences.getReservationSid().length() > 0) {
                // This means the reservation has been accepted and we need to display message.
                displayAcceptedReservation();

            } else {
                getRideDetails();
            }
        }

//        if(sharedPreferences.getAppMode().equalsIgnoreCase("ride")){
//            // See if pending task is already set.
//            getRideDetails();
//
//        } else {
//            // drive. Show availability
//            getDriverAvailabilityDetails();
//        }


        setMainActivityUI();


        try {

            mToolBar = (Toolbar) findViewById(R.id.main_activity_toolbar);
            setSupportActionBar(mToolBar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            Log.e("Main Activity", e.toString());
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerRoot);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolBar,
                R.string.action_drawer_open,
                R.string.action_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        NavigationView nv = (NavigationView) findViewById(R.id.navView);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                String txt;
                switch (menuItem.getItemId()) {
                    case R.id.logout_drawer:
                        sharedPreferences.emptySharedPrefences();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    case R.id.set_idle_drawer:
                        if("drive".equals(sharedPreferences.getAppMode())){
                            new ResetAvailability(MainActivity.this).execute();

                        }
                        break;
                    default:
                        txt = "Hang Tight! More Features coming soon!";
                        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_LONG).show();
                }

                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        mAddCarpoolInfo.setOnClickListener(this);
        acceptPendingRide.setOnClickListener(this);
    }

    private void init(){
        relLayDriverAvailability = (RelativeLayout) findViewById(R.id.relative_layout_driver_availability);
        relLayDriverPenTask = (RelativeLayout) findViewById(R.id.relative_layout_driver_pending_task);
        relLayoutDriverUpRide = (RelativeLayout) findViewById(R.id.relative_layout_driver_upcoming_ride);
        relLayDriverAvailText2 = (TextView) findViewById(R.id.relative_layout_driver_availability_text2);
        relLayDriverPendingTaskText2 = (TextView) findViewById(R.id.relative_layout_driver_pending_task_text2);
        relLayDriverPendingTaskText3 = (TextView) findViewById(R.id.relative_layout_driver_pending_task_text3);
        acceptPendingRide = (Button) findViewById(R.id.relative_layout_driver_pending_task_button);
        rejectPendingRide = (Button) findViewById(R.id.relative_layout_driver_pending_task_button2);
        relLayoutDriverUpcomingText2 = (TextView) findViewById(R.id.relative_layout_driver_upcoming_ride_text2);
        relLayoutDriverUpcomingText3 = (TextView) findViewById(R.id.relative_layout_driver_upcoming_ride_text3);



    }

    private void setMainActivityUI(){
        if(sharedPreferences.getAppMode().equals("ride")){

        } else {
            if(sharedPreferences.getDriverisAvailable() && !sharedPreferences.getDriverTaskisPending()){
                relLayDriverAvailability.setVisibility(View.VISIBLE);
                relLayDriverAvailText2.setText("Time: " + sharedPreferences.getDriverAvailbilitytime());

                relLayoutDriverUpRide.setVisibility(View.GONE);
                relLayDriverPenTask.setVisibility(View.GONE);
                mNoUpcomingRides.setVisibility(View.GONE);
            } else if (sharedPreferences.getDriverTaskisPending()) {
                relLayDriverPenTask.setVisibility(View.VISIBLE);
                relLayDriverPendingTaskText2.setText("Rider Name: " + sharedPreferences.getPendingTaskRiderName());
                relLayDriverPendingTaskText3.setText("From: " + sharedPreferences.getPendingTaskRiderZipCode());

                relLayDriverAvailability.setVisibility(View.GONE);
                relLayoutDriverUpRide.setVisibility(View.GONE);
                mNoUpcomingRides.setVisibility(View.GONE);
            } else if (sharedPreferences.getDriverTaskAccepted()){
                relLayoutDriverUpRide.setVisibility(View.VISIBLE);
                relLayoutDriverUpcomingText2.setText("With: " + sharedPreferences.getPendingTaskRiderName());
                relLayoutDriverUpcomingText3.setText("From: " + sharedPreferences.getPendingTaskRiderZipCode());

                relLayDriverAvailability.setVisibility(View.GONE);
                relLayDriverPenTask.setVisibility(View.GONE);
                mNoUpcomingRides.setVisibility(View.GONE);
            } else {
                mNoUpcomingRides.setVisibility(View.VISIBLE);


                relLayDriverAvailability.setVisibility(View.GONE);
                relLayoutDriverUpRide.setVisibility(View.GONE);
                relLayDriverPenTask.setVisibility(View.GONE);

            }
        }
    }


    @Override
    protected void onResume() {
        setMainActivityUI();
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_add_ride:
                if("drive".equals(sharedPreferences.getAppMode())){
                    Intent intent = new Intent(this, AddAvailbilityActivity.class);
                    startActivity(intent);
                } else {
                    //Request a ride.
                    Intent intent = new Intent(this, AddARideActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.relative_layout_driver_pending_task_button:
                //to do call async task
                new AcceptRideAsyncTask().execute(sharedPreferences.getWorkerId());
        }

    }


    private void getRideDetails(){
        User currentLoggedUser = sharedPreferences.getLoggedInUserData();

        if(sharedPreferences.getTaskSid() != null && sharedPreferences.getTaskSid().length() > 1){
            mNoUpcomingRides.setVisibility(View.INVISIBLE);
            upcomingRide.setVisibility(View.VISIBLE);
            upcomingRide.setText("Hi "+currentLoggedUser.getName()+". Please hang tight while we look for a Twilion to give you a ride." );
        } else {
            mNoUpcomingRides.setVisibility(View.VISIBLE);
        }
    }

    private void displayAcceptedReservation(){
        User currentLoggedUser = sharedPreferences.getLoggedInUserData();

        if(sharedPreferences.getReservationSid() != null && sharedPreferences.getReservationSid().length() > 1){
            mNoUpcomingRides.setVisibility(View.INVISIBLE);
            upcomingRide.setVisibility(View.VISIBLE);
            upcomingRide.setText("Congratulations "+currentLoggedUser.getName()+ "!! We found you a ride. You will be riding with John.");
        } else {
            mNoUpcomingRides.setVisibility(View.VISIBLE);
        }
    }

    private void getDriverAvailabilityDetails(){
        User currentLoggedUser = sharedPreferences.getLoggedInUserData();
        if(sharedPreferences.getAvailabilityMessage() != null && sharedPreferences.getAvailabilityMessage().length() > 1){
            mNoUpcomingRides.setVisibility(View.INVISIBLE);
            upcomingRide.setVisibility(View.VISIBLE);
            upcomingRide.setText(sharedPreferences.getAvailabilityMessage());

        } else {
            mNoUpcomingRides.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    public class AcceptRideAsyncTask extends AsyncTask<String, Void, String> {
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

                String urlStr = props.getProperty("SERVER_BASE_URL") + "/accept_ride";
                URL url = new URL(urlStr);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", params[0]);

                urlConnection.connect();

                DataOutputStream postBody = new DataOutputStream(urlConnection.getOutputStream());
                postBody.writeBytes(jsonObject.toString());
                postBody.flush();
                postBody.close();

                System.out.println("AcceptRide Response Code" + urlConnection.getResponseCode());


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ERROR", e.getMessage());
            }
            return workerId;
        }

        @Override
        protected void onPostExecute(String workerId) {
            sharedPreferences.setDriverTaskAccepted(true);
            sharedPreferences.setDriverisAvailable(false);
            sharedPreferences.setDriverTaskisPending(false);

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);

        }
    }


    public class ResetAvailability extends AsyncTask<Void, Void, Void> {
        TwilioRiderSharedPreferences sharedPreferences = new TwilioRiderSharedPreferences(getApplicationContext());
        Context context;

        private ResetAvailability(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
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
                jsonObject.put("status", "ready");
                jsonObject.put("hour", "8");

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
            sharedPreferences.setDriverisAvailable(true);
            sharedPreferences.setDriverTaskisPending(false);
            sharedPreferences.setDriverTaskAccepted(false);
            sharedPreferences.setDriverAvailbilitytime("8:00 to 9:00");
            return null;
        }
    }
}
