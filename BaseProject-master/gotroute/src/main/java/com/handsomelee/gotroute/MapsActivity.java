package com.handsomelee.gotroute;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity  extends SupportMapFragment implements OnMapReadyCallback {
    private static final String LOGTAG = "MapFragment";
    private GoogleMap mMap;
//    private Location bestLocation;
//    private LocationManager mLocationManager;
//    private double longtitude, latitude;

    public MapsActivity() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
       super.onActivityCreated(savedInstanceState);
       getMapAsync(this);
//       mLocationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
//       System.out.print(getLastBestLocation());
    
    }
    

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.v(LOGTAG, "map is ready");
        
        LatLng sydney = new LatLng(-34, 151);
        
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        Log.v(LOGTAG, "Added Sydney");
    }
  
  /**
   * bestLocation Getter Method
   */
//  public Location getLastBestLocation() {
//    Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//    Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//    long GPSLocationTime = 0;
//    if(locationGPS != null) {
//      GPSLocationTime = locationGPS.getTime();
//    }
//    long NetLocationTime = 0;
//    if(locationNet != null) {
//      NetLocationTime = locationNet.getTime();
//    }
//
//    if(GPSLocationTime - NetLocationTime > 0){
//      return locationGPS;
//    } else {
//      return locationNet;
//    }
//
//  }

//  public Location getNewLocation(){
//    boolean isNetworkEnabled, isGPSEnabled;
//    try {
//      isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//      isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//      if(!isNetworkEnabled & !isGPSEnabled){
//
//      } else {
//        if(isNetworkEnabled){
//          mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 20, 10, this);
//          if()
//        }
//      }
//
//    } catch (Exception e){
//      e.printStackTrace();
//    }
//  }
}
