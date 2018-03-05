package com.twilio.mchopra.demoapp.Storage;

/**
 * Created by adeshmukh on 3/4/18.
 */

public class User {
    private String username, password;


    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;

    }
}
