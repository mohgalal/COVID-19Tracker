package com.example.covid_19tracker;

public class User {
    String ssn;
    double lat,lng;

    public User(String ssn) {
        this.ssn = ssn;
    }

    public User(String ssn, double lat, double lng) {
        this.ssn = ssn;
        this.lat = lat;
        this.lng = lng;
    }

    public User() {
    }

    public String getssn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
