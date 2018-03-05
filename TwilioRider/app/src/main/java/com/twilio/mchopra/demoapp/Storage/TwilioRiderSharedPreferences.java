package com.twilio.mchopra.demoapp.Storage;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TwilioRiderSharedPreferences {

    public static final String SP_NAME = "userDetails";
    SharedPreferences sharedPreferences;


    public TwilioRiderSharedPreferences(Context context){

        try {

            sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        catch (Exception e){
            Log.v("TwilioRiderSP",e.toString());
            e.printStackTrace();
        }

    }

    public void setUserLoggedIn(boolean isLoggedIn){
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putBoolean("isLoggedIn", isLoggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn(){
        if (sharedPreferences.getBoolean("isLoggedIn",false)==true){
            return true;
        }
        else{
            return false;
        }

    }

    public void setLoggedInUserData(User user){
        try {
            SharedPreferences.Editor spEditor = sharedPreferences.edit();

            spEditor.putString("username", user.getUsername());
            spEditor.putString("password", user.getPassword());
            spEditor.commit();
        }
        catch (Exception e){
            Log.v("TwilioRiderSP", e.toString());

        }
    }
    public User getLoggedInUserData(){
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password","");
        User loggedInUser = new User(username,password);
        return loggedInUser;
    }

    public void setAppMode(String appMode){
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putString("appMode", appMode);
        spEditor.commit();
    }
    public String getAppMode(){
        String appMode = sharedPreferences.getString("appMode", "");
        return appMode;
    }

}
