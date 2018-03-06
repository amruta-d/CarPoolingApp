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

    private EditText mName, mPhoneNo, mPassword;
    private View mProgressView;
    private Button mLogin;
    private String mNameStr, mPhoneNoStr, mPasswordStr;
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
        mPhoneNo = (EditText) findViewById(R.id.etPhoneNo);
        mPassword = (EditText) findViewById(R.id.etPassword);
        mName = (EditText) findViewById(R.id.etName);
        mLogin = (Button) findViewById(R.id.sign_in_button);
        mProgressView = (ProgressBar) findViewById(R.id.login_progress);
    }

    @Override
    public void onClick(View view) {
        mNameStr = mName.getText().toString();
        mPhoneNoStr = mPhoneNo.getText().toString();
        mPasswordStr = mPassword.getText().toString();
        mCurrentUser = new User(mNameStr, mPhoneNoStr, mPasswordStr);
        sharedPreferences.setUserLoggedIn(true);
        sharedPreferences.setLoggedInUserData(mCurrentUser);
        Intent intent = new Intent(this, ChooseModeActivity.class);
        startActivity(intent);


    }

}

