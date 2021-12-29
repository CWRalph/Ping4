package com.example.ping4;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class MyApplication extends Application {
    //Contains the global list of locations
    //Singleton - only one instance of this is allowed
    private static MyApplication singleton;

    //List of locations
    private HashMap<String, LatLng> myPings = new HashMap<>();

    public void onCreate(){
        super.onCreate();
        singleton = this;
        HashMap<String, LatLng> myPings = new HashMap<String, LatLng>();
    }
    public HashMap<String,LatLng> getMyLocations() {
        return myPings;
    }

    //Add a value to the hash map
    public void add_value(String key, LatLng position){
        myPings.put(key,position);
    }
    public void remove_value(String key){
        myPings.remove(key);
    }
    public LatLng get_location(String key){
        return myPings.get(key);
    }
    public void setMyLocations(HashMap<String, LatLng> myPings){
        this.myPings = myPings;
    }

    public MyApplication getInstance(){
        return singleton;
    }

}
