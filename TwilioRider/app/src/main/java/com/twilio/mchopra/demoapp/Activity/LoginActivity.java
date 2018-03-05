package com.twilio.mchopra.demoapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.twilio.mchopra.demoapp.R;
import com.twilio.mchopra.demoapp.Storage.TwilioRiderSharedPreferences;
import com.twilio.mchopra.demoapp.Storage.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mUserName, mPasswordView;
    private View mProgressView;
    private Button mLogin;
    private String mUsernameStr, mPasswordStr;
    private User mCurrentUser;
    private TwilioRiderSharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        init();
        mLogin.setOnClickListener(this);
    }


    private void init() {
        sharedPreferences = new TwilioRiderSharedPreferences(this);
        mUserName = (EditText) findViewById(R.id.etUsername);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLogin = (Button) findViewById(R.id.sign_in_button);
        mProgressView = (ProgressBar) findViewById(R.id.login_progress);
    }

    @Override
    public void onClick(View view) {
        mUsernameStr = mUserName.getText().toString();
        mPasswordStr = mPasswordView.getText().toString();
        mCurrentUser = new User(mUsernameStr, mPasswordStr);
        sharedPreferences.setUserLoggedIn(true);
        sharedPreferences.setLoggedInUserData(mCurrentUser);
        Intent intent = new Intent(this, ChooseModeActivity.class);
        startActivity(intent);


    }

}

