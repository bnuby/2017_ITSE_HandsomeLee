package com.handsomelee.gotroute.Services;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

public class LocationSystem implements LocationListener {
  
  private LocationManager locationManager;
  private Location location;
  
  public LocationSystem(Context context) {
    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    
    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
      location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }
  }
  
  public Location getCurrentLocation() {
    return location;
  }
  
  public double getLongtitude() {
    return location.getLongitude();
  }
  
  public Location getLocation() {
    return location;
  }
  
  public double getLatitude() {
    return location.getLatitude();
  }
  
  @Override
  public void onLocationChanged(Location location) {
    this.location = location;
    Log.v("latitude", location.getLatitude() + "");
  }
  
  @Override
  public void onStatusChanged(String s, int i, Bundle bundle) {
  
  }
  
  @Override
  public void onProviderEnabled(String s) {
  
  }
  
  @Override
  public void onProviderDisabled(String s) {
  
  }
}
