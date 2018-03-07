package com.twilio.mchopra.demoapp.Storage;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Set;

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

            spEditor.putString("name", user.getName());
            spEditor.putString("phoneNo", user.getPhoneNo());
            spEditor.putString("password", user.getPassword());
            spEditor.commit();
        }
        catch (Exception e){
            Log.v("TwilioRiderSP", e.toString());

        }
    }
    public User getLoggedInUserData(){
        String name = sharedPreferences.getString("name","");
        String phoneNo = sharedPreferences.getString("phoneNo", "");
        String password = sharedPreferences.getString("password","");
        User loggedInUser = new User(name, phoneNo, password);
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

    public void setWorkerId(String workerId){
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putString("workerId", workerId);
        spEditor.commit();
    }
    public String getWorkerId(){
        String workerId = sharedPreferences.getString("workerId", "");
        return workerId;
    }

    public void setTaskSid(String taskSid){
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putString("taskSid", taskSid);
        spEditor.commit();
    }
    public String getTaskSid(){
        String taskSid = sharedPreferences.getString("taskSid", "");
        return taskSid;
    }

    public void setAvailabilityMessage(String availabilityMessage){
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putString("availabilityMessage", availabilityMessage);
        spEditor.commit();
    }

    public String getAvailabilityMessage(){
        String availabilityMessage = sharedPreferences.getString("availabilityMessage", "");
        return availabilityMessage;
    }

    public void setReservationSid(String reservationSid){
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putString("reservationSid", reservationSid);
        spEditor.commit();
    }
    public String getReservationSid() {
        String reservationSid = sharedPreferences.getString("reservationSid", "");
        return reservationSid;
    }

    public void setDriverisAvailable(boolean driverisAvailable){
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putBoolean("driverisAvailable", driverisAvailable);
        spEditor.apply();
    }


    public boolean getDriverisAvailable() {
        boolean driverisAvailable = sharedPreferences.getBoolean("driverisAvailable", false);
        return driverisAvailable;
    }


    public void setDriverTaskisPending(boolean driverTaskisPending){
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putBoolean("driverTaskisPending", driverTaskisPending);
        spEditor.apply();
    }


    public boolean getDriverTaskisPending() {
        boolean driverisAvailable = sharedPreferences.getBoolean("driverTaskisPending", false);
        return driverisAvailable;
    }

    public void setDriverTaskAccepted(boolean setDriverTaskAvailable){
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putBoolean("DriverTaskAvailable", setDriverTaskAvailable);
        spEditor.apply();
    }


    public boolean getDriverTaskAccepted() {
        boolean driverTaskAvailable = sharedPreferences.getBoolean("DriverTaskAvailable", false);
        return driverTaskAvailable;
    }

    public void setDriverAvailbilitytime(String driverAvailbilitytime){
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putString("driverAvailbilitytime", driverAvailbilitytime);
        spEditor.apply();
    }




    public String getDriverAvailbilitytime() {
        String driverAvailbilitytime = sharedPreferences.getString("driverAvailbilitytime", null);
        return driverAvailbilitytime;
    }


    public void setPendingTaskRiderName(String pendingTaskRiderName){
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putString("pendingTaskRiderName", pendingTaskRiderName);
        spEditor.apply();
    }


    public String getPendingTaskRiderName() {
        String pendingTaskRiderName = sharedPreferences.getString("pendingTaskRiderName", null);
        return pendingTaskRiderName;
    }

    public void setPendingTaskRiderZipCode(String pendingTaskRiderZipCode){
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putString("pendingTaskRiderZipCode", pendingTaskRiderZipCode);
        spEditor.apply();
    }


    public String getPendingTaskRiderZipCode() {
        String pendingTaskRiderZipCode = sharedPreferences.getString("pendingTaskRiderZipCode", null);
        return pendingTaskRiderZipCode;
    }

    public void setRideDetails(Set<String> taskDetails){
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putStringSet("taskAssigned", taskDetails);
        spEditor.apply();
    }


    public Set<String> getRideDetails() {
        Set<String> cart = sharedPreferences.getStringSet("taskAssigned", null);
        if (cart != null)
            Log.v("SP--cart size", cart.size() + "");
        return cart;
    }

    public void emptySharedPrefences(){
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.clear();
        spEditor.commit();

    }

}
