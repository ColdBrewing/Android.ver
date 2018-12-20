package com.example.knumap;

public class GPS {
    public double latitude;
    public double longitude;

    public GPS(double _latitude, double _longitude){
        this.latitude = _latitude;
        this.longitude = _longitude;
    }

    public GPS(String _latitude, String _longitude){
        this.latitude = Double.parseDouble( _latitude);
        this.longitude = Double.parseDouble( _longitude);
    }

    @Override
    public String toString() {
        return String.format("{latitude: %s, longitude: %s}", this.latitude, this.longitude);
    }
}
