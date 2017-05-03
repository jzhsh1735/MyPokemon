package com.example.android.mypokemon.json;

import android.location.Location;

public class Pokemon {

    private int id;
    private Location location;

    public Pokemon(int id, Location location) {
        this.id = id;
        this.location = location;
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public double getLongitude() {
        return location.getLongitude();
    }

    @Override
    public String toString() {
        return "id(" + id + "),lat(" + location.getLatitude() + "),lng(" + location.getLongitude() + ")";
    }

    public boolean isWithinArea(Location center, double radius) {
        return distance(center.getLatitude(), center.getLongitude(), location.getLatitude(), location.getLongitude()) < radius;
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344 * 1000;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
