package com.twilio.mchopra.demoapp.Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.twilio.mchopra.demoapp.R;
import com.twilio.mchopra.demoapp.Storage.TwilioRiderSharedPreferences;
import com.twilio.mchopra.demoapp.Storage.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar mToolBar;
    private RecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TwilioRiderSharedPreferences sharedPreferences;
    private FloatingActionButton mAddCarpoolInfo;
//    private RecyclerAdapter mAdapter;
    private TextView mNoUpcomingRides;
    private TextView upcomingRide;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = new TwilioRiderSharedPreferences(this);
        mAddCarpoolInfo = (FloatingActionButton) findViewById(R.id.fab_add_ride);
        mRecyclerView = (RecyclerView) findViewById(R.id.main_activity_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNoUpcomingRides = (TextView) findViewById(R.id.text_no_upcoming_rides);
        upcomingRide = (TextView) findViewById(R.id.upcoming_ride);
        upcomingRide.setVisibility(View.INVISIBLE);
        setTitle("Hello " + sharedPreferences.getLoggedInUserData().getName());
        Log.d("worker ID1", sharedPreferences.getWorkerId());

        if(sharedPreferences.getAppMode().equalsIgnoreCase("ride")){
            // See if pending task is already set.
            getRideDetails();

        } else {
            // drive. Show availability
            getDriverAvailabilityDetails();
        }


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
                        sharedPreferences.setUserLoggedIn(false);
                        sharedPreferences.setAppMode("");
                        sharedPreferences.setWorkerId("");
                        sharedPreferences.setTaskSid("");
                        sharedPreferences.setAvailabilityMessage("");
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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
    }


    public void onClick(View view) {
        if("drive".equals(sharedPreferences.getAppMode())){
            Intent intent = new Intent(this, AddAvailbilityActivity.class);
            startActivity(intent);
        } else {
            //Request a ride.
            Intent intent = new Intent(this, AddARideActivity.class);
            startActivity(intent);
        }

    }


    private void getRideDetails(){
        User currentLoggedUser = sharedPreferences.getLoggedInUserData();

        if(sharedPreferences.getTaskSid() != null && sharedPreferences.getTaskSid().length() > 1){
            mNoUpcomingRides.setVisibility(View.INVISIBLE);
            upcomingRide.setVisibility(View.VISIBLE);
            upcomingRide.setText(sharedPreferences.getTaskSid() + "scheduled for user "+currentLoggedUser.getName() );
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
}
