package com.handsomelee.gotroute.Model;

import com.google.android.gms.maps.model.LatLng;

public class Location {
    private double latitude;
    private double longitude;
    
    public Location(double latitude, double longitude) {
      this.latitude = latitude;
      this.longitude = longitude;
    }
    
    // latitude Getter Method
    public double getLatitude() {
      return latitude;
    }
    
    // longtitude Getter Method
    public double getLongitude() {
      return longitude;
    }
    
    public LatLng getLatLng() {
      return new LatLng(latitude, longitude);
    }
    
    public void setLatLng(android.location.Location location) {
      if(location != null) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
      }
    }
  
  @Override
  public String toString() {
    return "{" +
            "latitude:" + latitude +
            ", longitude:" + longitude +
            '}';
  }
  
  @Override
  public boolean equals(Object o) {
    android.location.Location location = (android.location.Location) o;
    return Double.compare(location.getLatitude(), latitude) == 0 &&
            Double.compare(location.getLongitude(), longitude) == 0;
  }
  
}