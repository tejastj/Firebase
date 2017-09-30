package com.tejastjexample.firebase;

/**
 * Created by Tejas T J on 16-08-2017.
 */
public class UserLocation {
    String name;
    String Address;
    double latitude;
    double longitude;
    String place;
    public UserLocation(){

    }

    public UserLocation(double latitude, double longitude,String place,String name,String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.place = place;
        this.name = name;
        this.Address = address;
    }
}
