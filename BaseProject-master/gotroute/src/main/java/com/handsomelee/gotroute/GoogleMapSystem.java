package com.handsomelee.gotroute;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapSystem extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener {
  
  protected GoogleMap mMap;
  protected MapView mapView;
  protected View rootView;
  protected int mapViewId;
  protected int layoutActivityId;
  protected int googleMapType;
  protected Marker marker;
  
  public GoogleMapSystem(int mapViewId, int layoutActivityId, int googleMapType) {
    this.mapViewId = mapViewId;
    this.layoutActivityId = layoutActivityId;
    this.googleMapType = googleMapType;
  }
  
  @Override
  public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    rootView = layoutInflater.inflate(layoutActivityId, viewGroup, false);
    mapView = ((MapView) rootView.findViewById(mapViewId));
    mapView.onCreate(bundle);
    mapView.getMapAsync(this);
    addOn();
    return rootView;
  }
  
  public void EnableMyLocationSetting() {
    while (!mMap.isMyLocationEnabled()) {
      if (MainActivity.checkGPSPermission(getActivity())) {
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setOnMapLongClickListener(this);
      }
    }
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
    EnableMyLocationSetting();

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
      if(marker != null) {
        marker.remove();
      }
      MarkerOptions markerOptions = new MarkerOptions();
      markerOptions.position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
      marker = mMap.addMarker(markerOptions);
    }
    
    public void addOn(){
    
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
