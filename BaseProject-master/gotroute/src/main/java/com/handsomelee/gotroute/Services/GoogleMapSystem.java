package com.handsomelee.gotroute.Services;

import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.*;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapSystem extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMyLocationClickListener
        , GoogleMap.OnMyLocationButtonClickListener {
  
  public enum MARKERTYPE {LatLng, Address, Name}
  
  protected static GoogleMap mMap;
  protected MapView mapView;
  protected View rootView;
  protected int mapViewId;
  protected int layoutActivityId;
  protected int googleMapType;
  protected Marker marker;
  protected static MARKERTYPE markerType;
  protected static LatLng latLng;
  protected static Polyline polyline;
  protected static List<Circle> circles;
  
  public GoogleMapSystem(int mapViewId, int layoutActivityId, int googleMapType) {
    this.mapViewId = mapViewId;
    this.layoutActivityId = layoutActivityId;
    this.googleMapType = googleMapType;
  }
  
  @Override
  public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    Log.v("create", "View");
    rootView = layoutInflater.inflate(layoutActivityId, viewGroup, false);
    mapView = ((MapView) rootView.findViewById(mapViewId));
    mapView.onCreate(bundle);
    mapView.getMapAsync(this);
    addOn();
    return rootView;
  }
  
  public static void EnableMyLocationSetting(long delayMillis) {
    
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        while (mMap == null) ;
        if (mMap != null) {
          //    if (MainActivity.checkGPSPermission(this)) {
          mMap.setMyLocationEnabled(true);
          mMap.getUiSettings().setMyLocationButtonEnabled(false);
          mMap.getUiSettings().setZoomControlsEnabled(true);
          mMap.getUiSettings().setZoomGesturesEnabled(true);
        }
      }
    }, delayMillis);
    
    
  }
  
  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }
  
  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    mMap.setMapType(googleMapType);
    mMap.setOnMyLocationButtonClickListener(this);
    mMap.setOnMyLocationClickListener(this);
    mMap.setOnMapLongClickListener(this);
  }
  
  public static void drawPolyLines(final FragmentActivity activity, final List<LatLng> latLngs) {
    new AsyncTask<Void, Void, Boolean>() {
      @Override
      protected Boolean doInBackground(Void... voids) {
        while (mMap == null){
          if(!RequestDirection.status){
            Log.v("error","d");
            return false;
          }
        };
        if (latLngs.size() > 0) {
          final PolylineOptions polylineOptions = new PolylineOptions();
          final List<CircleOptions> circleOptions = new ArrayList<>();
          polylineOptions.addAll(latLngs);
          circles = new ArrayList<>();
          circleOptions.add(new CircleOptions().center(latLngs.get(0)).fillColor(Color.TRANSPARENT).radius(20).strokeColor(Color.BLUE).strokeWidth(10));
          circleOptions.add(new CircleOptions().center(latLngs.get(latLngs.size() - 1)).fillColor(Color.TRANSPARENT).radius(20).strokeColor(Color.RED).strokeWidth(10));
          latLngs.clear();
          activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              if(polyline != null) {
                polyline.remove();
              }
              polylineOptions.color(Color.BLUE);
              polylineOptions.startCap(new RoundCap());
              polylineOptions.endCap(new SquareCap()).jointType(JointType.ROUND);
              polyline = mMap.addPolyline(polylineOptions);
              circles.add(mMap.addCircle(circleOptions.get(0)));
              circles.add(mMap.addCircle(circleOptions.get(1)));
            }
          });
          return true;
        }
        return false;
      }
    }.execute();
  
  
  }
  
  public static void removePolyline() {
    if(polyline != null)
      polyline.remove();
    if(circles != null){
      for(Circle circle : circles) {
        circle.remove();
      }
      circles.clear();
    }
  }
  
  
  @Override
  public boolean onMyLocationButtonClick() {
    Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
    // Return false so that we don't consume the event and the default behavior still occurs
    // (the camera animates to the user's current position).
    return false;
  }
  
  @Override
  public void onMyLocationClick(@NonNull Location location) {
    
    Toast.makeText(getActivity(), "Current location:\nLatitude : " + location.getLatitude() + "\nLongtitude : " + location.getLongitude(), Toast.LENGTH_LONG).show();
  }
  
  @Override
  public void onMapLongClick(LatLng latLng) {
    if (marker != null) {
      marker.remove();
    }
    MarkerOptions markerOptions = new MarkerOptions();
    markerOptions.position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
    marker = mMap.addMarker(markerOptions);
    markerType = MARKERTYPE.LatLng;
    this.latLng = latLng;
  }
  
  public static MARKERTYPE getMarkerType() {
    return markerType;
  }
  
  public static LatLng getLatLng() {
    return latLng;
  }
  
  public void addOn() {
  
  }
  
  
  @Override
  public void onResume() {
    mapView.onResume();
    super.onResume();
  }
  
  
  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }
  
  @Override
  public void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }
  
  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }
}
