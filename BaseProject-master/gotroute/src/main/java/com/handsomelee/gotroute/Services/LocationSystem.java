package com.handsomelee.gotroute.Services;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.handsomelee.gotroute.Controller.MapsActivity;
import com.handsomelee.gotroute.MainActivity;

public class LocationSystem implements LocationListener {

  static boolean reset;
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

  public double getLongitude() {
    return location.getLongitude();
  }

  public LatLng getLatLng() {
    return new LatLng(location.getLatitude(), location.getLongitude());
  }

  public double getLatitude() {
    return location.getLatitude();
  }

  @Override
  public void onLocationChanged(Location location) {
    this.location = location;

    if (MapsActivity.getProgressType() == MapsActivity.ProgressType.Navigation) {
      boolean checkInLine = false;
      CameraPosition cameraPosition = new CameraPosition.Builder()
              .target(getLatLng())
              .zoom(25f)
              .tilt(80f)
              .build();
      MapsActivity.getmMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
      reset = false;
      for (int i = 1; i < MainActivity.latLngs.size() - 1; i++) {
        if (checkIsInRange(location, MainActivity.latLngs.get(i), MainActivity.latLngs.get(i + 1))) {
          checkInLine = true;
          break;
        }
      }
      if (!checkInLine) {

        Button button = new Button(MainActivity.mActivity);
        MainActivity.requestNavigation(button);
        button.destroyDrawingCache();
      }
    } else if (!reset) {
      CameraPosition cameraPosition = new CameraPosition.Builder()
              .target(getLatLng())
              .zoom(15f)
              .build();
      MapsActivity.getmMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
      reset = true;
    }
  }

  public boolean checkIsInRange(Location location, LatLng... latLng) {
    final double EPSILON = 1 * Math.pow(10, -5);
    Log.v(latLng[0].latitude + "," + latLng[0].longitude, latLng[1].latitude + "," + latLng[1].longitude);
    double a = (latLng[0].latitude - latLng[1].latitude) / (latLng[0].longitude - latLng[1].longitude);
    double b = latLng[1].latitude - a * latLng[1].longitude;
    Log.v("abs", Math.abs(location.getLatitude() - (a * location.getLongitude() + b)) + "");
    if (Math.abs(location.getLatitude() - (a * location.getLongitude() + b)) < EPSILON) {
      return true;
    }
    return false;
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
