package com.vondear.rxtools.model;

/**
 * Created by Vondear on 2017/6/19.
 */

public class Gps {

    private double mLatitude;
    private double mLongitude;

    public Gps(double longitude, double mLatitude) {
        setLatitude(mLatitude);
        setLongitude(longitude);
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    @Override
    public String toString() {
        return mLongitude + "," + mLatitude;
    }
}
