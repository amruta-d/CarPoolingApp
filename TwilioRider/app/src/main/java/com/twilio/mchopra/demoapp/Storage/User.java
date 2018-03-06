package com.twilio.mchopra.demoapp.Storage;

/**
 * Created by adeshmukh on 3/4/18.
 */

public class User {
    private String name, phoneNo, password;


    public User(String name, String phoneNo, String password){
        this.name = name;
        this.phoneNo = phoneNo;
        this.password = password;
    }

    public String getName(){
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getPassword(){
        return password;

    }
}
