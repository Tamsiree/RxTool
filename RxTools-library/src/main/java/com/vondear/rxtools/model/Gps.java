package com.vondear.rxtools.model;

/**
 * @author Vondear
 * @date 2017/6/19
 */

public class Gps {

    private double mLatitude;
    private double mLongitude;

    public Gps() {
    }

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
